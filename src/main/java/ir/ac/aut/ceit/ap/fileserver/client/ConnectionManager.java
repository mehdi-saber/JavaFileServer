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

    public ConnectionManager(String address, int port)  {
        this.port = port;
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

     ExchangeData request(ExchangeData request) {
         ExchangeData response = null;
         try {
             Socket socket = new Socket(address, port);
             Exchange exchange = new Exchange(socket);
             exchange.send(request);
             response = exchange.receive();
         } catch (IOException | ClassNotFoundException e) {
             e.printStackTrace();
         }
         return response;
    }
}
