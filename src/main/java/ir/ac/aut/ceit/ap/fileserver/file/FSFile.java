package ir.ac.aut.ceit.ap.fileserver.file;


import java.util.List;

public class FSFile extends FSPath {
    private List<Long> parts;
    private Long size;
//    private List<FilePartitionInfo> partList;
//    private String ownerUsername;
//    private Timestamp createdDate;
//    private Timestamp lastAccessDate;
//    private String hashString;

    FSFile(FSDirectory parent, String name, List<Long> parts) {
        super(parent, name);
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

    public Long getSize() {
        return size;
    }
}
