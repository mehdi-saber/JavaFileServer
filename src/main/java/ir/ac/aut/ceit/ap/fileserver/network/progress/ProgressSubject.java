package ir.ac.aut.ceit.ap.fileserver.network.progress;

/**
 * Commands between progress reader and writer
 */
enum ProgressSubject {
    WAIT,
    CALL_BACK,
    END
}
