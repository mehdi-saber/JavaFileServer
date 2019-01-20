package ir.ac.aut.ceit.ap.fileserver.server;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import ir.ac.aut.ceit.ap.fileserver.file.FileStorage;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SFileStorage extends FileStorage {
    private Long idCounter;
    private int splitSize;


    private synchronized Long createId() {
        return idCounter++;
    }

    SFileStorage(Long idCounter) {
        super("data"+File.separator+"temp");
        this.idCounter = idCounter;
        this.splitSize = 3 * 1024 * 1024;
    }

    File getNewFile() {
        return getFileById(createId());
    }

    List<Long> splitFile(File file) {
        try {
            List<Long> filesId = new ArrayList<>();
            FileInputStream fileInputStream = new FileInputStream(file);

            byte[] buffer = new byte[splitSize];
            int len;
            Long remain = file.length();
            while (remain > 0 && (len = fileInputStream.read(buffer)) != -1) {
                ByteInputStream byteInputStream = new ByteInputStream(buffer, len);
                File partFile = getNewFile();
                IOUtil.writeI2O(
                        new FileOutputStream(partFile),
                        byteInputStream,
                        (long) len
                );
                remain -= len;
                filesId.add(Long.valueOf(partFile.getName()));
            }
            return filesId;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
