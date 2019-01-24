package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.file.FileStorage;

import java.io.File;

/**
 * Handles clients storage behaviors
 */
class CFileStorage extends FileStorage {
    /**
     * sets directory name by port number
     *
     * @param listenPort the port number
     */
    public void setPort(int listenPort) {
        super.setDirectory("data" + File.separator + listenPort);
    }
}
