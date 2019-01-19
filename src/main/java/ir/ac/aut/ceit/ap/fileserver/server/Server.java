package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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

        FSDirectory directory = new FSDirectory(FSDirectory.ROOT, "dawd");
        fileSystem.addPath(directory);
        for (int i = 0; i < 100; i++) {
            fileSystem.addPath(new FSFile(directory, "dawd.dwad", new ArrayList<>()));
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
            File downloadingFile = fileStorage.getNewFile();
            IOUtil.writeI2O(
                    new FileOutputStream(downloadingFile),
                    request.getStream("file"),
                    request.getStreamSize("file")
            );

            List<Long> parts = fileStorage.splitFile(downloadingFile);
            downloadingFile.delete();

            Map<ClientInfo, Set<Long>> destinations =
                    clientManager.getDestinations(parts, fileStorage.getRepeat());

            sendParts(destinations);

            clientManager.updateParts(destinations);

            for (Long part : parts)
                fileStorage.getFileById(part).delete();

            String fileName = (String) request.getParameter("fileName");
            FSDirectory directory = (FSDirectory) request.getParameter("directory");
            fileSystem.addPath(new FSFile(directory, fileName, parts));

            refreshClients();

            return new SendingMessage(Subject.UPLOAD_FILE_OK);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void sendParts(Map<ClientInfo, Set<Long>> destinations) throws IOException, InterruptedException {
        for (ClientInfo client : destinations.keySet()) {
            Set<Long> clientParts = destinations.get(client);

            SendingMessage partRequest = new SendingMessage(Subject.FETCH_PART);
            Map<Long, String> hashList = new HashMap<>();
            for (Long partId : clientParts) {
                File partFile = fileStorage.getFileById(partId);
                String hash = DigestUtils.sha256Hex(new FileInputStream(partFile));
                hashList.put(partId, hash);
                partRequest.addStream(partId.toString(), new FileInputStream(partFile), partFile.length());
            }
            partRequest.addParameter("hashList", hashList);
            connectionManager.sendRequest(partRequest, client.getAddress(), client.getListenPort()).join();
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
        FSPath newPath = (FSPath) request.getParameter("newPath");
        if (fileSystem.pathIsNew(newPath)) {
            fileSystem.removePath(path);
            fileSystem.addPath(newPath);
            refreshClients();
            return new SendingMessage(Subject.MOVE_PATH_OK);
        } else
            return new SendingMessage(Subject.MOVE_PATH_ALREADY_EXISTS);
    }

    SendingMessage createNewDirectory(ReceivingMessage request) {
        FSDirectory parent = (FSDirectory) request.getParameter("parent");
        String name = (String) request.getParameter("name");
        FSDirectory newDirectory = new FSDirectory(parent, name);
        if (fileSystem.pathIsNew(newDirectory)) {
            fileSystem.addPath(newDirectory);
            refreshClients();
            return new SendingMessage(Subject.CREATE_NEW_DIRECTORY_OK);
        } else
            return new SendingMessage(Subject.CREATE_NEW_DIRECTORY_REPEATED);
    }
}
