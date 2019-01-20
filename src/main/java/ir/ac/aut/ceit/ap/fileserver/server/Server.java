package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.*;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SendingMessage loginUser(ReceivingMessage request) {
        int listenPort = (int) request.getParameter("listenPort");
        String username = (String) request.getParameter("username");
        ClientInfo client = new ClientInfo(request.getSenderAddress(), listenPort, username);
        SendingMessage response = securityManager.loginUser(request, client);
        if (response.getTitle().equals(Subject.LOGIN_OK))
            clientManager.addClient(client);
        return response;
    }

    SendingMessage fetchDirectory(Message request) {
        FSDirectory directory = (FSDirectory) request.getParameter("directory");
        SendingMessage response = new SendingMessage(Subject.FETCH_DIRECTORY_OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    SendingMessage upload(ReceivingMessage request) {
        try {
            File downloadingFile = File.createTempFile("server", "dl");
            Long fileSize = request.getStreamSize("file");
            IOUtil.writeI2O(
                    new FileOutputStream(downloadingFile),
                    request.getInputStream("file"), fileSize
            );
            SendingMessage response = new SendingMessage(Subject.UPLOAD_FILE_OK);

            PipedInputStream pipedInputStream = new PipedInputStream();
            PrintWriter out = new PrintWriter(new PipedOutputStream(pipedInputStream));
            response.addInputStream("status", pipedInputStream, Long.MAX_VALUE);

            String fileName = (String) request.getParameter("fileName");
            FSDirectory directory = (FSDirectory) request.getParameter("directory");

            new Thread(() -> {
                sendParts(downloadingFile, directory, fileName, out);
            }).start();
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

            SendingMessage response = new SendingMessage(Subject.DOWNLOAD_FILE_OK);
            PipedInputStream fileInputStream = new PipedInputStream();
            response.addInputStream("file", fileInputStream, fileSize);
            PipedOutputStream fileOutputStream = new PipedOutputStream(fileInputStream);

            new Thread(() -> {
                for (Map.Entry<Long, ClientInfo> entry : partMap.entrySet()) {
                    Long partId = entry.getKey();
                    ClientInfo client = entry.getValue();

                    SRequest partRequest = new SRequest(Subject.SEND_PART);
                    partRequest.addParameter("partId", partId);
                    ResponseCallback responseCallback = partResponse -> {
                        IOUtil.writeI2O(
                                fileOutputStream,
                                partResponse.getInputStream("part"),
                                partResponse.getStreamSize("part")
                        );
                    };
                    partRequest.setResponseCallback(responseCallback);
                    try {
                        partRequest.send(client).join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendParts(File downloadingFile, FSDirectory directory, String fileName, PrintWriter out) {
        try {
            ProgressCallback callback = doneDelta -> {
                if (doneDelta == -1) {
                    out.println(StreamsCommand.PROGRESS_END.name());
                    out.close();
                    return;
                }
                out.println(StreamsCommand.PROGRESS_PASSED.name());
                out.println(doneDelta);
                out.flush();
            };

            List<Long> parts = fileStorage.splitFile(downloadingFile);
            Long fileSize = downloadingFile.length();
            downloadingFile.delete();
            Map<ClientInfo, Set<Long>> destinations =
                    clientManager.getDestinations(parts);

            for (ClientInfo client : destinations.keySet()) {
                Set<Long> clientParts = destinations.get(client);

                SRequest partRequest = new SRequest(Subject.RECEIVE_PART);
                Map<Long, String> hashList = new HashMap<>();
                for (Long partId : clientParts) {
                    File partFile = fileStorage.getFileById(partId);
                    String hash = DigestUtils.sha256Hex(new FileInputStream(partFile));
                    hashList.put(partId, hash);
                    partRequest.addInputStream(partId.toString(), new FileInputStream(partFile), partFile.length());
                    partRequest.addProgressCallback(partId.toString(), callback);
                }
                partRequest.addParameter("hashList", hashList);
                partRequest.send(client).join();
            }

            callback.call(-1);

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
            SRequest partRequest = new SRequest(Subject.REFRESH_DIRECTORY);
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
            return new SendingMessage(Subject.MOVE_PATH_OK);
        } else
            return new SendingMessage(Subject.MOVE_PATH_ALREADY_EXISTS);
    }

    SendingMessage createNewDirectory(ReceivingMessage request) {
        FSDirectory parent = (FSDirectory) request.getParameter("parent");
        String name = (String) request.getParameter("name");
        FSDirectory newDirectory = fileSystem.addDirectory(parent, name);
        if (newDirectory != null) {
            refreshClients();
            return new SendingMessage(Subject.CREATE_NEW_DIRECTORY_OK);
        } else
            return new SendingMessage(Subject.CREATE_NEW_DIRECTORY_REPEATED);
    }

    public Runnable getFinalCallback() {
        return finalCallback;
    }
}
