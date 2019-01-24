package ir.ac.aut.ceit.ap.fileserver.file;

import java.io.File;

abstract public class FileStorage {
    protected File directory;

    public File getFileById(Long id) {
        return new File(directory + File.separator + id);
    }

    public void setDirectory(String directoryName) {
        this.directory =  new File(directoryName);
        this.directory.mkdirs();
    }
}
