package ir.ac.aut.ceit.ap.fileserver.file;


import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Objects;

abstract public class FSPath implements Serializable {
    final FSDirectory parent;
    final String name;
    public static final String SEPARATOR = "/";

    FSPath(FSDirectory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    FSPath(String path) throws FileNotFoundException {
        int sepIndex = path.lastIndexOf(SEPARATOR);
        if (sepIndex != -1) {
            parent = new FSDirectory(path.substring(0, sepIndex));
            name = path.substring(sepIndex);
        } else
            throw new FileNotFoundException("INVALID PATH.");
    }

    public FSDirectory getParent() {
        return parent;
    }

    abstract public String getAbsolutePath();

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FSPath fsPath = (FSPath) o;
        return getAbsolutePath().equals(fsPath.getAbsolutePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, name);
    }
}
