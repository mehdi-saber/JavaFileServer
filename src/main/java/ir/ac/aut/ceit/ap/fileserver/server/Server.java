package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeTitle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server {
    List<ClientInfo> clientList;
    FileSystem fileSystem;
    ServerConnectionManager connectionManager;
    SecurityManager securityManager;

    public Server() throws IOException {
        connectionManager = new ServerConnectionManager(this, 5000);
        securityManager = new SecurityManager();
        clientList = new ArrayList<>();
    }

    ExchangeData registerUser(ExchangeData requestData) {
        String username = (String) requestData.getObject("username");
        String password = (String) requestData.getObject("password");

        if (username.matches("/^[a-z][^\\W_]{3,14}$/i") &&
                password.matches("/^(?=[^a-z]*[a-z])(?=\\D*\\d)[^:&.~\\s]{5,20}$/"))
            return new ExchangeData(ExchangeTitle.REGISTER_USER_PASS_NOT_ACCEPTED);

        for (ClientInfo client : clientList)
            if (client.username.equals(username))
                return new ExchangeData(ExchangeTitle.REGISTER_USER_REPEATED_USERNAME);

        ExchangeData tokenData = new ExchangeData(ExchangeTitle.REGISTER_USER_ACCEPTED);
        String token = SecurityManager.generateToken(username);
        tokenData.addParameter("token", token);
        return tokenData;
    }

    public void authUser(ExchangeData requestData) {
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
