package ir.ac.aut.ceit.ap.fileserver.server;

import java.util.ArrayList;
import java.util.List;

public class ClientInfo {
   private String username;
    private List<Long> parts;
    private String address;
   private int listenPort;

    private ClientInfo(List<Long> parts, String address, int listenPort, String username) {
        this.username = username;
        this.parts = parts;
        this.address = address;
        this.listenPort = listenPort;
    }

    ClientInfo(String address, int listenPort, String username) {
        this(new ArrayList<>(), address, listenPort, username);
    }

    public String getUsername() {
        return username;
    }

    public List<Long> getParts() {
        return parts;
    }

    public String getAddress() {
        return address;
    }

    public int getListenPort() {
        return listenPort;
    }
}
