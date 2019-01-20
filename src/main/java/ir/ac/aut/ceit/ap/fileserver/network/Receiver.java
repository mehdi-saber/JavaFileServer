package ir.ac.aut.ceit.ap.fileserver.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver extends ServerSocket implements Transporter {
    private Router router;
    private int listenPort;
    private Thread mainLoopThread;
    private boolean doReceive;

    public Receiver(int port, Router router) throws IOException {
        super(port);
        this.doReceive = true;
        this.router = router;
        this.mainLoopThread = new Thread(this::waitForConnection);
        this.mainLoopThread.start();
    }

    private void waitForConnection() {
        while (doReceive) {
            try {
                Socket socket = accept();
                new Thread(() -> handleReceive(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleReceive(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ReceivingMessage request = readMessage(socket);
            SendingMessage response = router.route(request);
            outputStream.write((StreamsCommand.END_READING_STREAMS + "\n").getBytes());
            writeMessage(response, outputStream);
            waitForStreamRequest(response, outputStream, inputStream);
            response.closeInputStreams();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void stopListener() {
        doReceive = false;
    }

    public Thread getMainLoopThread() {
        return mainLoopThread;
    }
}
