package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.Subject;

class RequestFactory {
    private String serverAddress;
    private int port;
    private String token;

    RequestFactory(String serverAddress, int port, String token) {
        this.serverAddress = serverAddress;
        this.port = port;
        this.token = token;
    }

    CRequest create(Subject title) {
        return new CRequest(title, serverAddress, port, token);
    }
}
