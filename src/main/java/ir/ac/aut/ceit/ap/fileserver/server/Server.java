package ir.ac.aut.ceit.ap.fileserver.server;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FilePartInfo;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


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
        partManager = new FilePartManager(1024 * 1024,clientList);
        securityManager = new SecurityManager();

        FSDirectory directory = fileSystem.addDirectory(FSDirectory.ROOT, "dawd");
        for (int i = 0; i < 100; i++)
            fileSystem.addFile(directory, "dawd.dwad");
    }

    SendingMessage loginUser(Message request, String address) {
        List<String> partList = (List<String>) request.getParameter("partList");
        int listenPort = (int) request.getParameter("listenPort");
        String username = (String) request.getParameter("username");
        ClientInfo client = new ClientInfo(partList, address, listenPort, username);
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

    SendingMessage upload(ReceivingMessage request) throws IOException, NoSuchAlgorithmException, InterruptedException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(request.getStream("file"));
        long fileSize = request.getStreamSize("file");
        List<ClientInfo> destinations = partManager.partsDestinations(fileSize);
        Iterator<ClientInfo> iterator = destinations.iterator();
        MessageDigest digest = MessageDigest.getInstance("MD5");
        while (iterator.hasNext()) {
            ClientInfo client = iterator.next();

            byte[] buffer = new byte[partManager.splitSize];
            int numBytes = bufferedInputStream.read(buffer);

            digest.update(buffer, 0, numBytes);
            String hash = DatatypeConverter.printHexBinary(digest.digest()).toUpperCase();
            FilePartInfo partInfo = new FilePartInfo(hash);

            ByteInputStream partInputStream = new ByteInputStream(buffer, numBytes);

            SendingMessage partRequest = new SendingMessage(Subject.FETCH_PART);
            partRequest.addParameter("partInfo", partInfo);
            partRequest.addStream("part", partInputStream, (long) partManager.splitSize);
            connectionManager.sendRequest(partRequest, client.getAddress(), client.getListenPort()).join();
            iterator.remove();
        }
        return new SendingMessage(Subject.UPLOAD_FILE_OK);
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
