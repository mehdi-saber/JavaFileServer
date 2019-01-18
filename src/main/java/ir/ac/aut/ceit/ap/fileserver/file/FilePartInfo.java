package ir.ac.aut.ceit.ap.fileserver.file;

import java.io.Serializable;

public class FilePartInfo implements Serializable {
    private long id;
    private String hash;

    public FilePartInfo(long id, String hash) {
        this.id = id;
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }

    public long getId() {
        return id;
    }
}
