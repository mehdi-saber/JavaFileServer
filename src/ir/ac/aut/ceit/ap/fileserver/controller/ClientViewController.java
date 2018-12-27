package ir.ac.aut.ceit.ap.fileserver.controller;

import ir.ac.aut.ceit.ap.fileserver.model.client.Client;
import ir.ac.aut.ceit.ap.fileserver.view.ClientView;

public class ClientViewController {
    private ClientView view;
    private Client client;

    public ClientViewController() {
        try {
        client=new Client();
            client.Login("admin","admin");
        } catch (Exception e) {
            e.printStackTrace();
        }
        view = new ClientView();
    }

}
