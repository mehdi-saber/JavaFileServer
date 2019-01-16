package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Exchange {
    private Socket socket;

    public Exchange(Socket socket) {
        this.socket = socket;
    }

   public void send(ExchangeData exchangeData) throws IOException {
       ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
       outputStream.writeObject(exchangeData);
    }

    public ExchangeData receive() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
        return (ExchangeData) inputStream.readObject();
    }
}
