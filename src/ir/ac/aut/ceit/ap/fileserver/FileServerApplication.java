package ir.ac.aut.ceit.ap.fileserver;

import ir.ac.aut.ceit.ap.fileserver.controller.ClientViewController;
import ir.ac.aut.ceit.ap.fileserver.model.server.Server;

import java.io.IOException;

public class FileServerApplication {

    public static void main(String[] args) {
        try {
            Server server=new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClientViewController controller = new ClientViewController();
    }
}
