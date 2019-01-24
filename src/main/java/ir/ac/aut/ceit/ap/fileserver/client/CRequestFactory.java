package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;

/**
 * creates CRequests
 */
class CRequestFactory {
    private String serverAddress;
    private int port;
    private String token=null;

    /**
     * creates an object of factory
     *
     * @param serverAddress the server address
     * @param port          the server port
     */
    CRequestFactory(String serverAddress, int port) {
        this.serverAddress = serverAddress;
        this.port = port;
    }

    /**
     * create a CRequest
     *
     * @param title request title
     * @return new request
     */
    CRequest create(C2SRequest title) {
        return new CRequest(title, serverAddress, port, token);
    }

    /**
     * sets token on factory
     *
     * @param token user access token
     */
    void setToken(String token) {
        this.token = token;
    }
}
