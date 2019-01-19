package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class FileSystem {
    private Set<FSPath> pathList;

    FileSystem() {
        this.pathList = new HashSet<>();
    }

    boolean addPath(FSPath path) {
        if (pathExists(path) || pathIsNew(path.getParent()))
            return false;
        pathList.add(path);
        return true;
    }

    Set<FSPath> listSubPaths(FSDirectory directory) {
        return pathList.stream()
                .filter(path -> path.getParent().equals(directory))
                .collect(Collectors.toSet());
    }

    private boolean pathIsNew(FSPath thePath) {
        return pathList.stream()
                .anyMatch(path -> path.equals(thePath));
    }

    private boolean pathExists(FSPath thePath) {
        return !pathIsNew(thePath);
    }

    boolean removePath(FSPath thePath) {
        if (pathIsNew(thePath))
            return false;
        if (thePath instanceof FSDirectory) {
            FSDirectory directory = (FSDirectory) thePath;
            for (FSPath path : listSubPaths(directory))
                removePath(path);
        }
        pathList.remove(thePath);
        return true;
    }


    boolean movePath(FSPath thePath, FSDirectory newDirectory) {
        if (pathIsNew(thePath) || pathIsNew(newDirectory))
            return false;
        if (thePath instanceof FSDirectory) {
            FSDirectory directory = (FSDirectory) thePath;
            for (FSPath path : listSubPaths(directory))
                movePath(path, );
        }
        pathList.remove(thePath);
        pathList.add(newPath);
        return true;
    }
}
