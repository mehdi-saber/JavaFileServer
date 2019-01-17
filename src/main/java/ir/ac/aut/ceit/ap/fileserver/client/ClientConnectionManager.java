package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.ConnectionManager;
import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.Router;

public class ClientConnectionManager extends ConnectionManager {
    private final int serverPort;
    private final String serverAddress;
    String token;

    public ClientConnectionManager(int listenPort, Router router, String serverAddress, int port) {
        super(listenPort, router);
        this.serverPort = port;
        this.serverAddress = serverAddress;
    }

    public Message sendRequest(Message request) {
        request.addParameter("token", token);
        return super.sendRequest(request, serverAddress, serverPort);
    }
}
