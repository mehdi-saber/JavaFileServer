package ir.ac.aut.ceit.ap.fileserver.network;

import java.net.Socket;

public interface Router {
    Message route(Message request, Socket socket);
}
