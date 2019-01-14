package ir.ac.aut.ceit.ap.fileserver.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnectionManager {
    private ServerSocket serverSocket;
    private boolean listenForConnection = false;
    private ConnectionRouter router;

    ServerConnectionManager(Server server, int port) throws IOException {
        router = new ConnectionRouter(server);
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
                    router.route(socket);
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
