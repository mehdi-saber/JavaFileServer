package ir.ac.aut.ceit.ap.fileserver.network.protocol;

/**
 * Server to client request types
 */
public enum S2CRequest implements RequestSubject {
    RECEIVE_PART,
    SEND_PART,
    REFRESH_DIRECTORY,
    DELETE_PART,
}
