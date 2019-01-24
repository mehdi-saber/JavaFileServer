package ir.ac.aut.ceit.ap.fileserver.file;


import java.io.Serializable;
import java.util.Objects;

/**
 * keeps file system paths info
 */
abstract public class FSPath implements Serializable {
    public static final String SEPARATOR = "/";
    protected FSDirectory parent;
    protected String name;

    /**
     * Create new object
     *
     * @param parent parent directory
     * @param name   path name
     */
    FSPath(FSDirectory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    /**
     * @return parent directory
     */
    public FSDirectory getParent() {
        return parent;
    }

    /**
     * @param parent path parent
     */
    public void setParent(FSDirectory parent) {
        this.parent = parent;
    }

    /**
     * @return path form root
     */
    public String getAbsolutePath() {
        if (this.equals(FSDirectory.ROOT))
            return SEPARATOR;
        StringBuilder absolute = new StringBuilder(name);
        if (this instanceof FSDirectory)
            absolute.append(SEPARATOR);
        FSDirectory parDir = parent;
        while (!parDir.equals(FSDirectory.ROOT)) {
            absolute.insert(0, SEPARATOR);
            absolute.insert(0, parDir.name);
            parDir = parDir.parent;
        }
        absolute.insert(0, SEPARATOR);
        return absolute.toString();
    }

    /**
     * @return path name
     */
    public String getName() {
        return name;
    }

    /**
     * sets path name
     *
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Checks other object equality
     *
     * @param o other Object
     * @return True if two object are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FSPath path = (FSPath) o;
        return Objects.equals(parent, path.parent) &&
                Objects.equals(name, path.name);
    }

    /**
     * calculates hash
     *
     * @return The hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(parent, name);
    }
}
