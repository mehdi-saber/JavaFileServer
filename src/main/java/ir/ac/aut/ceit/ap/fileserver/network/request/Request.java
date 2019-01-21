package ir.ac.aut.ceit.ap.fileserver.network.request;

import ir.ac.aut.ceit.ap.fileserver.network.protocol.RequestSubject;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.StreamingSubject;
import ir.ac.aut.ceit.ap.fileserver.network.Transporter;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ReceivingMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

abstract public class Request extends SendingMessage implements Transporter {
    public Request(RequestSubject title) {
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
                outputStream.write((StreamingSubject.END + "\n").getBytes());
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        requestThread.start();
        return requestThread;
    }
}
