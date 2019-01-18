package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Server {
    private List<ClientInfo> clientList;
    private FileSystem fileSystem;
    private ConnectionManager connectionManager;
    private FilePartManager partManager;
    private SecurityManager securityManager;
    private int port = 5050;

    public Server()  {
        connectionManager = new ConnectionManager(port, new ServerRouter(this));
        fileSystem = new FileSystem();
        clientList = new ArrayList<>();
        partManager = new FilePartManager(clientList, this);
        securityManager = new SecurityManager();

        FSDirectory directory = fileSystem.addDirectory(FSDirectory.ROOT, "dawd");
        for (int i = 0; i < 100; i++)
            fileSystem.addFile(directory, "dawd.dwad");
    }

    SendingMessage loginUser(ReceivingMessage request) {
        List<String> partList = (List<String>) request.getParameter("partList");
        int listenPort = (int) request.getParameter("listenPort");
        String username = (String) request.getParameter("username");
        ClientInfo client = new ClientInfo(partList, request.getSenderAddress(), listenPort, username);
        SendingMessage response = securityManager.loginUser(request, client);
        if (response.getTitle().equals(Subject.LOGIN_OK))
            clientList.add(client);
        return response;
    }

    SendingMessage fetchDirectory(Message request) {
        FSDirectory directory = (FSDirectory) request.getParameter("directory");
        SendingMessage response = new SendingMessage(Subject.FETCH_DIRECTORY_OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    SendingMessage upload(ReceivingMessage request) {
        partManager.getAndSplitFile(request);
        String fileName = (String) request.getParameter("fileName");
        FSDirectory directory=(FSDirectory)request.getParameter("directory");
        fileSystem.addFile(directory,fileName);
        return new SendingMessage(Subject.UPLOAD_FILE_OK);
    }

    void sendParts(Map<ClientInfo, List<Long>> destinations) throws IOException, InterruptedException {
        for (ClientInfo client : destinations.keySet()) {
            List<Long> clientParts = destinations.get(client);

            SendingMessage partRequest = new SendingMessage(Subject.FETCH_PART);
            Map<Long, String> hashList = new HashMap<>();
            for (Long partId : clientParts) {
                File partFile = partManager.parts.getFileById(partId);
                String hash = DigestUtils.sha256Hex(new FileInputStream(partFile));
                hashList.put(partId, hash);
                partRequest.addStream("part-" + partId, new FileInputStream(partFile), partFile.length());
            }
            partRequest.addParameter("hashList", hashList);
            connectionManager.sendRequest(partRequest, client.getAddress(), client.getListenPort()).join();
        }
    }

    public void addFile(FSFile info, byte[] data) {

    }

    public void removeFile(FSFile info) {

    }

    public void fetchFile(FSFile info) {

    }


    public void renameFile(FSFile info, byte[] data) {

    }
}
