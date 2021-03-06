package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressWriter;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.PasteOperationType;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CRequest;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Receiver;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;
import ir.ac.aut.ceit.ap.fileserver.server.security.User;
import ir.ac.aut.ceit.ap.fileserver.server.view.MainWindowController;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Connects different sections of the server
 */
public class Server {
    private SFileSystem fileSystem;
    private Receiver receiver;
    private SecurityManager securityManager;
    private SFileStorage fileStorage;
    private ClientManager clientManager;

    /**
     * Construct sections of server
     */
    public Server() {
        securityManager = new SecurityManager();
        receiver = new Receiver(new SRouter(this, securityManager));
        fileSystem = new SFileSystem();
        fileStorage = new SFileStorage();
        clientManager = new ClientManager();

        Runnable finalCallback = () -> {
            clientManager.save();
            fileSystem.save();
            fileStorage.save();
        };

        new MainWindowController(this, finalCallback);
    }

    /**
     * Lunches server
     *
     * @param args User passed arguments
     */
    public static void main(String[] args) {
        new Server();
    }

    /**
     * Start server
     *
     * @param port       The receiving port
     * @param splitSize  The maximum part size
     * @param redundancy The redundancy count
     */
    public void start(int port, int splitSize, int redundancy) {
        try {
            fileStorage.setSplitSize(splitSize);
            clientManager.setRedundancy(redundancy);
            receiver.start(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops server
     */
    public void stop() {
        receiver.stop();
    }


    /**
     * Handles user's login request
     *
     * @param request user's request
     * @return FORBIDDEN if fail and OK if success
     */
    SendingMessage loginUser(ReceivingMessage request) {
        SendingMessage response = securityManager.loginUser(request);
        if (response.getTitle().equals(ResponseSubject.OK))
            clientManager.addClient((ClientInfo) response.getParameter("client"));
        return response;
    }

    /**
     * Handles user's fetch directory request
     *
     * @param request user's request
     * @return sub-path list in response
     */
    SendingMessage fetchDirectory(Message request) {
        FSDirectory directory = (FSDirectory) request.getParameter("directory");
        SendingMessage response = new SendingMessage(ResponseSubject.OK);
        response.addParameter("list", fileSystem.listSubPaths(directory));
        return response;
    }

    /**
     * Creates a temporary file
     *
     * @return The file
     * @throws IOException Throws if fails
     */
    private File createTempFile() throws IOException {
        return File.createTempFile("server", "dl");
    }

    /**
     * Handles upload requests
     *
     * @param request the request
     * @return REPEATED if the new path exists else OK
     */
    SendingMessage upload(ReceivingMessage request) {
        try {
            String fileName = (String) request.getParameter("fileName");
            FSDirectory directory = (FSDirectory) request.getParameter("directory");

            if (fileSystem.pathExists(directory, fileName, false))
                return new SendingMessage(ResponseSubject.REPEATED);

            //storing downloaded file
            File downloadingFile = createTempFile();
            Long fileSize = request.getStreamSize("file");
            FileOutputStream downloadOutput = new FileOutputStream(downloadingFile);
            DigestInputStream digestInputStream =
                    new DigestInputStream(request.getInputStream("file"), MessageDigest.getInstance("MD5"));
            IOUtil.writeI2O(downloadOutput, digestInputStream, fileSize);
            downloadOutput.close();

            //calculates downloaded file hash
            MessageDigest digest = digestInputStream.getMessageDigest();
            String hash = IOUtil.printHexBinary(digest.digest());

            SendingMessage response = new SendingMessage(ResponseSubject.OK);
            ProgressWriter progressWriter = new ProgressWriter();
            //sends progress of uploading parts to clients
            response.addInputStream("status", progressWriter.getPipedInputStream(), Long.MAX_VALUE);

            new Thread(() -> sendParts(request, downloadingFile, progressWriter, hash)).start();

            return response;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Handles downloads requests
     *
     * @param request The request
     * @return OK if doesn't fail
     */
    SendingMessage download(ReceivingMessage request) {
        try {

            FSFile file = (FSFile) request.getParameter("file");
            Long fileSize = file.getSize();

            Map<Long, ClientInfo> partMap = clientManager.getFileClientList(file);

            SendingMessage response = new SendingMessage(ResponseSubject.OK);

            ProgressWriter out = new ProgressWriter();
            response.addInputStream("status", out.getPipedInputStream(), Long.MAX_VALUE);

            File downloadingFile = createTempFile();
            response.addInputStream("file", new FileInputStream(downloadingFile), fileSize);

            Thread thread = new Thread(() -> {
                FileOutputStream fileOutputStream;
                try {
                    fileOutputStream = new FileOutputStream(downloadingFile);
                    for (Map.Entry<Long, ClientInfo> entry : partMap.entrySet()) {
                        Long partId = entry.getKey();
                        ClientInfo client = entry.getValue();

                        SRequest partRequest = new SRequest(S2CRequest.SEND_PART);
                        partRequest.addParameter("partId", partId);
                        ResponseCallback responseCallback = partResponse -> IOUtil.writeI2O(
                                fileOutputStream,
                                partResponse.getInputStream("part"),
                                partResponse.getStreamSize("part"),
                                out
                        );
                        partRequest.setResponseCallback(responseCallback);
                        partRequest.send(client).join();
                    }
                    out.close();
                    fileOutputStream.close();
                } catch (InterruptedException | IOException e) {
                    e.printStackTrace();
                }
            });
            thread.start();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sends file parts to clients
     *
     * @param uploadRequest  upload request
     * @param uploaded       client uploaded file
     * @param progressWriter writes upload progress to uploading client
     * @param hash           uploaded file hash
     */
    private void sendParts(ReceivingMessage uploadRequest, File uploaded, ProgressWriter progressWriter, String hash) {
        try {
            String fileName = (String) uploadRequest.getParameter("fileName");
            FSDirectory directory = (FSDirectory) uploadRequest.getParameter("directory");
            User user = (User) uploadRequest.getParameter("user");


            Map<Long, String> parts = fileStorage.splitFile(uploaded, progressWriter);
            Long fileSize = uploaded.length();
            uploaded.delete();

            Map<ClientInfo, Set<Long>> destinations =
                    clientManager.getDestinations(parts.keySet());

            //sends parts to clients
            for (ClientInfo client : destinations.keySet()) {
                ResponseSubject responseSubject = null;
                //if client received parts with same hash returns OK else will keep sending parts
                while (!ResponseSubject.OK.equals(responseSubject))
                    responseSubject = sendAPart(client, destinations.get(client), parts, progressWriter);
            }

            progressWriter.close();

            clientManager.updateParts(destinations);

            for (Long part : parts.keySet())
                fileStorage.getFileById(part).delete();

            //get now time
            Date date = new Date();

            //directory is parent directory of uploading file
            fileSystem.addFile(directory, fileName, fileSize, parts, user.getUsername(), date, date, hash);

            refreshClients();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends client's parts
     *
     * @param client         The client
     * @param parts          The parts
     * @param hashes         parts hashes
     * @param progressWriter writes progress of sending
     * @return OK if everything do well
     * @throws InterruptedException throws if thread interrupts
     */
    private ResponseSubject sendAPart(ClientInfo client, Set<Long> parts,
                                      Map<Long, String> hashes, ProgressWriter progressWriter) throws InterruptedException {
        SRequest partRequest = new SRequest(S2CRequest.RECEIVE_PART);
        for (Long partId : parts) {
            File partFile = fileStorage.getFileById(partId);
            try {
                partRequest.addInputStream(partId.toString(), new FileInputStream(partFile), partFile.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            partRequest.addProgressCallback(partId.toString(), progressWriter);
        }
        //adds hash of parts to request
        partRequest.addParameter("hashList", hashes);

        //locks subject when is in use
        //subject is client's response title
        AtomicReference<ResponseSubject> subject = new AtomicReference<>();
        partRequest.setResponseCallback(response -> subject.set((ResponseSubject) response.getTitle()));
        //waits to request thread ends(sends response)
        partRequest.send(client).join();
        return subject.get();
    }

    /**
     * Request clients to refresh their file
     */
    private void refreshClients() {
        for (ClientInfo client : clientManager.getClientList()) {
            SRequest partRequest = new SRequest(S2CRequest.REFRESH_DIRECTORY);
            partRequest.send(client);
        }
    }

    /**
     * Handles file renames requests
     *
     * @param request The request
     * @return REPEATED if new path is exists else OK
     */
    SendingMessage renameFile(ReceivingMessage request) {
        FSPath path = (FSPath) request.getParameter("path");
        String newName = (String) request.getParameter("newName");
        boolean result = fileSystem.renamePath(path, newName);
        if (result) {
            refreshClients();
            return new SendingMessage(ResponseSubject.OK);
        } else
            return new SendingMessage(ResponseSubject.REPEATED);
    }

    /**
     * Handles create directory requests
     *
     * @param request The request
     * @return REPEATED if new path is exists else OK
     */
    SendingMessage createNewDirectory(ReceivingMessage request) {
        FSDirectory parent = (FSDirectory) request.getParameter("parent");
        String name = (String) request.getParameter("name");
        FSDirectory newDirectory = fileSystem.addDirectory(parent, name);
        if (newDirectory != null) {
            refreshClients();
            return new SendingMessage(ResponseSubject.OK);
        } else
            return new SendingMessage(ResponseSubject.REPEATED);
    }

    /**
     * Handles file remove requests
     *
     * @param request The request
     * @return return OK if no error occurs
     */
    SendingMessage remove(ReceivingMessage request) {
        FSPath path = (FSPath) request.getParameter("path");
        Set<FSFile> files = fileSystem.remove(path);
        Map<ClientInfo, Set<Long>> partMap = clientManager.getDeletingParts(files, new HashSet<>(fileSystem.getPathSet()));

        for (Map.Entry<ClientInfo, Set<Long>> entry : partMap.entrySet()) {
            ClientInfo client = entry.getKey();
            Set<Long> parts = entry.getValue();
            SRequest remiveRequest = new SRequest(S2CRequest.DELETE_PART);
            remiveRequest.addParameter("parts", parts);
            remiveRequest.send(client);
        }
        refreshClients();
        return new SendingMessage(ResponseSubject.OK);
    }

    /**
     * Handles cut&copy requests
     *
     * @param request The request
     * @return REPEATED if new path is exists else OK
     */
    SendingMessage paste(ReceivingMessage request) {
        FSPath path = (FSPath) request.getParameter("path");
        FSDirectory directory = (FSDirectory) request.getParameter("directory");
        PasteOperationType operationType = (PasteOperationType) request.getParameter("operation");
        FSPath newPath = fileSystem.getPath(directory, path.getName(), path instanceof FSDirectory);

        boolean selfPaste = path.equals(newPath);
        if (path instanceof FSDirectory)
            selfPaste |= fileSystem.isInsideDirectory((FSDirectory) path, directory);
        if (selfPaste)
            return new SendingMessage(ResponseSubject.SELF_PASTE);

        if (newPath != null)
            return new SendingMessage(ResponseSubject.REPEATED);

        switch (operationType) {
            case CUT:
                fileSystem.move(path, directory);
                break;
            case COPY:
                fileSystem.copy(path, directory);
                break;
        }
        refreshClients();
        return new SendingMessage(ResponseSubject.OK);
    }

    /**
     * Handles get file distribution info requests
     *
     * @param request The request
     * @return Distribution info in the response
     */
    SendingMessage fileDist(ReceivingMessage request) {
        SendingMessage response = new SendingMessage(ResponseSubject.OK);
        FSFile file = (FSFile) request.getParameter("file");
        response.addParameter("nodes", clientManager.getDist(file));
        return response;
    }

    SendingMessage search(ReceivingMessage request) {
        SendingMessage response = new SendingMessage(ResponseSubject.OK);
        FSDirectory directory = (FSDirectory) request.getParameter("directory");
        response.addParameter("subPath", fileSystem.listRecurSubPaths(directory));
        return response;
    }
}
