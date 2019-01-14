package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.C2SCommand;
import ir.ac.aut.ceit.ap.fileserver.network.Request;

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
        Request request = new Request(C2SCommand.LOGIN);
        request.addParameter("username", username);
        request.addParameter("password", password);
        connectionManager.sendRequest(request);
    }

}
