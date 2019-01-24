package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles server behaviors based on folder system
 */
class SFileSystem implements SaveAble {
    private List<FSPath> pathSet;

    /**
     * Construct new file system for server
     */
    SFileSystem() {
        List<FSPath> pathList = (List<FSPath>) load();
        if (pathList != null)
            this.pathSet = pathList;
        else
            this.pathSet = new ArrayList<>();
    }


    /**
     * Adds new file to path's list
     *
     * @param parent         The file parent directory
     * @param name           The file name
     * @param size           The file
     * @param parts          The file parts hashes
     * @param creator        The username who uploads file to system
     * @param createdDate    The upload time
     * @param lastAccessDate The Last access time
     * @param hash           THe file hash
     * @return The file
     */
    FSFile addFile(FSDirectory parent, String name, Long size, Map<Long, String> parts,
                   String creator, Date createdDate, Date lastAccessDate, String hash) {
        if (pathExists(parent, name, false) || !pathExists(parent))
            return null;
        FSFile file = new FSFile(parent, name, size, parts, creator, createdDate,hash);
        file.setLastAccessDate(lastAccessDate);
        pathSet.add(file);
        return file;
    }

    /**
     * Adds a directory to path's list
     *
     * @param parent The directory Parent directory
     * @param name   The directory name
     * @return The directory
     */
    FSDirectory addDirectory(FSDirectory parent, String name) {
        if (pathExists(parent, name, true) || !pathExists(parent))
            return null;
        FSDirectory directory = new FSDirectory(parent, name);
        pathSet.add(directory);
        return directory;
    }

    /**
     * Lists The directory sub paths
     *
     * @param directory The directory
     * @return Sub paths
     */
    Set<FSPath> listSubPaths(FSDirectory directory) {
        return pathSet.stream()
                .filter(path -> path.getParent().equals(directory))
                .collect(Collectors.toSet());
    }

    /**
     * List the directory sub path and their sub paths
     *
     * @param directory The directory
     * @return The sub paths
     */
    Set<FSPath> listRecurSubPaths(FSDirectory directory) {
        return pathSet.stream()
                .filter(path -> isInsideDirectory(directory, path))
                .collect(Collectors.toSet());
    }

    /**
     * Checks if a path is inside a directory
     *
     * @param directory The directory
     * @param path      The path
     * @return True if is inside or false
     */
    boolean isInsideDirectory(FSDirectory directory, FSPath path) {
        return path.getAbsolutePath().startsWith(directory.getAbsolutePath());
    }

    /**
     * Checks if a path exists in the path list
     *
     * @param thePath The path
     * @return True if path exists or false
     */
    boolean pathExists(FSPath thePath) {
        return pathExists(thePath.getParent(), thePath.getName(), thePath instanceof FSDirectory);
    }

    /**
     * Checks if a path exists in the path list
     *
     * @param parent      The path parent
     * @param name        The path name
     * @param isDirectory Whether path is Directory or not
     * @return true if path exist or false
     */
    boolean pathExists(FSDirectory parent, String name, boolean isDirectory) {
        return getPath(parent, name, isDirectory) != null;
    }

    /**
     * Get an equal path form path list
     *
     * @param path A path object
     * @return A path Object in the list
     */
    FSPath getPath(FSPath path) {
        return getPath(path.getParent(), path.getName(), path instanceof FSDirectory);
    }

    /**
     * Updates the hash map hash and parents memory references
     */
    void updateReferences() {
        for (FSPath path : pathSet)
            path.setParent((FSDirectory) getPath(path.getParent()));
        pathSet = new ArrayList<>(pathSet);
    }

    /**
     * Finds a path from path list by following info
     * @param parent The path parent
     * @param name The path name
     * @param isDirectory Whether the path is directory or not
     * @return A path from path list
     */
    FSPath getPath(FSDirectory parent, String name, boolean isDirectory) {
        if (parent == FSDirectory.ROOT.getParent() && name.equals(FSDirectory.ROOT.getName()))
            return FSDirectory.ROOT;
        return pathSet.stream()
                .filter(path -> path.getParent().equals(parent) &&
                        path.getName().equals(name) &&
                        ((isDirectory && path instanceof FSDirectory) || (!isDirectory && path instanceof FSFile))
                ).findFirst().orElse(null);
    }

    /**
     * Removes a path and it's sub path if exists from foldering system
     * @param path The path
     * @return path and it's deleting sub paths
     */
    Set<FSFile> remove(FSPath path) {
        if (!pathExists(path))
            return null;
        Set<FSFile> deletingFiles = new HashSet<>();

        if (path instanceof FSDirectory) {
            FSDirectory directory = (FSDirectory) path;
            for (FSPath subPath : listSubPaths(directory))
                remove(subPath);
        }

        updateReferences();
        pathSet.remove(path);

        if (path instanceof FSFile)
            deletingFiles.add((FSFile) path);
        return deletingFiles;
    }

    /**
     * renames a path
     * @param thePath The path
     * @param newName The new name
     * @return true if success or false
     */
    boolean renamePath(FSPath thePath, String newName) {
        if (!pathExists(thePath) || pathExists(thePath.getParent(), newName, thePath instanceof FSDirectory))
            return false;
        updateReferences();
        getPath(thePath).setName(newName);
        return true;
    }

    /**
     * Moves a path to a new directory
     * @param path The path
     * @param newDirectory The new Directory
     * @return whether the operation was successful or not
     */
    boolean move(FSPath path, FSDirectory newDirectory) {
        if (!pathExists(path) || !pathExists(newDirectory) ||
                pathExists(newDirectory, path.getName(), path instanceof FSDirectory))
            return false;
        updateReferences();
        getPath(path).setParent(newDirectory);
        return true;
    }

    /**
     * Copies a path to a new directory
     * @param path The path
     * @param newDirectory The new Directory
     * @return whether the operation was successful or not
     */
    boolean copy(FSPath path, FSDirectory newDirectory) {
        if (!pathExists(path) || !pathExists(newDirectory) ||
                pathExists(newDirectory, path.getName(), path instanceof FSDirectory))
            return false;
        FSPath newPath = clonePath(newDirectory, path);
        if (path instanceof FSDirectory)
            for (FSPath subPath : listSubPaths((FSDirectory) path))
                copy(subPath, (FSDirectory) newPath);
        return true;
    }

    private FSPath clonePath(FSDirectory parent, FSPath path) {
        if (path instanceof FSDirectory)
            return addDirectory(parent, path.getName());
        else if (path instanceof FSFile) {
            FSFile file = (FSFile) path;
            return addFile(parent, file.getName(), file.getSize(), file.getParts(),
                    file.getCreator(), file.getCreatedDate(), file.getLastAccessDate(),file.getHash());
        }
        return null;
    }

    /**
     *
     * @return The path set
     */
    List<FSPath> getPathSet() {
        return pathSet;
    }

    /**
     * Represents the saving data
     * @return The data
     */
    @Override
    public Object getSaveObject() {
        return pathSet;
    }

    /**
     * Represents the save file name
     * @return The file name
     */
    @Override
    public String getSaveFileName() {
        return "fileSystem";
    }
}
