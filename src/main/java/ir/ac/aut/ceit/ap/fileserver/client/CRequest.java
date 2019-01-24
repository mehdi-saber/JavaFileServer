package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;
import ir.ac.aut.ceit.ap.fileserver.network.request.Request;

/**
 * sends client to server request
 */
class CRequest extends Request {
    private String serverAddress;
    private int port;

    /**
     * create a request
     *
     * @param title         request title
     * @param serverAddress server address
     * @param port          server port number
     * @param token         user token
     */
    CRequest(C2SRequest title, String serverAddress, int port, String token) {
        super(title);
        this.serverAddress = serverAddress;
        this.port = port;
        addParameter("token", token);
    }

    /**
     * send request to server
     *
     * @return the request thread
     */
    Thread send() {
        return super.send(serverAddress, port);
    }
}
