package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CRequest;
import ir.ac.aut.ceit.ap.fileserver.network.request.Request;

/**
 * Handles sending server requests to clients
 */
class SRequest extends Request {
    /**
     * Constructs a request
     *
     * @param title The request title
     */
    SRequest(S2CRequest title) {
        super(title);
    }

    /**
     * Sends server request to a client
     *
     * @param client The client
     * @return Thread request Thread
     */
    Thread send(ClientInfo client) {
        return send(client.getAddress(), client.getListenPort());
    }
}
