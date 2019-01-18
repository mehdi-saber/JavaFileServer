package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.Router;
import ir.ac.aut.ceit.ap.fileserver.network.SendingMessage;


public class ClientRouter implements Router {
    private Client client;

    public ClientRouter(Client client) {
        this.client = client;
    }

    public SendingMessage route(ReceivingMessage request) {
        switch (request.getTitle()) {
            case FETCH_PART:
                return client.fetchFile(request);
        }
        return null;
    }
}
