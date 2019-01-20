package ir.ac.aut.ceit.ap.fileserver.util;

import ir.ac.aut.ceit.ap.fileserver.network.ProgressCallback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
    public static final int defaultBufferSize = 16 * 1024;
    public static void writeI2O(OutputStream outputStream, InputStream inputStream,
                                int bufferSize, Long size, ProgressCallback callback) {
        try {
            byte[] buffer = new byte[bufferSize];
            int len;
            while (size > 0 && (len = inputStream.read(buffer)) != -1) {
                if (callback != null)
                    callback.call(len);
                size -= len;
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeI2O(OutputStream outputStream, InputStream inputStream, Long size) {
        writeI2O(outputStream, inputStream, defaultBufferSize, size, null);
    }

    public static void writeI2O(OutputStream outputStream, InputStream inputStream, Long size,ProgressCallback progressCallback) {
        writeI2O(outputStream, inputStream, defaultBufferSize, size, progressCallback);
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
