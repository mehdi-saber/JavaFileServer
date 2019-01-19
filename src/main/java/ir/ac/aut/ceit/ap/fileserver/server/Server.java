package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.*;


public class Server {
    private FileSystem fileSystem;
    private ConnectionManager connectionManager;
    private SecurityManager securityManager;
    private ServerFileStorage fileStorage;
    private ClientManager clientManager;
    private int port = 5050;

    public Server()  {
        connectionManager = new ConnectionManager(port, new ServerRouter(this));
        fileSystem = new FileSystem();
        fileStorage = new ServerFileStorage(0L);
        clientManager = new ClientManager();
        securityManager = new SecurityManager();

        FSDirectory directory = fileSystem.addDirectory(FSDirectory.ROOT, "dawd");
        for (int i = 0; i < 100; i++)
            fileSystem.addFile(directory, "dawd.dwad", new ArrayList<>());
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
            File downloadingFile = File.createTempFile("server", "dl");
            FSFile file = (FSFile) request.getParameter("file");
            Long fileSize = file.getSize();
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
            downloadingFile.delete();
            Map<ClientInfo, Set<Long>> destinations =
                    clientManager.getDestinations(parts, fileStorage.getRepeat());

            for (ClientInfo client : destinations.keySet()) {
                Set<Long> clientParts = destinations.get(client);

                SendingMessage partRequest = new SendingMessage(Subject.FETCH_PART);
                Map<Long, String> hashList = new HashMap<>();
                for (Long partId : clientParts) {
                    File partFile = fileStorage.getFileById(partId);
                    String hash = DigestUtils.sha256Hex(new FileInputStream(partFile));
                    hashList.put(partId, hash);
                    partRequest.addInputStream(partId.toString(), new FileInputStream(partFile), partFile.length());
                    partRequest.addProgressCallback(partId.toString(), callback);
                }
                partRequest.addParameter("hashList", hashList);
                connectionManager.sendRequest(partRequest, client.getAddress(), client.getListenPort()).join();
            }

            callback.call(-1);

            clientManager.updateParts(destinations);

            for (Long part : parts)
                fileStorage.getFileById(part).delete();
            fileSystem.addFile(directory, fileName, parts);

            refreshClients();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void refreshClients() {
        for (ClientInfo client : clientManager.getClientList()) {
            SendingMessage partRequest = new SendingMessage(Subject.REFRESH_DIRECTORY);
            connectionManager.sendRequest(partRequest, client.getAddress(), client.getListenPort());
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
}
