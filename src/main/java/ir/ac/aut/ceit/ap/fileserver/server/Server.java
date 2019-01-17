package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.filesys.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSFile;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.ConnectionManager;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.Subject;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;


public class Server {
    private FileSystem fileSystem;
    private ConnectionManager connectionManager;
    private SecurityManager securityManager;

    public Server()  {
        connectionManager = new ConnectionManager(5252,new ServerRouter(this));
        fileSystem = new FileSystem();
        securityManager = new SecurityManager();

        FSDirectory directory = fileSystem.addDirectory(FileSystem.ROOT, "dawd");
        for (int i = 0; i < 100; i++)
            fileSystem.addFile(directory, "dawd.dwad");
    }

    Message registerUser(Message request) {
        return securityManager.registerUser(request);
    }

    Message fetchDirectory(Message request) {
        FSDirectory directory =(FSDirectory) request.getObject("directory");
        Message response = new Message(Subject.FETCH_DIRECTORY_OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    Message upload(Message request)  {
        byte[] file = request.getBytes("file");
        return new Message(Subject.UPLOAD_FILE_OK);
    }

    public Message authUser(Message request) {
        return securityManager.registerUser(request);
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
