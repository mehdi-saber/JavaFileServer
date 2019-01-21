package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum C2SResponse implements ResponseSubject {
    RECEIVE_PART_OK,
    SEND_PART_OK,
    REFRESH_DIRECTORY_OK,
}
