package ir.ac.aut.ceit.ap.fileserver.client;

import java.io.File;

class FilePartManager {
    File directory;

    FilePartManager(int listenPort) {
        directory = new File("data"+File.separator+"parts" + File.separator + listenPort);
        directory.mkdirs();
    }

    File getFileAddress(Long partId) {
        return new File(directory + File.separator + partId);
    }


}
