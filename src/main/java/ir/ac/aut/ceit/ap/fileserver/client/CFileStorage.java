package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.file.FileStorage;

import java.io.File;

class CFileStorage extends FileStorage {
    public void setPort(int listenPort) {
        super.setDirectory("data" + File.separator + listenPort);
    }
}
