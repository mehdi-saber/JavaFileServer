package ir.ac.aut.ceit.ap.fileserver.file;


import java.io.FileNotFoundException;
import java.util.List;

public class FSFile extends FSPath {
    private List<Long> parts;
//    private List<FilePartitionInfo> partList;
//    private Integer fileSize;
//    private String ownerUsername;
//    private Timestamp createdDate;
//    private Timestamp lastAccessDate;
//    private String hashString;

    public FSFile(FSDirectory parent, String name, List<Long> parts) {
        super(parent, name);
        this.parts = parts;
    }

    public FSFile(String path, List<Long> parts) throws FileNotFoundException {
        super(path);
        this.parts = parts;
    }

    public String getExtension() {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1)
            return name.substring(dotIndex, name.length() - 1);
        return "";
    }

    @Override
    public String getAbsolutePath() {
        return parent + SEPARATOR + name;
    }

    public List<Long> getParts() {
        return parts;
    }
}
