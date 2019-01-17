package ir.ac.aut.ceit.ap.fileserver.network;

public interface Router {
    Message route(ReceivingMessage request);
}
