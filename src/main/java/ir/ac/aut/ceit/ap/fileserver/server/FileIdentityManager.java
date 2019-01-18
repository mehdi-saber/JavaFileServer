package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.File;

class FileIdentityManager {
    private File directory;
    private Long idCounter;

    public synchronized Long createId() {
        return idCounter++;
    }

    FileIdentityManager(String directoryName, Long idCounter) {
        this.directory = new File("data/server/" + directoryName);
        this.directory.mkdirs();
        this.idCounter = idCounter;
    }

    File getFileById(Long fileId) {
        return new File(directory + File.separator + fileId);
    }
}
