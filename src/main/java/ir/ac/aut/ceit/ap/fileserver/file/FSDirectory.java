package ir.ac.aut.ceit.ap.fileserver.file;


public class FSDirectory extends FSPath {
    public static final FSDirectory ROOT = new FSDirectory(null, "");

    FSDirectory(FSDirectory parent, String name) {
        super(parent, name);
    }

    @Override
    public String getAbsolutePath() {
        if (this.equals(ROOT))
            return SEPARATOR;
        return parent.getAbsolutePath() + name + SEPARATOR;
    }
}
