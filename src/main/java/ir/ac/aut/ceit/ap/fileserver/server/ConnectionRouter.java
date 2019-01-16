package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.network.Exchange;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;

import java.io.IOException;
import java.net.Socket;

public class ConnectionRouter {
    private Server server;

    public ConnectionRouter(Server server) {
        this.server = server;
        SecurityManager securityManager = new SecurityManager();
    }

    void route(Socket socket) throws IOException, ClassNotFoundException {
        Exchange exchange = new Exchange(socket);
        ExchangeData request = exchange.receive();

//        if (securityManager.haveAccess()) ;
//        todo:send 403

        ExchangeData response = null;
        switch (request.getTitle()) {
            case REGISTER_USER:
                response = server.registerUser(request);
                break;
            case LOGIN_USER:
                break;
            case FETCH_DIRECTORY:
                response=server.fetchDirectory(request);
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
        exchange.send(response);
        socket.close();
    }
}
