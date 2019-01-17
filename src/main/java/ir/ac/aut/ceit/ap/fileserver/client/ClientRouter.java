package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.Router;

import java.net.Socket;


public class ClientRouter implements Router {
    private Client client;

    public ClientRouter(Client client) {
        this.client = client;
    }

    public Message route(Message request, Socket socket) {
        switch (request.getTitle()) {
            case UPLOAD_FILE:
                return client.fetchFile(request);
        }
        return null;
    }
}
