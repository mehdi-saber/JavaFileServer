package ir.ac.aut.ceit.ap.fileserver.network.receiver;

import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;

/**
 * routes requests to responses
 */
public interface Router {
    /**
     * routes request to response
     *
     * @param request the request
     * @return the response
     */
    SendingMessage route(ReceivingMessage request);
}
