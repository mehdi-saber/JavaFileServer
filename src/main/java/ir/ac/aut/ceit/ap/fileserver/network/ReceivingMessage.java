package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;

public class ReceivingMessage extends Message implements Serializable {
    private Socket socket;

    ReceivingMessage(Message message, Socket socket) {
        super(message);
        this.socket = socket;
    }

    public InputStream getInputStream(String key) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println(StreamsCommand.GET_STREAM_BY_KEY);
            out.println(key);
            out.flush();
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
