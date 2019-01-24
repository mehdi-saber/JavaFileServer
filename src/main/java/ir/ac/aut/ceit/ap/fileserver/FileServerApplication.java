package ir.ac.aut.ceit.ap.fileserver;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.server.Server;

public class FileServerApplication {

    public static void main(String[] args) {
        Server server = new Server();
        new Client();
//        new Client();
//        new Client();
    }
}
