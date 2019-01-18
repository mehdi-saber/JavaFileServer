package ir.ac.aut.ceit.ap.fileserver.file;

import java.io.Serializable;

public class FilePartInfo implements Serializable {
    private String hash;

    public FilePartInfo(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
