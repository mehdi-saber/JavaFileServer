package ir.ac.aut.ceit.ap.fileserver.network;

import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.net.Socket;

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
            StreamsCommand command = StreamsCommand.valueOf(IOUtil.readLineNoBuffer(inputStream));
            if (command.equals(StreamsCommand.END_READING_STREAMS))
                break;
            else if (command.equals(StreamsCommand.GET_STREAM_BY_KEY)) {
                String key = IOUtil.readLineNoBuffer(inputStream);
                ProgressCallback callback = request.getProgressCallback(key);
                InputStream messageInputStream = request.getStream(key);
                IOUtil.writeI2O(outputStream, messageInputStream, request.getStreamSize(key), callback);
            }
        }
    }
}
