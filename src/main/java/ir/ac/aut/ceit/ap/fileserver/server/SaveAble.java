package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.*;

public interface SaveAble {
    Object getSaveObject();

    File getSaveFile();

    default void save() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(getSaveFile()));
            objectOutputStream.writeObject(getSaveObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    default Object load() {
        return loadFile(getSaveFile());
    }

    static Object loadFile(File saveFile) {
        if (saveFile.exists()) {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(saveFile));
                return objectInputStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
