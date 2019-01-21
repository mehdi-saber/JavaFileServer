package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class SFileSystem implements SaveAble {
    private Set<FSPath> pathList;

    SFileSystem() {
        Set<FSPath> pathList = (Set<FSPath>) load();
        if (pathList != null)
            this.pathList = pathList;
        else
            this.pathList = new HashSet<>();
    }

    FSFile addFile(FSDirectory parent, String name, Long size, Set<Long> parts) {
        if (pathExists(parent, name, false) || !pathExists(parent))
            return null;
        FSFile file = new FSFile(parent, name,size, parts);
        pathList.add(file);
        return file;
    }

    FSDirectory addDirectory(FSDirectory parent, String name) {
        if (pathExists(parent, name, true) || !pathExists(parent))
            return null;
        FSDirectory directory = new FSDirectory(parent, name);
        pathList.add(directory);
        return directory;
    }

    Set<FSPath> listSubPaths(FSDirectory directory) {
        return pathList.stream()
                .filter(path -> path.getParent().equals(directory))
                .collect(Collectors.toSet());
    }

    private boolean pathExists(FSPath thePath) {
        if(thePath.equals(FSDirectory.ROOT))
            return true;
        return pathList.stream()
                .anyMatch(path -> path.equals(thePath));
    }

    private FSPath getPath(FSPath pathObject) {
        return pathList.stream()
                .filter(path -> path.equals(pathObject))
                .findFirst().get();
    }

    boolean pathExists(FSDirectory parent, String name, boolean isDirectory) {
        return pathList.stream()
                .anyMatch(
                        path -> path.getParent().equals(parent) &&
                                path.getName().equals(name) &&
                                ((isDirectory && path instanceof FSDirectory) || (!isDirectory && path instanceof FSFile))
                );
    }

    private boolean removePath(FSPath thePath) {
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

    boolean renamePath(FSPath thePath, String newName) {
        if (!pathExists(thePath) || pathExists(thePath.getParent(), newName, thePath instanceof FSDirectory))
            return false;
        getPath(thePath).setName(newName);
        return true;
    }

    boolean movePath(FSPath thePath, FSDirectory newDirectory) {
        if (!pathExists(thePath) || !pathExists(newDirectory) ||
                pathExists(newDirectory, thePath.getName(),thePath instanceof FSDirectory))
            return false;
        thePath.setParent(newDirectory);
        return true;
    }

    boolean copy(FSPath thePath, FSDirectory newDirectory) {
        if (!pathExists(thePath) || !pathExists(newDirectory) ||
                pathExists(newDirectory, thePath.getName(), thePath instanceof FSDirectory))
            return false;
        thePath.setParent(newDirectory);
        return true;
    }

    @Override
    public Object getSaveObject() {
        return pathList;
    }

    @Override
    public String getSaveFileName() {
        return "fileSystem";
    }
}
