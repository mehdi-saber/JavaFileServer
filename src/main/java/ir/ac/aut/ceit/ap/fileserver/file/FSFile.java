package ir.ac.aut.ceit.ap.fileserver.file;


public class FSFile extends FSPath {
//    private List<FilePartitionInfo> partList;
//    private Integer fileSize;
//    private String ownerUsername;
//    private Timestamp createdDate;
//    private Timestamp lastAccessDate;
//    private String hashString;

    FSFile(FSDirectory parent, String name) {
        super(parent, name);
    }

    FSFile(String path) throws Exception {
        super(path);
    }

    public String getExtension() {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1)
            return name.substring(dotIndex, name.length() - 1);
        return "";
    }
}
