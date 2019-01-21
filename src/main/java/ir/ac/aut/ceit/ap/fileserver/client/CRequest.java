package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.request.Request;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;

class CRequest extends Request {
    private String serverAddress;
    private int port;

    CRequest(C2SRequest title, String serverAddress, int port, String token) {
        super(title);
        this.serverAddress = serverAddress;
        this.port = port;
        addParameter("token", token);
    }

    Thread send() {
        return super.send(serverAddress, port);
    }
}
