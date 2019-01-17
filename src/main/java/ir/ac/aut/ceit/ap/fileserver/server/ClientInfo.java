package ir.ac.aut.ceit.ap.fileserver.server;

import java.util.List;

public class ClientInfo {
   private String username;
   private List<String> parts;
   private String address;
   private int listenPort;

    ClientInfo(List<String> parts, String address, int listenPort, String username) {
        this.username = username;
        this.parts = parts;
        this.address = address;
        this.listenPort = listenPort;
    }

    public String getUsername() {
        return username;
    }

    public List<String> getParts() {
        return parts;
    }

    public String getAddress() {
        return address;
    }

    public int getListenPort() {
        return listenPort;
    }
}
