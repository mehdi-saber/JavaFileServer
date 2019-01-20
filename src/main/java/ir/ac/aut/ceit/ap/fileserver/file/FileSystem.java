package ir.ac.aut.ceit.ap.fileserver.file;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

abstract public class FileSystem {
    protected Set<FSPath> pathList;

    public FileSystem(Set<FSPath> pathList) {
        if (pathList != null)
            this.pathList = pathList;
        else
            this.pathList = new HashSet<>();
    }

    public FSFile addFile(FSDirectory parent, String name,Long size, Set<Long> parts) {
        if (pathExists(parent, name, false) || !pathExists(parent))
            return null;
        FSFile file = new FSFile(parent, name,size, parts);
        pathList.add(file);
        return file;
    }

    public FSDirectory addDirectory(FSDirectory parent, String name) {
        if (pathExists(parent, name, true) || !pathExists(parent))
            return null;
        FSDirectory directory = new FSDirectory(parent, name);
        pathList.add(directory);
        return directory;
    }

    public Set<FSPath> listSubPaths(FSDirectory directory) {
        return pathList.stream()
                .filter(path -> path.getParent().equals(directory))
                .collect(Collectors.toSet());
    }

    public boolean pathExists(FSPath thePath) {
        if(thePath.equals(FSDirectory.ROOT))
            return true;
        return pathList.stream()
                .anyMatch(path -> path.equals(thePath));
    }

    public boolean pathExists(FSDirectory parent, String name, boolean isDirectory) {
        return pathList.stream()
                .anyMatch(
                        path -> path.getParent().equals(parent) &&
                                path.getName().equals(name) &&
                                ((isDirectory && path instanceof FSDirectory) || (!isDirectory && path instanceof FSFile))
                );
    }

    public boolean removePath(FSPath thePath) {
        if (!pathExists(thePath))
            return false;
        if (thePath instanceof FSDirectory) {
            FSDirectory directory = (FSDirectory) thePath;
            for (FSPath path : listSubPaths(directory))
                removePath(path);
        }
        pathList.remove(thePath);
        return true;
    }

    public boolean renamePath(FSPath thePath, String newName) {
        if (!pathExists(thePath) || pathExists(thePath.getParent(), newName, thePath instanceof FSDirectory))
            return false;
        thePath.setName(newName);
        return true;
    }

    public boolean movePath(FSPath thePath, FSDirectory newDirectory) {
        if (!pathExists(thePath) || !pathExists(newDirectory) ||
                pathExists(newDirectory, thePath.getName(),thePath instanceof FSDirectory))
            return false;
        thePath.setParent(newDirectory);
        return true;
    }
}
