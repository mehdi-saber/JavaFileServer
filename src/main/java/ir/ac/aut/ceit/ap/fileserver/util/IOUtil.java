package ir.ac.aut.ceit.ap.fileserver.util;

import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
    public static final int DEFAULT_BUFFER_SIZE = 16 * 1024;

    /**
     * Writes the InputStream to OutputStream
     *
     * @param outputStream The OutputStream
     * @param inputStream  The InputStream
     * @param bufferSize   The buffer remain
     * @param remain       The remain of the InputStream bytes
     * @param callback     We call it when we write buffer to the OutputStream
     */
    private static void writeI2O(OutputStream outputStream, InputStream inputStream,
                                 int bufferSize, Long remain, ProgressCallback callback) {
        try {
            byte[] buffer = new byte[bufferSize];
            //The length of bytes The InputStream gives when we call read
            int len;
            while (remain > 0 && (len = inputStream.read(buffer)) != -1) {
                if (callback != null)
                    callback.call(len);
                remain -= len;
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls writeI2O with default buffer size and no callback
     */
    public static void writeI2O(OutputStream outputStream, InputStream inputStream, Long size) {
        writeI2O(outputStream, inputStream, DEFAULT_BUFFER_SIZE, size, null);
    }

    /**
     * Calls writeI2O with default buffer size
     */
    public static void writeI2O(OutputStream outputStream, InputStream inputStream, Long size,ProgressCallback progressCallback) {
        writeI2O(outputStream, inputStream, DEFAULT_BUFFER_SIZE, size, progressCallback);
    }

    public static String readLineNoBuffer(InputStream inputStream) {
        try {
            StringBuilder keyBuilder = new StringBuilder();
            int c;
            while ((c = inputStream.read()) != -1 && c != '\n')
                keyBuilder.append((char) c);
            return keyBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
