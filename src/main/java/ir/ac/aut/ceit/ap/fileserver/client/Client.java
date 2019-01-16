package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSFile;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSPath;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeTitle;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Client {
    private ConnectionManager connectionManager;
    private MainWindowController mainWindowController;
    public Client()  {
        mainWindowController = new MainWindowController(this);
        connectionManager = new ConnectionManager("localhost", 5000);

        fetchDirectory(FileSystem.ROOT);
    }

    public void login(String username, String password) throws IOException {
        ExchangeData request = new ExchangeData(ExchangeTitle.LOGIN_USER);
        request.addParameter("username", username);
        request.addParameter("password", password);
        ExchangeData response = connectionManager.request(request);
    }

    public void register(String username, String password) throws IOException {
        ExchangeData request = new ExchangeData(ExchangeTitle.REGISTER_USER);
        request.addParameter("username", username);
        request.addParameter("password", password);
        ExchangeData response = connectionManager.request(request);
        System.out.println(response.getObject("token"));
    }

    public void fetchDirectory(FSDirectory directory) {
        ExchangeData request = new ExchangeData(ExchangeTitle.FETCH_DIRECTORY);
        request.addParameter("directory", directory);
        ExchangeData response = connectionManager.request(request);
        List<FSPath> list = (List<FSPath>) response.getObject("list");
        changeDirectory(directory, list);
    }

    public void download(FSFile file) {
//        todo:implement
    }

    public void copy(FSPath path) {
//        todo:implement
    }

    public void cut(FSPath path) {
        //        todo:implement
    }

    public void rename(FSPath path, String newName) {
        //        todo:implement
    }

    public void delete(FSPath path) {
        //        todo:implement
    }

    public void upload(File file) {
        //        todo:implement
    }

    public void paste(FSDirectory directory) {
        //        todo:implement
    }

    public void search(FSDirectory directory) {
        //        todo:implement
    }

    public void changeDirectory(FSDirectory directory, List<FSPath> pathList) {
        mainWindowController.showFileList(directory, pathList);
    }
}
