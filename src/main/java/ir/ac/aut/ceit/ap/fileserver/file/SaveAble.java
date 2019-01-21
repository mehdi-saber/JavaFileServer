package ir.ac.aut.ceit.ap.fileserver.file;

import java.io.*;

public interface SaveAble {
    Object getSaveObject();

    String getSaveFileName();

    default void save() {
        try {
            File saveFile = getSaveFileByName(getSaveFileName());
            FileOutputStream fileOutputStream = new FileOutputStream(saveFile);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(getSaveObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static File getSaveFileByName(String fileName) {
        return new File("data" + File.separator + fileName);
    }

    default Object load() {
        return loadFile(getSaveFileName());
    }

    static Object loadFile(String fileName) {
        File saveFile = getSaveFileByName(fileName);
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
