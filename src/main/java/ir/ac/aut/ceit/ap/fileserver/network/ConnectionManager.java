package ir.ac.aut.ceit.ap.fileserver.network;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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

    public Message sendRequest(Message request, String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            writeMessage(request, socket.getOutputStream());
            return readMessage(socket.getInputStream());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
                Message request = null;
                try {
                    request = readMessage(socket.getInputStream());
                    Message response = router.route(request,socket);
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

    private Message readMessage(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        Message message = (Message) objectInputStream.readObject();
        return message;
    }

    private void writeMessage(Message message, OutputStream outputStream) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(message);
    }
}
