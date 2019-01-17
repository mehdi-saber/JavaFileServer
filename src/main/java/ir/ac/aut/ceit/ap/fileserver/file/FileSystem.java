package ir.ac.aut.ceit.ap.fileserver.file;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileSystem {
    private List<FSPath> pathList;

    public FileSystem() {
        this.pathList = new ArrayList<>();
    }

    public FSFile addFile(FSDirectory parent, String name) {
        FSFile file = new FSFile(parent, name);
        pathList.add(file);
        return file;
    }

    public FSDirectory addDirectory(FSDirectory parent, String name) {
        FSDirectory directory = new FSDirectory(parent, name);
        pathList.add(directory);
        return directory;
    }

    public List<FSPath> listSubPaths(FSDirectory directory) {
        return pathList.stream()
                .filter(path -> path.getParent().equals(directory))
                .collect(Collectors.toList());
    }

    public FSPath getPath(String pathStr) {
        for (FSPath path : pathList)
            if (path instanceof FSDirectory)
                if (pathStr.equals(path.getAbsolutePath()))
                    return path;
        return null;
    }
}
