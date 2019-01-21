package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.request.Request;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CRequest;

class SRequest extends Request {
    SRequest(S2CRequest title) {
        super(title);
    }

    Thread send(ClientInfo client) {
        return send(client.getAddress(), client.getListenPort());
    }
}
