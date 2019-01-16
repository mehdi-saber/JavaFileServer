package ir.ac.aut.ceit.ap.fileserver.filesys;


import java.sql.Timestamp;

public class DirectoryInfo extends PathInfo {
    private String ownerName;
    private Timestamp createdDate;
    private Timestamp lastAccessDate;

    public DirectoryInfo(String address) {
        super(address);
    }
}
