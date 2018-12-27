package ir.ac.aut.ceit.ap.fileserver.model.client;

import ir.ac.aut.ceit.ap.fileserver.model.network.Request;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnectionManager {
    int port;
    InetAddress address;
    Socket socket;

    public ClientConnectionManager(String address, int port) throws UnknownHostException {
        this.port = port;
        this.address = InetAddress.getByName(address);
    }

    public void sendRequest(Request request) throws IOException {
        Socket socket = new Socket(address, port);
        request.send(socket.getOutputStream());
    }
}
