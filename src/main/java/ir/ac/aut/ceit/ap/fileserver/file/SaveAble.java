package ir.ac.aut.ceit.ap.fileserver.file;

import java.io.*;

/**
 * The object data can be stored and load from file
 */
public interface SaveAble {
    /**
     * @return data to save
     */
    Object getSaveObject();

    /**
     * @return save file name
     */
    String getSaveFileName();

    /**
     * save to file
     */
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

    /**
     * get save file address by file name
     *
     * @param fileName file name
     * @return stored data
     */
    static File getSaveFileByName(String fileName) {
        return new File("data" + File.separator + fileName);
    }

    /**
     * loads from file
     *
     * @return stored data
     */
    default Object load() {
        return loadFile(getSaveFileName());
    }

    /**
     * loads from file
     *
     * @param fileName save file name
     * @return stored data
     */
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
