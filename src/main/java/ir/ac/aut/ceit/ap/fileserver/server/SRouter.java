package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.C2SRequest;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.Router;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;


class SRouter implements Router {
    private Server server;

    SRouter(Server server) {
        this.server = server;
    }

    public SendingMessage route(ReceivingMessage request) {
        switch ((C2SRequest) request.getTitle()) {
            case LOGIN:
                return server.loginUser(request);
            case FETCH_DIRECTORY:
                return server.fetchDirectory(request);
            case DOWNLOAD_FILE:
                return server.download(request);
            case RENAME_FILE:
                return server.renameFile(request);
            case UPLOAD_FILE:
                    return server.upload(request);
            case CREATE_NEW_DIRECTORY:
                return server.createNewDirectory(request);
            case PASTE:
                return server.paste(request);
        }
        return null;
    }
}
