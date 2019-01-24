package ir.ac.aut.ceit.ap.fileserver.file;

import java.io.File;

/**
 * Manages making path for files of a class
 */
abstract public class FileStorage {
    private File directory;

    /**
     * Get a file path by ID
     *
     * @param id The relative ID
     * @return The file
     */
    public File getFileById(Long id) {
        return new File(directory + File.separator + id);
    }

    /**
     * Sets files parent directory
     *
     * @param directoryName The directory name
     */
    protected void setDirectory(String directoryName) {
        this.directory = new File(directoryName);
        this.directory.mkdirs();
    }
}
