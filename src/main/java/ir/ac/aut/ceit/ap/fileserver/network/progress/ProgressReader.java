package ir.ac.aut.ceit.ap.fileserver.network.progress;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Reads operation progress through input stream
 */
public class ProgressReader {
    private InputStream inputStream;
    private ProgressCallback callback;

    /**
     * Get new instance
     * @param inputStream The input stream
     * @param callback The progress callback
     */
    public ProgressReader(InputStream inputStream, ProgressCallback callback) {
        this.inputStream = inputStream;
        this.callback = callback;
    }

    /**
     * Start reading
     * @return Readign thread
     */
    public Thread start() {
        Thread thread = new Thread(() -> {
            Scanner scanner = new Scanner(inputStream);
            while (true) {
                try {
                    ProgressSubject subject = ProgressSubject.valueOf(scanner.nextLine());
                    if (subject.equals(ProgressSubject.END))
                        break;
                    else if (subject.equals(ProgressSubject.CALL_BACK))
                        callback.call(Integer.valueOf(scanner.nextLine()));
                } catch (NoSuchElementException e) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return thread;
    }
}
