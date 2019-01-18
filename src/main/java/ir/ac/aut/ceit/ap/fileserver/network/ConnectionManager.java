package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    public static final String END_OF_REQUEST_STREAM = "END_OF_REQUEST_STREAM";
    private ServerSocket serverSocket;
    private Thread listenerThread;
    private int listenPort;
    private int bufferSize = 1024 * 1024;
    private Router router;

    public ConnectionManager(int listenPort, Router router) {
        this.listenPort = listenPort;
        this.router = router;
        startListener();
    }

    public Thread sendRequest(SendingMessage request, String address, int port) {
        Thread requestThread = new Thread(() -> {
            try {
                Socket socket = new Socket(address, port);
                OutputStream outputStream = socket.getOutputStream();
                InputStream inputStream = socket.getInputStream();
                writeMessage(request, outputStream);
                waitForStreamRequest(request, outputStream, inputStream);
                request.getResponseCallback().call(readMessage(socket));
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        requestThread.start();
        return requestThread;
    }

    private void waitForStreamRequest(SendingMessage request,
                                      OutputStream outputStream,
                                      InputStream inputStream) {
        try {
            while (true) {
                StringBuilder keyBuilder = new StringBuilder();
                int c;
                while ((c = inputStream.read()) != -1 && c != '\n')
                    keyBuilder.append((char) c);
                if (keyBuilder.toString().equals(END_OF_REQUEST_STREAM))
                    break;
                String key = keyBuilder.toString();
                ProgressCallback callback = request.getProgressCallback(key);
                BufferedInputStream bufferedInputStream = new BufferedInputStream(request.getStream(key));
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                byte[] buffer = new byte[bufferSize];
                int numBytes;
                while ((numBytes = bufferedInputStream.read(buffer)) != -1) {
                    if (callback != null)
                        callback.call(bufferSize);
                    bufferedOutputStream.write(buffer, 0, numBytes);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startListener() {
        try {
            serverSocket = new ServerSocket(listenPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        listenerThread = new Thread(() -> {
            try {
                waitForConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.start();
    }

    private void waitForConnection() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            Thread requestThread = new Thread(() -> {
                ReceivingMessage request;
                try {
                    OutputStream outputStream = socket.getOutputStream();
                    request = readMessage(socket);
                    SendingMessage response = router.route(request);
                    outputStream.write((END_OF_REQUEST_STREAM + "\n").getBytes());
                    writeMessage(response, outputStream);
                } catch (IOException | ClassNotFoundException e ) {
                    e.printStackTrace();
                }
            });
            requestThread.start();
        }
    }

    private void stopListener() {
        if (listenerThread != null && listenerThread.isAlive())
            listenerThread.interrupt();
    }

    private ReceivingMessage readMessage(Socket socket)
            throws ClassNotFoundException, IOException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        SendingMessage sendingMessage = (SendingMessage) objectInputStream.readObject();
        return new ReceivingMessage(sendingMessage, socket);
    }

    private void writeMessage(SendingMessage sendingMessage, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(sendingMessage);
    }
}
