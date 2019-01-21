package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class SFileSystem implements SaveAble {
    private Set<FSPath> pathSet;

    SFileSystem() {
        Set<FSPath> pathList = (Set<FSPath>) load();
        if (pathList != null)
            this.pathSet = pathList;
        else
            this.pathSet = new HashSet<>();
    }


    FSFile addFile(FSDirectory parent, String name, Long size, Set<Long> parts) {
        if (pathExists(parent, name, false) || !pathExists(parent))
            return null;
        FSFile file = new FSFile(parent, name,size, parts);
        pathSet.add(file);
        return file;
    }

    FSDirectory addDirectory(FSDirectory parent, String name) {
        if (pathExists(parent, name, true) || !pathExists(parent))
            return null;
        FSDirectory directory = new FSDirectory(parent, name);
        pathSet.add(directory);
        return directory;
    }

    Set<FSPath> listSubPaths(FSDirectory directory) {
        return pathSet.stream()
                .filter(path -> path.getParent().equals(directory))
                .collect(Collectors.toSet());
    }

    Set<FSPath> listRecurSubPaths(FSDirectory directory) {
        return pathSet.stream()
                .filter(path -> isInsideDirectory(directory, path))
                .collect(Collectors.toSet());
    }

    boolean isInsideDirectory(FSDirectory directory, FSPath path) {
        return path.getAbsolutePath().startsWith(directory.getAbsolutePath());
    }

    boolean pathExists(FSPath thePath) {
        return pathExists(thePath.getParent(), thePath.getName(), thePath instanceof FSDirectory);
    }


    boolean pathExists(FSDirectory parent, String name, boolean isDirectory) {
        return getPath(parent, name, isDirectory) != null;
    }

    FSPath getPath(FSPath path) {
        return getPath(path.getParent(), path.getName(), path instanceof FSDirectory);
    }

    void updateRefrences() {
        for (FSPath path : pathSet)
            path.setParent((FSDirectory) getPath(path.getParent()));
    }

    FSPath getPath(FSDirectory parent, String name, boolean isDirectory) {
        if (parent == FSDirectory.ROOT.getParent() && name.equals(FSDirectory.ROOT.getName()))
            return FSDirectory.ROOT;
        return pathSet.stream()
                .filter(path -> path.getParent().equals(parent) &&
                        path.getName().equals(name) &&
                        ((isDirectory && path instanceof FSDirectory) || (!isDirectory && path instanceof FSFile))
                ).findFirst().orElse(null);
    }

    Set<FSFile> remove(FSPath thePath) {
        if (!pathExists(thePath))
            return null;
        Set<FSFile> deletingFiles = new HashSet<>();
        if (thePath instanceof FSDirectory) {
            FSDirectory directory = (FSDirectory) thePath;
            for (FSPath path : listSubPaths(directory))
                remove(path);
        }
        pathSet.remove(thePath);
        if (thePath instanceof FSFile)
            deletingFiles.add((FSFile) thePath);
        return deletingFiles;
    }

    boolean renamePath(FSPath thePath, String newName) {
        if (!pathExists(thePath) || pathExists(thePath.getParent(), newName, thePath instanceof FSDirectory))
            return false;
        getPath(thePath).setName(newName);
        return true;
    }

    boolean move(FSPath path, FSDirectory newDirectory) {
        if (!pathExists(path) || !pathExists(newDirectory) ||
                pathExists(newDirectory, path.getName(), path instanceof FSDirectory))
            return false;
        updateRefrences();
        getPath(path).setParent(newDirectory);
        return true;
    }

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
            return addFile(parent, file.getName(), file.getSize(), file.getParts());
        }
        return null;
    }

    public Set<FSPath> getPathSet() {
        return pathSet;
    }

    @Override
    public Object getSaveObject() {
        return pathSet;
    }

    @Override
    public String getSaveFileName() {
        return "fileSystem";
    }
}
