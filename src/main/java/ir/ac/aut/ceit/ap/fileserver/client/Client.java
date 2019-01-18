package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.ConnectWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FilePartInfo;
import ir.ac.aut.ceit.ap.fileserver.network.*;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private ClientConnectionManager connectionManager;
    private MainWindowController mainWindowController;
    private FilePartManager partManager;
    private int listenPort;

    public Client()  {
        partManager = new FilePartManager();
        new ConnectWindowController(this);
    }

    public void openMainWindow() {
        mainWindowController = new MainWindowController(this);
        fetchDirectory(FSDirectory.ROOT);
    }

    public boolean connectToServer(String serverAddress, int serverPort, String username, String password) {
        this.listenPort = new Random().nextInt(10000);
        connectionManager = new ClientConnectionManager(
                listenPort,
                new ClientRouter(this),
                serverAddress, serverPort);
        SendingMessage request = new SendingMessage(Subject.LOGIN);
        request.addParameter("username", username);
        request.addParameter("password", password);
        request.addParameter("listenPort", listenPort);
        request.addParameter("partList", partManager.listPartHashes());

        AtomicBoolean connected = new AtomicBoolean(false);
        request.setResponseCallback(response -> {
            if (response.getTitle().equals(Subject.LOGIN_OK)) {
                String token = (String) response.getParameter("token");
                connectionManager.setToken(token);
                connected.set(true);
            } else if (response.getTitle().equals(Subject.LOGIN_FAILED)) {
                connected.set(false);
            }
        });
        try {
            connectionManager.sendRequest(request).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return connected.get();
    }

    public void fetchDirectory(FSDirectory directory) {
        SendingMessage request = new SendingMessage(Subject.FETCH_DIRECTORY);
        request.addParameter("directory", directory);
        request.setResponseCallback(new ResponseCallback() {
            @Override
            public void call(ReceivingMessage response) {
                List<FSPath> list = (List<FSPath>) response.getParameter("list");
                changeDirectory(directory, list);
            }
        });
        connectionManager.sendRequest(request);
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
            SendingMessage request = new SendingMessage(Subject.UPLOAD_FILE);
            FileInputStream fileInputStream = new FileInputStream(file);
            request.addStream("file", fileInputStream);
            request.addParameter("fileSize", file.length());
            connectionManager.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendingMessage fetchFile(ReceivingMessage request) {
        FilePartInfo partInfo = (FilePartInfo) request.getParameter("partInfo");
        OutputStream outputStream = partManager.storePartOutputStream(partInfo);
        InputStream inputStream = request.getStream("part");
        try {
            int b;
            while ((b = inputStream.read()) != -1)
                outputStream.write(b);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SendingMessage(Subject.FETCH_PART_OK);
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
