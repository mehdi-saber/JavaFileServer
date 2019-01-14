package ir.ac.aut.ceit.ap.fileserver.server;

import java.io.IOException;

public class ServerViewController {
    private ServerView view;
    private Server server;

    public ServerViewController() {
        try {
            server = new Server();
        } catch (IOException e) {
            e.printStackTrace();
        }
        view=new ServerView();
    }
}
