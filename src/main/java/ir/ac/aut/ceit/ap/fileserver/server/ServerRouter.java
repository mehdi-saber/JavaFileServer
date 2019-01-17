package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.Router;


public class ServerRouter implements Router {
    private Server server;

    public ServerRouter(Server server) {
        this.server = server;
    }

    public Message route(Message request) {
        switch (request.getTitle()) {
            case LOGIN:
                return server.registerUser(request);
            case FETCH_DIRECTORY:
                return server.fetchDirectory(request);
            case REMOVE_FILE:
            case MOVE_FILE:
            case RENAME_FILE:
            case UPLOAD_FILE:
                return server.upload(request);
        }
        return null;
    }
}
