package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.client.view.ConnectWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.client.view.ProgressWindow;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressReader;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.PasteOperationType;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Receiver;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Connects different sections of the client
 */
public class Client {
    private Receiver receiver;
    private CRequestFactory requestFactory;
    private MainWindowController mainWindowController;
    private CFileStorage fileStorage;

    /**
     * Lunches client
     *
     * @param args user passed parameter
     */
    public static void main(String[] args) {
        new Client();
    }

    /**
     * creates a client object
     */
    public Client() {
        fileStorage = new CFileStorage();
        receiver = new Receiver(new CRouter(this));
        new ConnectWindowController(this);
    }

    /**
     * get file nodes distribution from server
     *
     * @param file     the file
     * @param callback response callback
     */
    public void getNodeDist(FSFile file, ResponseCallback callback) {
        CRequest request = requestFactory.create(C2SRequest.FILE_DIST);
        request.addParameter("file", (file));
        request.setResponseCallback(callback);
        request.send();
    }

    /**
     * open client main window
     */
    public void openMainWindow() {
        mainWindowController = new MainWindowController(this);
        fetchDirectory(FSDirectory.ROOT);
    }

    /**
     * connects to server
     *
     * @param serverAddress server address
     * @param serverPort    server port number
     * @param username      client username
     * @param password      client password
     * @param listenPort    client receive port
     * @return
     */
    public boolean connectToServer(String serverAddress, int serverPort, String username, String password, int listenPort) {
        try {
            requestFactory = new CRequestFactory(serverAddress, serverPort);
            CRequest request = requestFactory.create(C2SRequest.LOGIN);
            request.addParameter("username", username);
            request.addParameter("password", password);
            request.addParameter("listenPort", listenPort);
            //free space in GB
            int space = (int) (new File("data/").getUsableSpace() / 1024 / 1024);
            request.addParameter("space", space);

            AtomicBoolean connected = new AtomicBoolean(false);
            request.setResponseCallback(response -> {
                try {
                    if (response.getTitle().equals(ResponseSubject.OK)) {
                        String token = (String) response.getParameter("token");
                        receiver.start(listenPort);
                        fileStorage.setPort(listenPort);
                        requestFactory.setToken(token);
                        connected.set(true);
                    } else if (response.getTitle().equals(ResponseSubject.FORBIDDEN))
                        connected.set(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            request.send(serverAddress, serverPort).join();

            return connected.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * gets directory info
     *
     * @param directory the directory
     */
    public void fetchDirectory(FSDirectory directory) {
        CRequest request = requestFactory.create(C2SRequest.FETCH_DIRECTORY);
        request.addParameter("directory", directory);
        request.setResponseCallback(response -> {
            Set<FSPath> list = (Set<FSPath>) response.getParameter("list");
            mainWindowController.showPathList(directory, list);
        });
        request.send();
    }

    /**
     * apply cut and copy operations
     *
     * @param path           the path
     * @param directory      to new directory
     * @param pasteOperation operations type
     */
    public void paste(FSPath path, FSDirectory directory, PasteOperationType pasteOperation) {
        CRequest request = requestFactory.create(C2SRequest.PASTE);
        request.addParameter("path", path);
        request.addParameter("directory", directory);
        request.addParameter("operation", pasteOperation);
        request.setResponseCallback(response -> {
            switch ((ResponseSubject) response.getTitle()) {
                case REPEATED:
                    mainWindowController.showPathRepeatedError(directory.getAbsolutePath() + path.getName());
                    break;
                case SELF_PASTE:
                    mainWindowController.showError("Can not paste an item inside or onto the item.");
                    break;
            }
        });
        request.send();
    }

    /**
     * request to delete a path from server
     *
     * @param path the path
     */
    public void delete(FSPath path) {
        CRequest request = requestFactory.create(C2SRequest.DELETE_FILE);
        request.addParameter("path", path);
        request.send();
    }

    /**
     * uploads a file to server
     *
     * @param file             the file
     * @param directory        the directory
     * @param progressCallback the progress callback
     * @param uiCallback       the ui response callback
     */
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

    /**
     * downloads a file to client
     *
     * @param file               the file info form file system
     * @param downloadFile       the file address
     * @param progressWindow     progress window
     * @param uiResponseCallback ui response callback
     */
    public void download(FSFile file, File downloadFile, ProgressWindow progressWindow, ResponseCallback uiResponseCallback) {
        CRequest request = requestFactory.create(C2SRequest.Preview);
        request.addParameter("file", file);
        ResponseCallback responseCallback = response -> {
            try {
                uiResponseCallback.call(response);
                FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
                IOUtil.writeI2O(
                        fileOutputStream,
                        response.getInputStream("file"),
                        response.getStreamSize("file"),
                        progressWindow != null ? progressWindow.getCallback() : null
                );
                fileOutputStream.close();
                if (progressWindow != null)
                    mainWindowController.closeProgressWindow(progressWindow);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    /**
     * download file to preview it
     *
     * @param file               the file info
     * @param downloadFile       the file address
     * @param uiResponseCallback ui response callback
     */
    public void preview(FSFile file, File downloadFile, ResponseCallback uiResponseCallback) {
        CRequest request = requestFactory.create(C2SRequest.Preview);
        request.addParameter("file", file);
        ResponseCallback responseCallback = response -> {
            try {
                new ProgressReader(response.getInputStream("status"), doneDelta -> {}).start().join();
                FileOutputStream fileOutputStream = new FileOutputStream(downloadFile);
                IOUtil.writeI2O(
                        fileOutputStream,
                        response.getInputStream("file"),
                        response.getStreamSize("file")
                );
                fileOutputStream.close();
                uiResponseCallback.call(response);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    /**
     * gets and stores a part to client
     *
     * @param request the server request
     * @return responses Ok if hashes be same else FAILED
     */
    SendingMessage fetchPart(ReceivingMessage request) {
        try {
            Map<Long, String> hashList = (Map<Long, String>) request.getParameter("hashList");
            for (String keyStr : request.getStreamSize().keySet()) {
                Long key = Long.valueOf(keyStr);
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                DigestInputStream digestInputStream =
                        new DigestInputStream(request.getInputStream(keyStr), messageDigest);
                IOUtil.writeI2O(
                        new FileOutputStream(fileStorage.getFileById(key)),
                        digestInputStream, request.getStreamSize(keyStr)
                );
                String hash = IOUtil.printHexBinary(messageDigest.digest());
                if (!hash.equals(hashList.get(key)))
                    return new SendingMessage(ResponseSubject.FAILED);
            }
            return new SendingMessage(ResponseSubject.OK);
        } catch (FileNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * request to search
     *
     * @param directory the directory
     */
    public void search(FSDirectory directory) {
        //        todo:implement
    }

    /**
     * handles server refresh notify
     *
     * @param request server request
     * @return OK
     */
    SendingMessage refreshDirectory(ReceivingMessage request) {
        fetchDirectory(mainWindowController.getCurDir());
        return new SendingMessage(ResponseSubject.OK);
    }

    /**
     * creates a new folder in the parent
     *
     * @param parent the parent directory
     * @param name   the directory name
     */
    public void createNewFolder(FSDirectory parent, String name) {
        CRequest request = requestFactory.create(C2SRequest.CREATE_NEW_DIRECTORY);
        request.addParameter("parent", parent);
        request.addParameter("name", name);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(ResponseSubject.REPEATED)) {
                mainWindowController.showPathRepeatedError(parent.getAbsolutePath() + name);
                mainWindowController.createNewFolder();
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    /**
     * rename a path
     *
     * @param path    the path
     * @param newName new name
     */
    public void rename(FSPath path, String newName) {
        CRequest request = requestFactory.create(C2SRequest.RENAME_FILE);
        request.addParameter("path", path);
        request.addParameter("newName", newName);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(ResponseSubject.REPEATED)) {
                String newPath = path.getParent().getAbsolutePath() + newName;
                if (path instanceof FSDirectory)
                    newPath += FSPath.SEPARATOR;
                mainWindowController.showPathRepeatedError(newPath);
                mainWindowController.renamePath(path);
            }
        };
        request.setResponseCallback(responseCallback);
        request.send();
    }

    /**
     * handles server get part request
     *
     * @param request server request
     * @return Ok if success
     */
    SendingMessage sendPart(ReceivingMessage request) {
        try {
            SendingMessage response = new SendingMessage(ResponseSubject.OK);
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

    /**
     * handles server delete parts request
     *
     * @param request server request
     * @return OK
     */
    SendingMessage deleteParts(ReceivingMessage request) {
        Set<Long> parts = (Set<Long>) request.getParameter("parts");
        for (Long partId : parts)
            fileStorage.getFileById(partId).delete();
        return new SendingMessage(ResponseSubject.OK);
    }
}
