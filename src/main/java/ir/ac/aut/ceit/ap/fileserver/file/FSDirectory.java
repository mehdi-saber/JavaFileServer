package ir.ac.aut.ceit.ap.fileserver.file;


public class FSDirectory extends FSPath {
    public static final FSDirectory ROOT = new FSDirectory(null, "");

    FSDirectory(FSDirectory parent, String name) {
        super(parent, name);
    }

    FSDirectory(String path) throws Exception {
        super(path);
    }
}