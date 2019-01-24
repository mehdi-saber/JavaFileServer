package ir.ac.aut.ceit.ap.fileserver.network.receiver;


import ir.ac.aut.ceit.ap.fileserver.network.Message;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.StreamingSubject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

/**
 * incoming message
 */
public class ReceivingMessage extends Message implements Serializable {
    private final OutputStream socketOutputStream;
    private final InputStream socketInputStream;
    private final String senderAddress;

    /**
     * creates new object
     *
     * @param message            a sending message
     * @param socketOutputStream the socket output stream
     * @param socketInputStream  the socket input stream
     * @param senderAddress      the incoming socket address
     */
    public ReceivingMessage(Message message, OutputStream socketOutputStream, InputStream socketInputStream, String senderAddress) {
        super(message);
        this.socketOutputStream = socketOutputStream;
        this.senderAddress = senderAddress;
        this.socketInputStream = socketInputStream;
    }

    /**
     * gets streaming input stream by key
     *
     * @param key the key
     * @return the input stream
     */
    public InputStream getInputStream(String key) {
        PrintWriter out = new PrintWriter(socketOutputStream);
        out.println(StreamingSubject.SWITCH_TO_STREAM);
        out.println(key);
        out.flush();
        return socketInputStream;
    }

    /**
     * @return incoming socket address
     */
    public String getSenderAddress() {
        return senderAddress;
    }
}
