package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.Socket;

public class ReceivingMessage extends Message implements Serializable {
    private Socket socket;

    public ReceivingMessage(Message message, Socket socket) {
        super(message);
        this.socket = socket;
    }

    public InputStream getStream(String key) {
        try {
            socket.getOutputStream().write((key + "\n").getBytes());
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSenderAddress() {
        return socket.getInetAddress().getHostAddress();
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
