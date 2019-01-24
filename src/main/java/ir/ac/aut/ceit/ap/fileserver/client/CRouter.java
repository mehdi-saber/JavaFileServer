package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CRequest;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Router;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;


/**
 * routes clients receiving requests
 */
public class CRouter implements Router {
    private Client client;

    /**
     * creates new routes
     *
     * @param client the client
     */
    CRouter(Client client) {
        this.client = client;
    }

    /**
     * @param request the server requests
     * @return response
     */
    public SendingMessage route(ReceivingMessage request) {
        switch ((S2CRequest) request.getTitle()) {
            case RECEIVE_PART:
                return client.fetchPart(request);
            case SEND_PART:
                return client.sendPart(request);
            case REFRESH_DIRECTORY:
                return client.refreshDirectory(request);
            case DELETE_PART:
                return client.deleteParts(request);
        }
        return null;
    }
}
