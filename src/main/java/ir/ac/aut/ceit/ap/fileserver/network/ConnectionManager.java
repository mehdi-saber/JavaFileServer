package ir.ac.aut.ceit.ap.fileserver.network;


import ir.ac.aut.ceit.ap.fileserver.util.IOUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionManager {
    private ServerSocket serverSocket;
    private Thread listenerThread;
    private int listenPort;
    private boolean doReceive = true;
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
                request.closeInputStreams();
                ResponseCallback responseCallback = request.getResponseCallback();
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

    private void waitForStreamRequest(SendingMessage request,
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
                IOUtil.writeI2O(outputStream, messageInputStream, 8 * 1024, request.getStreamSize(key), callback);
            }
        }
    }

    private void startListener() {
        try {
            serverSocket = new ServerSocket(listenPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(this::waitForConnection).start();
    }

    private void waitForConnection() {
        while (doReceive) {
            try {
                Socket socket = serverSocket.accept();
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
