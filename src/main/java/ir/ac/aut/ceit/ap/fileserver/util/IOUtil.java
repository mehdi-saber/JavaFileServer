package ir.ac.aut.ceit.ap.fileserver.util;

import ir.ac.aut.ceit.ap.fileserver.network.ProgressCallback;

import java.io.InputStream;
import java.io.OutputStream;

public class IOUtil {
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

    public static void writeI2O(OutputStream outputStream, InputStream inputStream) {
        writeI2O(outputStream, inputStream, 8 * 1024, Long.MAX_VALUE, null);
    }

    public static void writeI2O(OutputStream outputStream, InputStream inputStream, Long size) {
        writeI2O(outputStream, inputStream, 8 * 1024, size, null);
    }
}
