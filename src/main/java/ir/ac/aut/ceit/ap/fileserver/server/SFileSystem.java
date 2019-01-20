package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;

import java.io.File;
import java.util.HashSet;

class SFileSystem extends FileSystem implements SaveAble {
    private static final File saveFile = new File("data" + File.separator + "pathList");

    SFileSystem() {
        super((HashSet<FSPath>) SaveAble.loadFile(saveFile));
    }

    @Override
    public Object getSaveObject() {
        return pathList;
    }

    @Override
    public File getSaveFile() {
        return saveFile;
    }
}
