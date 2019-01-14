package ir.ac.aut.ceit.ap.fileserver.server;


import ir.ac.aut.ceit.ap.fileserver.network.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionManager {
    private ServerSocket serverSocket;
    private boolean listenForConnection = false;
    private ConnectionRouter router;

    ServerConnectionManager(int port) throws IOException {
        router = new ConnectionRouter();
        serverSocket = new ServerSocket(port);
        new Thread(() -> {
            try {
                waitForConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void waitForConnection() throws IOException {
        listenForConnection = true;
        while (listenForConnection) {
            Socket socket = serverSocket.accept();
            Thread t = new Thread(() -> {
                try {
                    Request request = new Request(socket.getInputStream());
                    router.route(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }

    private void stop() {
        listenForConnection = false;
    }
}
