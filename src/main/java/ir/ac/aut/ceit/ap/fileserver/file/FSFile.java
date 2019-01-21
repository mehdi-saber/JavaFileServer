package ir.ac.aut.ceit.ap.fileserver.file;


import java.util.Set;

public class FSFile extends FSPath {
    private Set<Long> parts;
    private Long size;
//    private List<FilePartitionInfo> partList;
//    private String ownerUsername;
//    private Timestamp createdDate;
//    private Timestamp lastAccessDate;
//    private String hashString;

    public FSFile(FSDirectory parent, String name, Long size, Set<Long> parts) {
        super(parent, name);
        this.parts = parts;
        this.size=size;
    }

    public String getExtension() {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1)
            return name.substring(dotIndex, name.length() - 1);
        return "";
    }

    public Set<Long> getParts() {
        return parts;
    }

    public Long getSize() {
        return size;
    }
}
