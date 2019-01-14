package ir.ac.aut.ceit.ap.fileserver.client;

import com.google.gson.Gson;

public class ClientViewController {
    private ClientView view;
    private Client client;

    public ClientViewController(Client client) {
        this.client = client;
        view = new ClientView();
    }
}
