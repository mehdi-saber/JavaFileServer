package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.ConnectWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.ProgressWindow;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SResponse;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CResponse;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Receiver;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Client {
    private Receiver receiver;
    private CRequestFactory requestFactory;
    private MainWindowController mainWindowController;
    private CFileStorage fileStorage;
    private Runnable finalCallback;
    private int listenPort;

    public Client(Runnable finalCallback, int listenPort)  {
        this.finalCallback=finalCallback;
        this.listenPort = listenPort;
        fileStorage = new CFileStorage(this.listenPort);
        new ConnectWindowController(this);
    }

    public void openMainWindow() {
        mainWindowController = new MainWindowController(this,finalCallback);
        fetchDirectory(FSDirectory.ROOT);
//        mainWindowController.upload(new File("/Users/mehdi-saber/Desktop/1.mp4"), FSDirectory.ROOT);//todo:remove
    }
    public boolean connectToServer(String serverAddress, int serverPort, String username, String password) {
        requestFactory = new CRequestFactory(serverAddress, serverPort);
        CRequest request = requestFactory.create(C2SRequest.LOGIN);
        request.addParameter("username", username);
        request.addParameter("password", password);
        request.addParameter("listenPort", listenPort);

        AtomicBoolean connected = new AtomicBoolean(false);
        request.setResponseCallback(response -> {
            try {
                if (response.getTitle().equals(S2CResponse.LOGIN_OK)) {
                    String token = (String) response.getParameter("token");
                    requestFactory.setToken(token);
                    receiver = new Receiver(listenPort, new CRouter(this));
                    connected.set(true);
                } else if (response.getTitle().equals(S2CResponse.LOGIN_FAILED))
                    connected.set(false);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            request.send(serverAddress, serverPort).join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return connected.get();
    }

    public void fetchDirectory(FSDirectory directory) {
        CRequest request = requestFactory.create(C2SRequest.FETCH_DIRECTORY);
        request.addParameter("directory", directory);
        request.setResponseCallback(response -> {
            Set<FSPath> list = (Set<FSPath>) response.getParameter("list");
            mainWindowController.showPathList(directory, list);
        });
        request.send();
    }


    public void copy(FSPath path, FSDirectory directory) {
//        CRequest request=requestFactory.create(Subject.)
    }

    public void cut(FSPath path, FSDirectory directory) {
        //        todo:implement
    }


    public void delete(FSPath path) {
        //        todo:implement
    }

    public void upload(File file, FSDirectory directory, ProgressCallback progressCallback, ResponseCallback uiCallback) {
        try {
            CRequest request = requestFactory.create(C2SRequest.UPLOAD_FILE);
            request.addInputStream("file", new FileInputStream(file), file.length());
            request.addProgressCallback("file", progressCallback);
            request.addParameter("fileName",file.getName());
            request.addParameter("directory", directory);
            request.setResponseCallback(uiCallback);
            request.send();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void download(FSFile file, File downloadFile, ProgressWindow progressWindow, ResponseCallback uiResponseCallback) {
        CRequest request = requestFactory.create(C2SRequest.DOWNLOAD_FILE);
        request.addParameter("file", file);
        ResponseCallback responseCallback = response -> {
            try {
                uiResponseCallback.call(response);
                IOUtil.writeI2O(
                        new FileOutputStream(downloadFile),
                        response.getInputStream("file"),
                        response.getStreamSize("file"),
                        progressWindow.getCallback()
                );
                mainWindowController.closeProgressWindow(progressWindow);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    SendingMessage fetchPart(ReceivingMessage request) {
        try {
            for (String key : request.getStreamSize().keySet())
                IOUtil.writeI2O(
                        new FileOutputStream(fileStorage.getFileById(Long.valueOf(key))),
                        request.getInputStream(key), request.getStreamSize(key)
                );
            return new SendingMessage(C2SResponse.RECEIVE_PART_OK);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    Message getFilePart(Message request) {
        return null;
    }

    public void search(FSDirectory directory) {
        //        todo:implement
    }

    SendingMessage refreshDirectory(ReceivingMessage request) {
        fetchDirectory(mainWindowController.getCurDir());
        return new SendingMessage(C2SResponse.REFRESH_DIRECTORY_OK);
    }

    public void createNewFolder(FSDirectory parent, String name) {
        CRequest request = requestFactory.create(C2SRequest.CREATE_NEW_DIRECTORY);
        request.addParameter("parent", parent);
        request.addParameter("name", name);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(S2CResponse.CREATE_NEW_DIRECTORY_REPEATED)) {
                mainWindowController.showPathRepeatedError(parent.getAbsolutePath() + name);
                mainWindowController.createNewFolder();
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    public void rename(FSPath path, String newName) {
        CRequest request = requestFactory.create(C2SRequest.MOVE_PATH);
        request.addParameter("path", path);
        request.addParameter("newName", newName);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(S2CResponse.MOVE_PATH_ALREADY_EXISTS)) {
                String newPath = path.getAbsolutePath() + newName;
                if (path instanceof FSDirectory)
                    newPath += FSPath.SEPARATOR;
                mainWindowController.showPathRepeatedError(newPath);
                mainWindowController.renamePath(path);
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    SendingMessage sendPart(ReceivingMessage request) {
        try {
            SendingMessage response = new SendingMessage(C2SResponse.SEND_PART_OK);
            Long partId = (Long) request.getParameter("partId");
            File file = fileStorage.getFileById(partId);
            FileInputStream fileInputStream = new FileInputStream(file);
            response.addInputStream("part", fileInputStream, file.length());
            return response;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
