package ir.ac.aut.ceit.ap.fileserver.file;


import java.util.Date;
import java.util.Map;

public class FSFile extends FSPath {
    private final Map<Long, String> parts;
    private final Long size;
    private final String creator;
    private final Date createdDate;
    private Date lastAccessDate;
    private String hash;

    public FSFile(FSDirectory parent, String name, Long size, Map<Long, String> parts,
                  String creator, Date createdDate, String hash) {
        super(parent, name);
        this.parts = parts;
        this.size=size;
        this.creator = creator;
        this.createdDate = createdDate;
        this.hash = hash;
    }

    public String getExtension() {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1)
            return name.substring(dotIndex+1, name.length());
        return "";
    }


    public Long getSize() {
        return size;
    }

    public String getCreator() {
        return creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public Map<Long, String> getParts() {
        return parts;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public String getHash() {
        return hash;
    }
}
