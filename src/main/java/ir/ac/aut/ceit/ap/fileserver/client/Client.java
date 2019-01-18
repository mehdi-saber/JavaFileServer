package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.ConnectWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

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
        this.listenPort = new Random().nextInt(10000);
        partManager = new FilePartManager(this.listenPort);
        new ConnectWindowController(this);
    }

    public void openMainWindow() {
        mainWindowController = new MainWindowController(this);
        fetchDirectory(FSDirectory.ROOT);
    }

    public boolean connectToServer(String serverAddress, int serverPort, String username, String password) {
        connectionManager = new ClientConnectionManager(
                listenPort,
                new ClientRouter(this),
                serverAddress, serverPort);
        SendingMessage request = new SendingMessage(Subject.LOGIN);
        request.addParameter("username", username);
        request.addParameter("password", password);
        request.addParameter("listenPort", listenPort);

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
        request.setResponseCallback(response -> {
            List<FSPath> list = (List<FSPath>) response.getParameter("list");
            changeDirectory(directory, list);
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

    public void upload(File file, FSDirectory directory, long fileSize,
                       ProgressCallback callback, ResponseCallback responseCallback) {
        try {
            SendingMessage request = new SendingMessage(Subject.UPLOAD_FILE);
            request.addStream("file", new FileInputStream(file), fileSize);
            request.addProgressCallback("file", callback);
            request.addParameter("fileName",file.getName());
            request.addParameter("directory", directory);
            request.setResponseCallback(responseCallback);
            connectionManager.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SendingMessage fetchPart(ReceivingMessage request) {
        for (String key : request.getStreamSize().keySet()) {
            long partId = Integer.valueOf(key.split("-")[1]);
            try {
                IOUtil.writeI2O(
                        new FileOutputStream(partManager.getFileAddress(partId)),
                        request.getStream(key), request.getStreamSize(key)
                );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
