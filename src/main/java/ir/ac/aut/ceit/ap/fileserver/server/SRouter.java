package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Router;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.server.security.SecurityManager;
import ir.ac.aut.ceit.ap.fileserver.server.security.User;

/**
 * Server receiving requests router
 */
class SRouter implements Router {
    private Server server;
    private SecurityManager securityManager;

    /**
     * Construct a router for servers
     * @param server The server
     * @param securityManager The server security manager
     */
    SRouter(Server server, SecurityManager securityManager) {
        this.server = server;
        this.securityManager = securityManager;
    }

    /**
     * Routes receiving requests to sending responses
     * @param request The receiving request
     * @return response
     */
    public SendingMessage route(ReceivingMessage request) {
        C2SRequest title = (C2SRequest) request.getTitle();
        if (!title.equals(C2SRequest.LOGIN)) {
            String token = (String) request.getParameter("token");
            if (token == null)
                return new SendingMessage(ResponseSubject.FORBIDDEN);
            String username = securityManager.getUsername(token);
            User user = securityManager.getUserByUserName(username);
            request.addParameter("user", user);
        }
        switch (title) {
            case LOGIN:
                return server.loginUser(request);
            case FETCH_DIRECTORY:
                return server.fetchDirectory(request);
            case Preview:
                return server.download(request);
            case RENAME_FILE:
                return server.renameFile(request);
            case UPLOAD_FILE:
                    return server.upload(request);
            case CREATE_NEW_DIRECTORY:
                return server.createNewDirectory(request);
            case PASTE:
                return server.paste(request);
            case DELETE_FILE:
                return server.remove(request);
            case FILE_DIST:
                return server.fileDist(request);
        }
        return null;
    }
}
