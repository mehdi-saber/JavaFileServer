package ir.ac.aut.ceit.ap.fileserver.network.receiver;

import ir.ac.aut.ceit.ap.fileserver.network.Transporter;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.StreamingSubject;
import ir.ac.aut.ceit.ap.fileserver.network.request.SendingMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver implements Transporter {
    private Router router;
    private boolean doReceive;
    private ServerSocket serverSocket;
    private Thread receiveThread;

    public Receiver(Router router) {
        this.doReceive = true;
        this.router = router;
    }

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        receiveThread = new Thread(() -> {
            try {
                while (doReceive) {
                    Socket socket = serverSocket.accept();
                    new Thread(() -> handleReceive(socket)).start();
                }
            } catch (IOException ignored) { }
        });
        receiveThread.start();
    }

    private void handleReceive(Socket socket) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();

            ReceivingMessage request = readMessage(outputStream, inputStream, socket.getInetAddress().getHostAddress());
            SendingMessage response = router.route(request);
            if (response == null)
                response = new SendingMessage(ResponseSubject.FAILED);
            outputStream.write((StreamingSubject.END + "\n").getBytes());
            writeMessage(response, outputStream);
            waitForStreamRequest(response, outputStream, inputStream);
            response.closeInputStreams();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            doReceive = false;
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
