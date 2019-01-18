package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class FileIdentityManager {
    private File directory;
    private Long idCounter;

    private synchronized Long createId() {
        return idCounter++;
    }

    FileIdentityManager(String directoryName, Long idCounter) {
        this.directory = new File("data/server/" + directoryName);
        this.directory.mkdirs();
        this.idCounter = idCounter;
    }

    File getFileById(Long fileId) {
        return new File(directory + File.separator + fileId);
    }

    Long storeFile(InputStream inputStream, Long fileSize) {
        Long fileId = createId();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(getFileById(fileId));
            byte[] buffer = new byte[8 * 1024];
            int len;
            while (fileSize>0&&(len = inputStream.read(buffer)) !=-1) {
                fileOutputStream.write(buffer, 0, len);
                fileSize-=len;
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileId;
    }


}
