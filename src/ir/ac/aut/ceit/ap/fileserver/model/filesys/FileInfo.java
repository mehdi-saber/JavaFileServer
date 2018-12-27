package ir.ac.aut.ceit.ap.fileserver.model.filesys;


import ir.ac.aut.ceit.ap.fileserver.model.client.FilePartitionInfo;

import java.sql.Timestamp;
import java.util.List;

abstract public class FileInfo extends AddressInfo {
    private List<FilePartitionInfo> partList;
    private Integer fileSize;
    private String ownerUsername;
    private Timestamp createdDate;
    private Timestamp lastAccessDate;
    private String hashString;

    public String getExtention() {
        if (address.contains("."))
            return address.substring(address.lastIndexOf("."), address.length() - 1);
        else
            return "";
    }
}
