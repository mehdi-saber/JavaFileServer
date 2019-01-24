package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;

import javax.swing.*;
import java.awt.event.ActionListener;

public class ConnectWindowController {
    private ConnectWindow dialog;
    private Client client;

    public ConnectWindowController(Client client) {
        this.client = client;
        dialog = new ConnectWindow();
        setupListener();
        connectToServer();//todo:remove
    }

    private void setupListener() {
        dialog.exitBtn.addActionListener(e -> System.exit(0));
        ActionListener connectAL = e -> connectToServer();
        dialog.loginBtn.addActionListener(connectAL);
        dialog.addressTxt.addActionListener(connectAL);
        dialog.listenPortTxt.addActionListener(connectAL);
        dialog.portTxt.addActionListener(connectAL);
        dialog.usernameTxt.addActionListener(connectAL);
        dialog.passwordTxt.addActionListener(connectAL);
    }

    private void connectToServer() {
        String serverAddress = dialog.addressTxt.getText();
        int serverPort = Integer.valueOf(dialog.portTxt.getText());
        int listenPort = Integer.valueOf(dialog.listenPortTxt.getText());
        String username = dialog.usernameTxt.getText();
        String password = String.valueOf(dialog.passwordTxt.getPassword());
        boolean connected = client.connectToServer(serverAddress, serverPort, username, password, listenPort);
        if (connected) {
            dialog.setVisible(false);
            dialog.dispose();
            client.openMainWindow();
        } else
            JOptionPane.showMessageDialog(
                    dialog, "There was a problem in connection.\nCheck information.",
                    "ERROR", JOptionPane.ERROR_MESSAGE);
    }
}
