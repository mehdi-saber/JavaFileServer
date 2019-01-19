package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FileSystem;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

class SFileSystem extends FileSystem {
    private static final File saveFile = new File("data" + File.separator + "pathList");

    SFileSystem() {
        super(loadPathList());
    }

    void savePathList() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(saveFile));
            objectOutputStream.writeObject(pathList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Set<FSPath> loadPathList() {
        Set<FSPath> pathList = null;
        if (saveFile.exists()) {
            try {

                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(saveFile));
                Object o = objectInputStream.readObject();
                pathList = (HashSet<FSPath>) o;
                objectInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (pathList != null)
            return pathList;
        return new HashSet<>();
    }
}
