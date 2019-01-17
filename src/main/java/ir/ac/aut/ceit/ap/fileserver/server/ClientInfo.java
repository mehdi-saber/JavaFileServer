package ir.ac.aut.ceit.ap.fileserver.server;

import java.util.List;

public class ClientInfo {
    List<String> parts;
    String address;
    int listenPort;

    public ClientInfo(List<String> parts, String address, int listenPort) {
        this.parts = parts;
        this.address = address;
        this.listenPort = listenPort;
    }
}
