package ir.ac.aut.ceit.ap.fileserver.network.receiver;


import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.StreamingSubject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

public class ReceivingMessage extends Message implements Serializable {
    private final OutputStream socketOutputStream;
    private final InputStream socketInputStream;
    private final String senderAddress;

    public ReceivingMessage(Message message, OutputStream socketOutputStream, InputStream socketInputStream, String senderAddress) {
        super(message);
        this.socketOutputStream = socketOutputStream;
        this.senderAddress = senderAddress;
        this.socketInputStream = socketInputStream;
    }

    public InputStream getInputStream(String key) {
        PrintWriter out = new PrintWriter(socketOutputStream);
        out.println(StreamingSubject.SWITCH_TO_STREAM);
        out.println(key);
        out.flush();
        return socketInputStream;
    }

    public String getSenderAddress() {
        return senderAddress;
    }
}
