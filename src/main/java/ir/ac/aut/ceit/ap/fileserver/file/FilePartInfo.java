package ir.ac.aut.ceit.ap.fileserver.file;

public class FilePartInfo {
    private String hash;

    public FilePartInfo(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
