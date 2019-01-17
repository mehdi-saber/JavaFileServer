package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ConnectionManager {
    private ServerSocket serverSocket;
    private Thread listenerThread;
    private int listenPort;
    private Router router;

    public ConnectionManager(int listenPort, Router router) {
        this.listenPort = listenPort;
        this.router = router;
        startListener();
    }

    public ReceivingMessage sendRequest(SendingMessage request, String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            writeMessage(request, socket.getOutputStream());
            ReceivingMessage response = readMessage(socket);
            new Thread(() -> waitForStreamRequest(request, socket)).start();
            return response;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    static void waitForStreamRequest(SendingMessage sendingMessage, Socket socket) {
        try {
            while (true) {
                Scanner scanner = new Scanner(socket.getInputStream());
                String key = scanner.nextLine();
                InputStream inputStream = sendingMessage.getStream(key);
                int b;
                while ((b = inputStream.read()) != -1) {
                    socket.getOutputStream().write(b);
                }
                socket.getOutputStream().write(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchElementException ignored) {
        }
    }

    public void startListener() {
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
                ReceivingMessage request = null;
                try {
                    request = readMessage(socket);
                    Message response = router.route(request);
                    writeMessage(response, socket.getOutputStream());
                } catch (IOException | ClassNotFoundException e) {
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

    private ReceivingMessage readMessage(Socket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
        SendingMessage sendingMessage = (SendingMessage) objectInputStream.readObject();
        return new ReceivingMessage(sendingMessage, sendingMessage.streams.keySet(), socket);
    }

    private void writeMessage(Message message, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(message);
    }
}
