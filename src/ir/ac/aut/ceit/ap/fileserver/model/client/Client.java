package ir.ac.aut.ceit.ap.fileserver.model.client;

import ir.ac.aut.ceit.ap.fileserver.controller.ClientViewController;
import ir.ac.aut.ceit.ap.fileserver.model.network.Command;
import ir.ac.aut.ceit.ap.fileserver.model.network.Request;
import ir.ac.aut.ceit.ap.fileserver.model.network.AuthDto;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Map;

public class Client {
    private Map<FilePartitionInfo, File> partitionsMap;
    private ClientViewController viewController;
    private ClientConnectionManager connectionManager;

    public Client() throws UnknownHostException {
        connectionManager = new ClientConnectionManager("localhost", 5000);
    }

    public void Login(String username, String password) throws IOException {
        AuthDto authDto = new AuthDto(username, password);
        Request request = new Request (Command.LOGIN);
        connectionManager.sendRequest(request);
    }

}
