package ir.ac.aut.ceit.ap.fileserver.network.progress;

/**
 * A callback for progress
 */
public interface ProgressCallback {
    /**
     * Calls when progress happen
     *
     * @param doneDelta Bytes done length
     */
    void call(int doneDelta);
}
