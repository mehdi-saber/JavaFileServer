package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ClientInfo implements Serializable {
    private String username;
    private Set<Long> parts;
    private String address;
    private int listenPort;

    private ClientInfo(Set<Long> parts, String address, int listenPort, String username) {
        this.username = username;
        this.parts = parts;
        this.address = address;
        this.listenPort = listenPort;
    }

    ClientInfo(String address, int listenPort, String username) {
        this(new HashSet<>(), address, listenPort, username);
    }

    public String getUsername() {
        return username;
    }

    public Set<Long> getParts() {
        return parts;
    }

    public String getAddress() {
        return address;
    }

    public int getListenPort() {
        return listenPort;
    }

}
