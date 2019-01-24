package ir.ac.aut.ceit.ap.fileserver.file;

/**
 * Keeps system directory info
 */
public class FSDirectory extends FSPath {
    //the system root folder
    public static final FSDirectory ROOT = new FSDirectory(null, "");

    /**
     * Get a new directory object
     *
     * @param parent The directory parent
     * @param name   The directory name
     */
    public FSDirectory(FSDirectory parent, String name) {
        super(parent, name);
    }

}
