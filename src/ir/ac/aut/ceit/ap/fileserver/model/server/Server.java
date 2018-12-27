package ir.ac.aut.ceit.ap.fileserver.model.server;

import ir.ac.aut.ceit.ap.fileserver.model.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.model.network.AuthDto;

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

    public void loginUser(AuthDto authDto) {
        System.out.println(authDto.getUsername()+" "+authDto.getPassword());
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
