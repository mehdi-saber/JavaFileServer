package ir.ac.aut.ceit.ap.fileserver.file;


public class FSDirectory extends FSPath {
    public static final FSDirectory ROOT = new FSDirectory(null, "");

    public FSDirectory(FSDirectory parent, String name) {
        super(parent, name);
    }

}
