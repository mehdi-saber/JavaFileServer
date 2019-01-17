package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.ConnectWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSFile;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSPath;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileSystem;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.Subject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class Client {
    private ClientConnectionManager connectionManager;
    private MainWindowController mainWindowController;
    private int listenPort;

    public Client()  {
        new ConnectWindowController(this);
    }

    public void openMainWindow() {
        mainWindowController = new MainWindowController(this);
        fetchDirectory(FileSystem.ROOT);
    }

    public boolean connectToServer(String serverAddress, int serverPort, String username, String password) {
        this.listenPort = new Random().nextInt(10000);
        connectionManager = new ClientConnectionManager(
                listenPort,
                new ClientRouter(this),
                serverAddress, serverPort);
        Message request = new Message(Subject.LOGIN);
        request.addParameter("username", username);
        request.addParameter("password", password);
        Message response = connectionManager.sendRequest(request);
        if (response.getTitle().equals(Subject.LOGIN_OK)) {
            connectionManager.token = (String) response.getObject("token");
            return true;
        } else if (response.getTitle().equals(Subject.LOGIN_FAILED))
            return false;
        return false;
    }

    public void fetchDirectory(FSDirectory directory) {
        Message request = new Message(Subject.FETCH_DIRECTORY);
        request.addParameter("directory", directory);
        Message response = connectionManager.sendRequest(request);
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
        if (file == null)
            return;
        try {
            Message request = new Message(Subject.UPLOAD_FILE);
            byte[] bytes = FileUtils.readFileToByteArray(file);
            request.addBytes("file", bytes);
            connectionManager.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Message fetchFile(Message request) {
        return new Message(null);
    }

    Message getFilePart(Message request) {
        return null;
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
