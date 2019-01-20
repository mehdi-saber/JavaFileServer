package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.file.FileStorage;

import java.io.File;

class CFileStorage extends FileStorage {
    CFileStorage(long listenPort) {
        super("data" + File.separator + listenPort);
    }
}
