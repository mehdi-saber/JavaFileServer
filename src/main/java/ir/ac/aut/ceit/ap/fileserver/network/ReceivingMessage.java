package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Set;

public class ReceivingMessage extends Message {
    private Set<String> streamKey;
    private Socket socket;

    public ReceivingMessage(Subject title, Socket socket) {
        super(title);
        this.socket = socket;
    }

    public ReceivingMessage(Message message, Set<String> streamKey, Socket socket) {
        super(message);
        this.socket = socket;
        this.streamKey = streamKey;
    }

    public InputStream getStream(String key) {
        try {
            socket.getOutputStream().write(key.getBytes());
            return socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSenderAddress() {
        return socket.getInetAddress().getHostAddress();
    }
}
