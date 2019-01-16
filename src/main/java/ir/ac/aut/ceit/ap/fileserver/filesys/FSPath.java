package ir.ac.aut.ceit.ap.fileserver.filesys;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

abstract public class FSPath implements Serializable {
    protected final FSDirectory parent;
    protected final String name;
    private static final String SEPARATOR = "/";

    FSPath(FSDirectory parent, String name) {
        this.parent = parent;
        this.name = name;
    }

    FSPath(String path) throws Exception {
        int sepIndex = path.lastIndexOf(SEPARATOR);
        if (sepIndex != -1) {
            parent = new FSDirectory(path.substring(0, sepIndex));
            name = path.substring(sepIndex);
        } else
            throw new Exception("INVALID PATH.");
    }

    public FSDirectory getParent() {
        return parent;
    }

    @SerializedName("path")
    public String getAbsolutePath() {
        if (parent != null)
            return parent.getAbsolutePath()  + name;
        return SEPARATOR;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FSPath fsPath = (FSPath) o;
        return Objects.equals(parent, fsPath.parent) &&
                Objects.equals(name, fsPath.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(parent, name);
    }
}
