package ir.ac.aut.ceit.ap.fileserver.server;

import ir.ac.aut.ceit.ap.fileserver.file.FileStorage;
import ir.ac.aut.ceit.ap.fileserver.file.SaveAble;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

class SFileStorage extends FileStorage implements SaveAble {
    private Long idCounter;
    private int splitSize;

    private synchronized Long createId() {
        return idCounter++;
    }

    SFileStorage() {
        super("data"+File.separator+"temp");
        Long idCounter = (Long) load();
        this.idCounter = idCounter == null ? 0 : idCounter;
    }

    File getNewFile() {
        return getFileById(createId());
    }

    /**
     * Splits file to different parts.
     *
     * @param file             The file we want to split
     * @param progressCallback The progress Call back
     * @return A map which consists of the part ID and it's MD5 hash
     */
    Map<Long, String> splitFile(File file, ProgressCallback progressCallback) {
        try {
            Map<Long, String> partsHash = new HashMap<>();
            FileInputStream theFileInputStream = new FileInputStream(file);
            //This DigestInputStream will calculates the part MD5 hash during streaming the file
            DigestInputStream inputStream =
                    new DigestInputStream(theFileInputStream, MessageDigest.getInstance("MD5"));

            int bufferSize = IOUtil.DEFAULT_BUFFER_SIZE;
            byte[] buffer = new byte[bufferSize];
            int len, remain = 0;
            long fileRemain = file.length();
            FileOutputStream fileOutputStream = null;
            File partFile = null;
            //reads the file
            while (true) {
                len = inputStream.read(buffer, 0, remain < bufferSize ? remain : bufferSize);
                if (remain == 0) {
                    //end of recent part
                    if (fileOutputStream != null) {
                        MessageDigest digest = inputStream.getMessageDigest();
                        String hash = IOUtil.printHexBinary(digest.digest());
                        partsHash.put(Long.valueOf(partFile.getName()), hash);
                        digest.reset();
                        fileOutputStream.close();
                    }
                    if (fileRemain == 0)
                        break;
                    //start new part
                    partFile = getNewFile();
                    fileOutputStream = new FileOutputStream(partFile);
                    remain = fileRemain < splitSize ? (int) fileRemain : splitSize;
                }
                remain -= len;
                fileRemain -= len;
                fileOutputStream.write(buffer, 0, len);
                progressCallback.call(len);
            }
            return partsHash;
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setSplitSize(int splitSize) {
        this.splitSize = splitSize;
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
