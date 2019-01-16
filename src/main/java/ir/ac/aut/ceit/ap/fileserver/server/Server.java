package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.filesys.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSFile;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeTitle;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;

import java.util.Map;


public class Server {
    private FileSystem fileSystem;
    private ConnectionManager connectionManager;
    private SecurityManager securityManager;

    public Server()  {

        connectionManager = new ConnectionManager(this, 5000);
        fileSystem = new FileSystem();
        securityManager = new SecurityManager();

        FSDirectory directory = fileSystem.addDirectory(fileSystem.ROOT, "dawd");
        for (int i = 0; i < 100; i++)
            fileSystem.addFile(directory, "dawd.dwad");
    }

    ExchangeData registerUser(ExchangeData request) {
        return securityManager.registerUser(request);
    }

    ExchangeData fetchDirectory(ExchangeData request) {
        FSDirectory directory =(FSDirectory) request.getObject("directory");
        ExchangeData response = new ExchangeData(ExchangeTitle.FETCH_DIRECTORY_OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    public ExchangeData authUser(ExchangeData request) {
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
