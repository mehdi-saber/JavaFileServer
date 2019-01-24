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

    /**
     * Construct a new client info
     *
     * @param id         The assigned ID
     * @param address    The client IP address
     * @param listenPort The client receive port number
     * @param username   The client username
     */
    public ClientInfo(Long id, String address, int listenPort, String username) {
        this.username = username;
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
}
