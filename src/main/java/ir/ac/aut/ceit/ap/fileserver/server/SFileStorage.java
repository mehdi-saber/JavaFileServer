package ir.ac.aut.ceit.ap.fileserver.server;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import ir.ac.aut.ceit.ap.fileserver.file.FileStorage;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressWriter;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SFileStorage extends FileStorage implements SaveAble {
    private Long idCounter;
    private int splitSize;

    private synchronized Long createId() {
        return idCounter++;
    }

    SFileStorage(int splitSize) {
        super("data"+File.separator+"temp");
        Long idCounter = (Long) load();
        this.idCounter = idCounter == null ? 0 : idCounter;
        this.splitSize = splitSize;
    }

    File getNewFile() {
        return getFileById(createId());
    }

    List<Long> splitFile(File file, ProgressWriter out) {
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
                        (long) len,
                        out
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

    @Override
    public Object getSaveObject() {
        return idCounter;
    }

    @Override
    public String getSaveFileName() {
        return "fileStorage";
    }
}
