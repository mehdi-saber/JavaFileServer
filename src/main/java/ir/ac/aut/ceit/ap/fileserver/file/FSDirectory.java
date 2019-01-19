package ir.ac.aut.ceit.ap.fileserver.file;


import java.io.FileNotFoundException;

public class FSDirectory extends FSPath {
    public static final FSDirectory ROOT = new FSDirectory(null, "");

    public FSDirectory(FSDirectory parent, String name) {
        super(parent, name);
    }

    public FSDirectory(String path) throws FileNotFoundException {
        super(path);
    }

    @Override
    public String getAbsolutePath() {
        if (parent != null)
            return parent.getAbsolutePath() + name + SEPARATOR;
        return SEPARATOR;
    }
}
