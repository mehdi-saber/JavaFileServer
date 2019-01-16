package ir.ac.aut.ceit.ap.fileserver.server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

 class ConnectionManager {
    private ServerSocket serverSocket;
    private boolean listenForConnection = false;
    private ConnectionRouter router;

    ConnectionManager(Server server, int port)  {
        router = new ConnectionRouter(server);
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
