package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Router;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.S2CRequest;


public class CRouter implements Router {
    private Client client;

    public CRouter(Client client) {
        this.client = client;
    }

    public SendingMessage route(ReceivingMessage request) {
        switch ((S2CRequest) request.getTitle()) {
            case RECEIVE_PART:
                return client.fetchPart(request);
            case SEND_PART:
                return client.sendPart(request);
            case REFRESH_DIRECTORY:
                return client.refreshDirectory(request);
        }
        return null;
    }
}
