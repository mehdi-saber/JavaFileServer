package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.Router;
import ir.ac.aut.ceit.ap.fileserver.network.SendingMessage;

import java.io.IOException;


public class ServerRouter implements Router {
    private Server server;

    public ServerRouter(Server server) {
        this.server = server;
    }

    public SendingMessage route(ReceivingMessage request) {
        switch (request.getTitle()) {
            case LOGIN:
                return server.loginUser(request, request.getSenderAddress());
            case FETCH_DIRECTORY:
                return server.fetchDirectory(request);
            case REMOVE_FILE:
            case MOVE_FILE:
            case RENAME_FILE:
            case UPLOAD_FILE:
                try {
                    return server.upload(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
}
