package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;

import java.io.InputStream;
import java.util.ArrayList;
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

    Message loginUser(Message request, String address) {
        Message response = securityManager.loginUser(request);
        if (response.getTitle().equals(Subject.LOGIN_OK)) {
            List<String> partList = (List<String>) request.getObject("partList");
            int listenPort = (int) request.getObject("listenPort");
            clientList.add(new ClientInfo(partList, address, listenPort));
        }
        return response;
    }

    Message fetchDirectory(Message request) {
        FSDirectory directory =(FSDirectory) request.getObject("directory");
        Message response = new SendingMessage(Subject.FETCH_DIRECTORY_OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    Message upload(ReceivingMessage request) {
        InputStream inputStream = request.getStream("file");
        int fileSize = (int) request.getObject("fileSize");
        partManager.splitFile(fileSize);
        return new SendingMessage(Subject.UPLOAD_FILE_OK);
    }

    public Message authUser(Message request) {
        return securityManager.loginUser(request);
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
