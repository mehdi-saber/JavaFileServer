package ir.ac.aut.ceit.ap.fileserver.model.server;

import ir.ac.aut.ceit.ap.fileserver.model.network.AuthDto;
import ir.ac.aut.ceit.ap.fileserver.model.network.Request;

import java.io.IOException;

public class ConnectionRouter {
    private Server server;

    public ConnectionRouter() {
    }

    void route(Request request) throws IOException {
        switch (request.getCommand()) {
            case MOVE:
                break;
            case LOGIN:
                AuthDto authDto = (AuthDto) request.getObject("authDto");
                server.loginUser(authDto);
                break;
            case REMOVE:
                break;
            case RENAME:
                break;
            case UPLOAD:
                break;
            default:
                //todo:exception
                break;
        }
    }
}
