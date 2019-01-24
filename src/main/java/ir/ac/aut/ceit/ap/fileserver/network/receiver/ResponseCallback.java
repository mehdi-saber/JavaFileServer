package ir.ac.aut.ceit.ap.fileserver.network.receiver;

/**
 * A callback for response
 */
public interface ResponseCallback {
    /**
     * calls when response arrives
     *
     * @param response the response
     */
    void call(ReceivingMessage response);
}
