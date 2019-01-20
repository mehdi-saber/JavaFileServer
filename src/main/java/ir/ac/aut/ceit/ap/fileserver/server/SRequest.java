package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.Request;
import ir.ac.aut.ceit.ap.fileserver.network.Subject;

class SRequest extends Request {
    SRequest(Subject title) {
        super(title);
    }

    Thread send(ClientInfo client) {
        return send(client.getAddress(), client.getListenPort());
    }
}
