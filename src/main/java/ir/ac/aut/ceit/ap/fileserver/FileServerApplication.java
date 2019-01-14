package ir.ac.aut.ceit.ap.fileserver;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.server.Server;

import java.io.IOException;

public class FileServerApplication {

    public static void main(String[] args) {
        try {
            Server server=new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Client client = new Client();
            client.Login("admin", "admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
