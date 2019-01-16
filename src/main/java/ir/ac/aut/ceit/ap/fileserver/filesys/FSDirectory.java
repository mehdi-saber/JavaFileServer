package ir.ac.aut.ceit.ap.fileserver.filesys;


public class FSDirectory extends FSPath {
    FSDirectory(FSDirectory parent, String name) {
        super(parent, name);
    }

    FSDirectory(String path) throws Exception {
        super(path);
    }
}
