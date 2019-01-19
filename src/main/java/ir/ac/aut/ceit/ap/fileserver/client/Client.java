package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.ConnectWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.*;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private ClientConnectionManager connectionManager;
    private MainWindowController mainWindowController;
    private ClientFileStorage fileStorage;
    private int listenPort;

    public Client()  {
        this.listenPort = new Random().nextInt(10000);
        fileStorage = new ClientFileStorage(listenPort);
        new ConnectWindowController(this);
    }

    public void openMainWindow() {
        mainWindowController = new MainWindowController(this);
        fetchDirectory(FSDirectory.ROOT);
//        mainWindowController.upload(new File("/Users/mehdi-saber/Desktop/1.mp4"), FSDirectory.ROOT);//todo:remove
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
            Set<FSPath> list = (Set<FSPath>) response.getParameter("list");
            changeDirectory(directory, list);
        });
        connectionManager.sendRequest(request);
    }


    public void copy(FSPath path) {
//        todo:implement
    }

    public void cut(FSPath path) {
        //        todo:implement
    }


    public void delete(FSPath path) {
        //        todo:implement
    }

    public void upload(File file, FSDirectory directory, ProgressCallback progressCallback, ResponseCallback responseCallback) {
        try {
            SendingMessage request = new SendingMessage(Subject.UPLOAD_FILE);
            request.addInputStream("file", new FileInputStream(file), file.length());
            request.addProgressCallback("file", progressCallback);
            request.addParameter("fileName",file.getName());
            request.addParameter("directory", directory);
            request.setResponseCallback(responseCallback);
            connectionManager.sendRequest(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(FSFile file, ProgressCallback progressCallback, ResponseCallback uiResponseCallback) {
            SendingMessage request = new SendingMessage(Subject.DOWNLOAD_FILE);
            request.addParameter("file", file);
            ResponseCallback responseCallback = response -> {
                uiResponseCallback.call(response);
            };
            request.setResponseCallback(responseCallback);
            connectionManager.sendRequest(request);
    }

    public SendingMessage fetchPart(ReceivingMessage request) {
        try {
            for (String key : request.getStreamSize().keySet())
                IOUtil.writeI2O(
                        new FileOutputStream(fileStorage.getFileById(Long.valueOf(key))),
                        request.getInputStream(key), request.getStreamSize(key)
                );
            return new SendingMessage(Subject.FETCH_PART_OK);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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

    public void changeDirectory(FSDirectory directory, Set<FSPath> pathList) {
        mainWindowController.showPathList(directory, pathList);
    }

    public SendingMessage refreshDirectory(ReceivingMessage request) {
        fetchDirectory(mainWindowController.getCurDir());
        return new SendingMessage(Subject.REFRESH_DIRECTORY_OK);
    }

    public void createNewFolder(FSDirectory parent, String name) {
        SendingMessage request = new SendingMessage(Subject.CREATE_NEW_DIRECTORY);
        request.addParameter("parent", parent);
        request.addParameter("name", name);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(Subject.CREATE_NEW_DIRECTORY_REPEATED)) {
                mainWindowController.showError("directory \"" + parent.getAbsolutePath() + name + "\"already exists.");
                mainWindowController.createNewFolder();
            }
        };
        request.setResponseCallback(responseCallback);
        connectionManager.sendRequest(request);
    }

    public void rename(FSPath path, String newName) {
        SendingMessage request = new SendingMessage(Subject.MOVE_PATH);
        request.addParameter("path", path);
        request.addParameter("newName", newName);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(Subject.MOVE_PATH_ALREADY_EXISTS)) {
                String newPath = path.getAbsolutePath() + newName;
                if (path instanceof FSDirectory)
                    newPath += FSPath.SEPARATOR;
                mainWindowController.showError("path \"" + newPath + "\"already exists.");
                mainWindowController.renamePath(path);
            }
        };
        request.setResponseCallback(responseCallback);
        connectionManager.sendRequest(request);
    }
}
