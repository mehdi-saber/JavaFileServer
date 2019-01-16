package ir.ac.aut.ceit.ap.fileserver.filesys;


public class FileInfo extends PathInfo {
//    private List<FilePartitionInfo> partList;
//    private Integer fileSize;
//    private String ownerUsername;
//    private Timestamp createdDate;
//    private Timestamp lastAccessDate;
//    private String hashString;


    public FileInfo(String address) {
        super(address);
    }

    public String getExtension() {
        if (address.contains("."))
            return address.substring(address.lastIndexOf("."), address.length() - 1);
        else
            return "";
    }
}
