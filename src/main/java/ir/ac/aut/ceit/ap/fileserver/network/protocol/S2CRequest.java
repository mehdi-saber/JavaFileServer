package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum  S2CRequest implements RequestSubject {
    RECEIVE_PART,
    SEND_PART,
    REFRESH_DIRECTORY, DELETE_PART,
}
