package ir.ac.aut.ceit.ap.fileserver.network.protocol;

/**
 * Base class of response's subject
 */
public enum ResponseSubject implements Subject {
    OK,
    FORBIDDEN,
    REPEATED,
    SELF_PASTE,
    FAILED
}
