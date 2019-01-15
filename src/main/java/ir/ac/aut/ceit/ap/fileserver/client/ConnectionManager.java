package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.network.Exchange;
import ir.ac.aut.ceit.ap.fileserver.network.ExchangeData;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

 class ConnectionManager {
    int port;
    InetAddress address;
    Socket socket;

    public ConnectionManager(String address, int port) throws UnknownHostException {
        this.port = port;
        this.address = InetAddress.getByName(address);
    }

    ExchangeData request(ExchangeData requestData) throws IOException {
        Socket socket = new Socket(address, port);
        Exchange transfer = new Exchange(socket);
        transfer.send(requestData);
        return transfer.receive();
    }
}
