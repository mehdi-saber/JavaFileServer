package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;
import java.awt.*;

 class ConnectWindow extends JFrame {
    JTextField addressTxt;
    JTextField portTxt;
    JTextField usernameTxt;
    JPasswordField passwordTxt;
    JButton exitBtn;
    JButton loginBtn;

    public ConnectWindow() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.BOTH;

        c.gridy = 0;
        JLabel addressLabel = new JLabel("Address:");
        addressTxt = new JTextField(10);
        addressTxt.setText("localhost");
        c.gridx = 0;
        c.gridwidth = 1;
        add(addressLabel, c);
        c.gridx = 1;
        c.gridwidth = 2;
        add(addressTxt, c);

        c.gridy = 1;
        JLabel portLabel = new JLabel("Port:");
        portTxt = new JTextField(10);
        portTxt.setText("5050");
        c.gridx = 0;
        c.gridwidth = 1;
        add(portLabel, c);
        c.gridx = 1;
        c.gridwidth = 2;
        add(portTxt, c);

        c.gridy = 2;
        JLabel usernameLabel = new JLabel("Username:");
        usernameTxt = new JTextField(10);
        usernameTxt.setText("admin");
        c.gridx = 0;
        c.gridwidth = 1;
        add(usernameLabel, c);
        c.gridx = 1;
        c.gridwidth = 2;
        add(usernameTxt, c);

        c.gridy = 3;
        JLabel passwordLabel = new JLabel("Password:");
        passwordTxt = new JPasswordField(10);
        passwordTxt.setText("admin123");
        c.gridx = 0;
        c.gridwidth = 1;
        add(passwordLabel, c);
        c.gridx = 1;
        c.gridwidth = 2;
        add(passwordTxt, c);

        c.gridy = 4;
        c.gridwidth = 1;
        loginBtn = new JButton("Connect");
        exitBtn = new JButton("Exit");
        c.gridx = 1;
        add(exitBtn, c);
        c.gridx = 2;
        add(loginBtn, c);

        setSize(250, 140);
        setResizable(false);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
    }
}
