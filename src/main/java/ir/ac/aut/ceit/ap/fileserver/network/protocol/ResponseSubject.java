package ir.ac.aut.ceit.ap.fileserver.network.protocol;

public enum ResponseSubject implements Subject {
    OK,
    FORBIDDEN,
    REPEATED,
    SELF_PASTE,
    FAILED
}
