package ir.ac.aut.ceit.ap.fileserver.client;

import ir.ac.aut.ceit.ap.fileserver.file.FilePartInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

class FilePartManager {
    File directory;

    public FilePartManager(int listenPort) {
        directory = new File("data"+File.separator+"parts" + File.separator + listenPort);
        directory.mkdirs();
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
