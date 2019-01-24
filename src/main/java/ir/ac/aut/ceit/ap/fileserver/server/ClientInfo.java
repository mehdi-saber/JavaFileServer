package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Holds clients info
 */
public class ClientInfo implements Serializable {
    private final Long id;
    private final String username;
    private final Set<Long> parts;
    private final String address;
    private final int listenPort;
    private Integer space;

    /**
     * Construct a new client info
     * @param id           assigned ID
     * @param address      client IP address
     * @param listenPort   client receive port number
     * @param username     client username
     * @param space    client HDD space Left
     */
    public ClientInfo(Long id, String address, int listenPort, String username, Integer space) {
        this.username = username;
        this.space = space;
        this.parts = new HashSet<>();
        this.address = address;
        this.listenPort = listenPort;
        this.id = id;
    }


    /**
     * @return The client username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @return The client parts ID set
     */
    Set<Long> getParts() {
        return parts;
    }

    /**
     *
     * @return The Client IP address
     */
    String getAddress() {
        return address;
    }

    /**
     *
     * @return The Client receive port number
     */
    int getListenPort() {
        return listenPort;
    }

    /**
     *
     * @return The Client assigned ID
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @return free space
     */
    public Integer getSpace() {
        return space;
    }
}
