package ir.ac.aut.ceit.ap.fileserver.network.receiver;

/**
 * A callback for response
 */
public interface ResponseCallback {
    void call(ReceivingMessage response);
}
