package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ClientInfo implements Serializable {
    private final Long id;
    private final String username;
    private final Set<Long> parts;
    private final String address;
    private final int listenPort;

    private ClientInfo(Long id, Set<Long> parts, String address, int listenPort, String username) {
        this.username = username;
        this.parts = parts;
        this.address = address;
        this.listenPort = listenPort;
        this.id = id;
    }

    public ClientInfo(Long id, String address, int listenPort, String username) {
        this(id, new HashSet<>(), address, listenPort, username);
    }

    public String getUsername() {
        return username;
    }

    Set<Long> getParts() {
        return parts;
    }

    public String getAddress() {
        return address;
    }

    public int getListenPort() {
        return listenPort;
    }

    public Long getId() {
        return id;
    }
}
