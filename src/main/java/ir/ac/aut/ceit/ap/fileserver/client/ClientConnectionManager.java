package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.DataTransfer;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;

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

    ExchangeData request(ExchangeData requestData) throws IOException {
        Socket socket = new Socket(address, port);
        DataTransfer transfer = new DataTransfer(socket);
        transfer.send(requestData);
        return transfer.receive();
    }
}
