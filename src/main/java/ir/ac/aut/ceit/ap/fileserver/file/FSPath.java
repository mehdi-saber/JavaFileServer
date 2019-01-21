package ir.ac.aut.ceit.ap.fileserver.file;


import java.io.Serializable;
import java.util.Objects;

abstract public class FSPath implements Serializable {
    protected FSDirectory parent;
    protected String name;
    public static final String SEPARATOR = "/";

    FSPath(FSDirectory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    public FSDirectory getParent() {
        return parent;
    }

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

    public String getName() {
        return name;
    }

    public void setParent(FSDirectory parent) {
        this.parent = parent;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FSPath path = (FSPath) o;
        return Objects.equals(parent, path.parent) &&
                Objects.equals(name, path.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, name);
    }
}
