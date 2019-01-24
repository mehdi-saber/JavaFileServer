package ir.ac.aut.ceit.ap.fileserver.network;

import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.StreamingSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;
import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;

/**
 * transports message
 */
public interface Transporter {
    /**
     * read a message
     *
     * @param outputStream  pass to receiving for stream
     * @param inputStream   read object
     * @param senderAddress pass to receiving for stream
     * @return receiving stream
     * @throws ClassNotFoundException if reads unknown object
     * @throws IOException            if object corrupted
     */
    default ReceivingMessage readMessage(OutputStream outputStream, InputStream inputStream, String senderAddress)
            throws ClassNotFoundException, IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        SendingMessage sendingMessage = (SendingMessage) objectInputStream.readObject();
        return new ReceivingMessage(sendingMessage, outputStream, inputStream, senderAddress);
    }

    /**
     * writes a message
     *
     * @param sendingMessage message to write
     * @param outputStream   stream to write in
     * @throws IOException if object corrupted
     */
    default void writeMessage(SendingMessage sendingMessage, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(sendingMessage);
    }

    /**
     * doing stream process
     *
     * @param sendingMessage message to stream
     * @param outputStream   to write streams
     * @param inputStream    to read commands
     */
    default void waitForStreamRequest(SendingMessage sendingMessage,
                                      OutputStream outputStream,
                                      InputStream inputStream) {
        while (true) {
            StreamingSubject command = StreamingSubject.valueOf(IOUtil.readLineNoBuffer(inputStream));
            if (command.equals(StreamingSubject.END))
                break;
            else if (command.equals(StreamingSubject.SWITCH_TO_STREAM)) {
                String key = IOUtil.readLineNoBuffer(inputStream);
                ProgressCallback callback = sendingMessage.getProgressCallback(key);
                InputStream messageInputStream = sendingMessage.getStream(key);
                IOUtil.writeI2O(outputStream, messageInputStream, sendingMessage.getStreamSize(key), callback);
            }
        }
    }
}
