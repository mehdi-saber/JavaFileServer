package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProgressReader extends Thread {

    public ProgressReader(InputStream inputStream, ProgressCallback callback) {
        super(() -> callback(inputStream, callback));
        start();
    }

    private static void callback(InputStream inputStream, ProgressCallback callback) {
        Scanner scanner = new Scanner(inputStream);
        while (true) {
            try {
                StreamsCommand command = StreamsCommand.valueOf(scanner.nextLine());
                if (command.equals(StreamsCommand.PROGRESS_END))
                    break;
                else if (command.equals(StreamsCommand.PROGRESS_PASSED))
                    callback.call(Integer.valueOf(scanner.nextLine()));
            } catch (NoSuchElementException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
