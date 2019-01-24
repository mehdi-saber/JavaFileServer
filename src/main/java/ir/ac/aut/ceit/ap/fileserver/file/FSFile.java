package ir.ac.aut.ceit.ap.fileserver.file;


import java.util.Date;
import java.util.Map;

/**
 * Keeps file system files info
 */
public class FSFile extends FSPath {
    private final Map<Long, String> parts;
    private final Long size;
    private final String creator;
    private final Date createdDate;
    private Date lastAccessDate;
    private String hash;

    /**
     * Creates new file
     *
     * @param parent      The parent directory
     * @param name        The file name
     * @param size        The file size
     * @param parts       The file parts hashes
     * @param creator     The file uploader
     * @param createdDate The upload time
     * @param hash        The file hash
     */
    public FSFile(FSDirectory parent, String name, Long size, Map<Long, String> parts,
                  String creator, Date createdDate, String hash) {
        super(parent, name);
        this.parts = parts;
        this.size=size;
        this.creator = creator;
        this.createdDate = createdDate;
        this.hash = hash;
    }

    /**
     *
     * @return File extention
     */
    public String getExtension() {
        int dotIndex = name.lastIndexOf(".");
        if (dotIndex != -1)
            return name.substring(dotIndex+1, name.length());
        return "";
    }


    /**
     *
     * @return File size
     */
    public Long getSize() {
        return size;
    }

    /**
     *
     * @return file uploader
     */
    public String getCreator() {
        return creator;
    }

    /**
     *
     * @return upload date
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @return last access date
     */
    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    /**
     *
     * @return parts hashes
     */
    public Map<Long, String> getParts() {
        return parts;
    }

    /**
     * Sets last access date
     * @param lastAccessDate the last access time
     */
    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    /**
     *
     * @return file hash
     */
    public String getHash() {
        return hash;
    }
}
