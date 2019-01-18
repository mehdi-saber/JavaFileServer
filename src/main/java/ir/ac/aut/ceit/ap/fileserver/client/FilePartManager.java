package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.file.FilePartInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

class FilePartManager {
    File directory;

    public FilePartManager() {
        directory = new File("parts" + File.separator + new Random().nextInt() + File.separator);
        if (!directory.mkdirs())
            try {
                throw new Exception("can not create folder.");
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public OutputStream storePartOutputStream(FilePartInfo partInfo) {
        String name = partInfo.getHash();
        try {
            return new FileOutputStream(new File(directory+File.separator + name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadPart(FilePartInfo part) {
        return null;
    }

    public List<String> listPartHashes() {
        return null;
    }
}
