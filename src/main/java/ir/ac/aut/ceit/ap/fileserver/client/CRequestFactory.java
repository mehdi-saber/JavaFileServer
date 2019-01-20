package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.Subject;

class CRequestFactory {
    private String serverAddress;
    private int port;
    private String token=null;

    CRequestFactory(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    CRequest create(Subject title) {
        return new CRequest(title, serverAddress, port, token);
    }

     void setToken(String token) {
        this.token = token;
    }
}