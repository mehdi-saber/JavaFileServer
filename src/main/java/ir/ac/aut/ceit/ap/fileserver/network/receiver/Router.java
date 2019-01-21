package ir.ac.aut.ceit.ap.fileserver.network.receiver;

import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;

public interface Router {
    SendingMessage route(ReceivingMessage request);
}
