package ir.ac.aut.ceit.ap.fileserver.network;

public interface Router {
    SendingMessage route(ReceivingMessage request);
}
