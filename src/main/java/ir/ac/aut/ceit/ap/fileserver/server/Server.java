package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.network.Request;

import java.io.IOException;
import java.util.List;

public class Server {
    List<ClientInfo> clientList;
    FileSystem fileSystem;
    ServerConnectionManager connectionManager;

    public Server() throws IOException {
        connectionManager = new ServerConnectionManager(5000);
    }

    public void setClientList(List<ClientInfo> clientList) {
        this.clientList = clientList;
    }

    public void registerUser(Request request) {

    }

    public void authUser(Request request) {


    }

    public void addFile(FileInfo info, byte[] data) {

    }

    public void removeFile(FileInfo info) {

    }

    public void fetchFile(FileInfo info) {

    }


    public void renameFile(FileInfo info, byte[] data) {

    }
}
