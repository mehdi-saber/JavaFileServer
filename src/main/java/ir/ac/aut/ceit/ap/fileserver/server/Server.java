package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressWriter;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CRequest;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CResponse;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.Subject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Receiver;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.*;


public class Server {
    private SFileSystem fileSystem;
    private Receiver receiver;
    private SecurityManager securityManager;
    private SFileStorage fileStorage;
    private ClientManager clientManager;
    private Runnable finalCallback;
    private int port = 5050;

    public Server()  {
        try {
            receiver = new Receiver(port, new SRouter(this));
            fileSystem = new SFileSystem();
            fileStorage = new SFileStorage(0L);
            clientManager = new ClientManager(2);
            securityManager = new SecurityManager();

            finalCallback = () -> {
                clientManager.save();
                fileSystem.save();
            };


//            FSDirectory directory = fileSystem.addDirectory(FSDirectory.ROOT, "dawd");
//            for (int i = 0; i < 10; i++) {
//                fileSystem.addFile(directory, i + ".pdf", 1L, new HashSet<>());
//                fileSystem.addFile(FSDirectory.ROOT, i + ".pdf", 1L, new HashSet<>());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SendingMessage loginUser(ReceivingMessage request) {
        int listenPort = (int) request.getParameter("listenPort");
        String username = (String) request.getParameter("username");
        ClientInfo client = new ClientInfo(request.getSenderAddress(), listenPort, username);
        SendingMessage response = securityManager.loginUser(request, client);
        if (response.getTitle().equals(S2CResponse.LOGIN_OK))
            clientManager.addClient(client);
        return response;
    }

    SendingMessage fetchDirectory(Message request) {
        FSDirectory directory = (FSDirectory) request.getParameter("directory");
        SendingMessage response = new SendingMessage(S2CResponse.FETCH_DIRECTORY_OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    private File createTempFile() throws IOException {
        return File.createTempFile("server", "dl");
    }

    SendingMessage upload(ReceivingMessage request) {
        try {
            String fileName = (String) request.getParameter("fileName");
            FSDirectory directory = (FSDirectory) request.getParameter("directory");

            if (fileSystem.pathExists(directory, fileName, false))
                return new SendingMessage(S2CResponse.UPLOAD_FILE_REPEATED);

            File downloadingFile = createTempFile();
            Long fileSize = request.getStreamSize("file");
            FileOutputStream downloadOutput = new FileOutputStream(downloadingFile);
            IOUtil.writeI2O(downloadOutput, request.getInputStream("file"), fileSize);
            downloadOutput.close();

            SendingMessage response = new SendingMessage(S2CResponse.UPLOAD_FILE_OK);

            PipedInputStream pipedInputStream = new PipedInputStream();
            response.addInputStream("status", pipedInputStream, Long.MAX_VALUE);
            ProgressWriter progressWriter = new ProgressWriter(new PipedOutputStream(pipedInputStream));

            new Thread(() -> sendParts(downloadingFile, directory, fileName, progressWriter)).start();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    SendingMessage download(ReceivingMessage request) {
        try {

            FSFile file = (FSFile) request.getParameter("file");
            Long fileSize = file.getSize();

            Map<Long, ClientInfo> partMap = clientManager.getFileClientList(file);

            SendingMessage response = new SendingMessage(S2CResponse.DOWNLOAD_FILE_OK);

            PipedOutputStream pipedOutputStream = new PipedOutputStream();
            PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);
            response.addInputStream("status", pipedInputStream, Long.MAX_VALUE);

            ProgressWriter out = new ProgressWriter(pipedOutputStream);

            File downloadingFile = createTempFile();
            response.addInputStream("file", new FileInputStream(downloadingFile), fileSize);

            Thread thread = new Thread(() -> {
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(downloadingFile);
                    for (Map.Entry<Long, ClientInfo> entry : partMap.entrySet()) {
                        Long partId = entry.getKey();
                        ClientInfo client = entry.getValue();

                        SRequest partRequest = new SRequest(S2CRequest.SEND_PART);
                        partRequest.addParameter("partId", partId);
                        ResponseCallback responseCallback = partResponse -> IOUtil.writeI2O(
                                fileOutputStream,
                                partResponse.getInputStream("part"),
                                partResponse.getStreamSize("part"),
                                out
                        );
                        partRequest.setResponseCallback(responseCallback);
                        partRequest.send(client).join();
                    }
                    out.close();
                    fileOutputStream.close();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendParts(File downloadingFile, FSDirectory directory, String fileName, ProgressWriter progressWriter) {
        try {
            List<Long> parts = fileStorage.splitFile(downloadingFile, progressWriter);
            Long fileSize = downloadingFile.length();
            downloadingFile.delete();
            Map<ClientInfo, Set<Long>> destinations =
                    clientManager.getDestinations(parts);

            for (ClientInfo client : destinations.keySet()) {
                Set<Long> clientParts = destinations.get(client);

                SRequest partRequest = new SRequest(S2CRequest.RECEIVE_PART);
                Map<Long, String> hashList = new HashMap<>();
                for (Long partId : clientParts) {
                    File partFile = fileStorage.getFileById(partId);
                    String hash = DigestUtils.sha256Hex(new FileInputStream(partFile));
                    hashList.put(partId, hash);
                    partRequest.addInputStream(partId.toString(), new FileInputStream(partFile), partFile.length());
                    partRequest.addProgressCallback(partId.toString(), progressWriter);
                }
                partRequest.addParameter("hashList", hashList);
                partRequest.send(client).join();
            }

            progressWriter.close();

            clientManager.updateParts(destinations);

            for (Long part : parts)
                fileStorage.getFileById(part).delete();
            fileSystem.addFile(directory, fileName, fileSize, new HashSet<>(parts));

            refreshClients();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshClients() {
        for (ClientInfo client : clientManager.getClientList()) {
            SRequest partRequest = new SRequest(S2CRequest.REFRESH_DIRECTORY);
            partRequest.send(client);
        }
    }

    public void addFile(FSFile info, byte[] data) {

    }

    public void removeFile(FSFile info) {

    }

    public void fetchFile(FSFile info) {

    }

    SendingMessage renameFile(ReceivingMessage request) {
        FSPath path = (FSPath) request.getParameter("path");
        String newName = (String) request.getParameter("newName");
        boolean result = fileSystem.renamePath(path, newName);
        if (result) {
            refreshClients();
            return new SendingMessage(S2CResponse.MOVE_PATH_OK);
        } else
            return new SendingMessage(S2CResponse.MOVE_PATH_ALREADY_EXISTS);
    }

    SendingMessage createNewDirectory(ReceivingMessage request) {
        FSDirectory parent = (FSDirectory) request.getParameter("parent");
        String name = (String) request.getParameter("name");
        FSDirectory newDirectory = fileSystem.addDirectory(parent, name);
        if (newDirectory != null) {
            refreshClients();
            return new SendingMessage(S2CResponse.CREATE_NEW_DIRECTORY_OK);
        } else
            return new SendingMessage(S2CResponse.CREATE_NEW_DIRECTORY_REPEATED);
    }

    public Runnable getFinalCallback() {
        return finalCallback;
    }
}
