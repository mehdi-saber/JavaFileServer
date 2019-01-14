package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.C2SCommand;
import ir.ac.aut.ceit.ap.fileserver.network.Request;

public class ConnectionRouter {
    private Server server;
    private SecurityManager securityManager;

    public ConnectionRouter() {
        securityManager = new SecurityManager();
    }

    void route(Request request) {
//        if (securityManager.haveAccess()) ;
//        todo:send 403
        switch ((C2SCommand) request.getCommand()) {
            case MOVE:
                break;
            case REGISTER:
                server.registerUser(request);
                break;
            case LOGIN:
                server.authUser(request);
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
