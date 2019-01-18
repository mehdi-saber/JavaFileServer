package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.ConnectionManager;
import ir.ac.aut.ceit.ap.fileserver.network.Router;
import ir.ac.aut.ceit.ap.fileserver.network.SendingMessage;

class ClientConnectionManager extends ConnectionManager {
    private final int serverPort;
    private final String serverAddress;
    private String token;

    ClientConnectionManager(int listenPort, Router router, String serverAddress, int port) {
        super(listenPort, router);
        this.serverPort = port;
        this.serverAddress = serverAddress;
    }

    Thread sendRequest(SendingMessage request) {
        request.addParameter("token", token);
        return super.sendRequest(request, serverAddress, serverPort);
    }

    public void setToken(String token) {
        this.token = token;
    }
}
