package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

abstract public class Request extends SendingMessage implements Transporter {
    public Request(Subject title) {
        super(title);
    }

    public Thread send(String address, int port) {
        Thread requestThread = new Thread(() -> {
            try {
                Socket socket = new Socket(address, port);
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                writeMessage(this, outputStream);
                waitForStreamRequest(this, outputStream, inputStream);
                closeInputStreams();
                ReceivingMessage receivingMessage = readMessage(socket);
                if (responseCallback != null)
                    responseCallback.call(receivingMessage);
                outputStream.write((StreamsCommand.END_READING_STREAMS + "\n").getBytes());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        requestThread.start();
        return requestThread;
    }
}
