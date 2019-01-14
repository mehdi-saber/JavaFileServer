package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.DataTransfer;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;

import java.io.IOException;
import java.net.Socket;

public class ConnectionRouter {
    private Server server;

    public ConnectionRouter(Server server) {
        this.server = server;
        SecurityManager securityManager = new SecurityManager();
    }

    void route(Socket socket) throws IOException {
        DataTransfer dataTransfer = new DataTransfer(socket);
        ExchangeData requestData = dataTransfer.receive();
//        if (securityManager.haveAccess()) ;
//        todo:send 403
        ExchangeData responseData = null;
        switch (requestData.getTitle()) {
            case REGISTER_USER:
                responseData = server.registerUser(requestData);
                break;
            case LOGIN_USER:
                break;
            case REMOVE_FILE:
                break;
            case MOVE_FILE:
                break;
            case RENAME_FILE:
                break;
            case UPLOAD_FILE:
                break;
        }
        dataTransfer.send(responseData);
        socket.close();
    }
}
