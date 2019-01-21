package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;

import java.io.File;
import java.util.HashSet;

class SFileSystem extends FileSystem implements SaveAble {
    private static final String saveFileName = "SFileStorage";

    SFileSystem() {
        super((HashSet<FSPath>) SaveAble.loadFile(saveFileName));
    }

    @Override
    public Object getSaveObject() {
        return pathList;
    }

    @Override
    public String getSaveFileName() {
        return saveFileName;
    }

}
