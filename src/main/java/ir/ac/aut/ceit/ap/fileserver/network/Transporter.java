package ir.ac.aut.ceit.ap.fileserver.network;

import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.StreamingSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public interface Transporter {
    default ReceivingMessage readMessage(Socket socket)
            throws ClassNotFoundException, IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        SendingMessage sendingMessage = (SendingMessage) objectInputStream.readObject();
        return new ReceivingMessage(sendingMessage, socket);
    }

    default void writeMessage(SendingMessage sendingMessage, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(sendingMessage);
    }

    default void waitForStreamRequest(SendingMessage request,
                                      OutputStream outputStream,
                                      InputStream inputStream) {
        while (true) {
            Scanner scanner = new Scanner(inputStream);
            StreamingSubject command = StreamingSubject.valueOf(scanner.nextLine());
            if (command.equals(StreamingSubject.END))
                break;
            else if (command.equals(StreamingSubject.SWITCH_TO_STREAM)) {
                String key = scanner.nextLine();
                ProgressCallback callback = request.getProgressCallback(key);
                InputStream messageInputStream = request.getStream(key);
                IOUtil.writeI2O(outputStream, messageInputStream, request.getStreamSize(key), callback);
            }
        }
    }
}
