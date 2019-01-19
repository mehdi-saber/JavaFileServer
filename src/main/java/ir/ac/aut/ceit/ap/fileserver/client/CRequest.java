package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.Subject;

class CRequest extends SendingMessage {
    private String serverAddress;
    private int port;

    CRequest(Subject title, String serverAddress, int port, String token) {
        super(title);
        this.serverAddress = serverAddress;
        this.port = port;
        addParameter("token", token);
    }

    Thread send() {
        return super.send(serverAddress, port);
    }
}
