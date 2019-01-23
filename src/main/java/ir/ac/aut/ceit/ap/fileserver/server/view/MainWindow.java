package ir.ac.aut.ceit.ap.fileserver.server.view;

import javax.swing.*;
import java.awt.*;

class MainWindow extends JFrame {
    final JTextField portTxt;
    final JTextField splitTxt;
    final JTextField redundancyTxt;
    final JButton startBtn;
    final JButton stopBtn;

    MainWindow() {
        setLayout(new GridBagLayout());
        GridBagConstraints c =new GridBagConstraints();

        c.gridy=0;
        c.gridx=0;
        add(new JLabel("Server port:"),c);
        portTxt = new JTextField();
        c.gridx=1;
        add(portTxt,c);

        c.gridy=1;
        c.gridx = 0;
        add(new JLabel("Split size(mB):"), c);
        splitTxt = new JTextField();
        c.gridx = 1;
        add(splitTxt, c);

        c.gridy = 2;
        c.gridx = 0;
        add(new JLabel("redundancy:"), c);
        c.gridx = 1;
        redundancyTxt = new JTextField();
        add(redundancyTxt, c);

        c.gridy = 3;
        c.gridx = 0;
        startBtn = new JButton("start");
        add(startBtn, c);
        stopBtn = new JButton("stop");
        c.gridx = 1;
        add(stopBtn, c);

        setupJFrame();
    }

    private void setupJFrame() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
