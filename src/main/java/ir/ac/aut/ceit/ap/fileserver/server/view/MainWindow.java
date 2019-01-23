package ir.ac.aut.ceit.ap.fileserver.server.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

class MainWindow extends JFrame {
    final JTextField portTxt;
    final JTextField splitTxt;
    final JTextField redundancyTxt;
    final JButton startBtn;
    final JButton stopBtn;

    MainWindow() {
        super("server");
        setLayout(new GridBagLayout());
        GridBagConstraints c =new GridBagConstraints();

        c.anchor=GridBagConstraints.LINE_START;
        c.weightx=1;


        c.gridy=0;
        c.gridx=0;
        add(new JLabel("Server port:"),c);
        portTxt = new JTextField(10);
        portTxt.setText("5050");
        c.gridx=1;
        add(portTxt,c);

        c.gridy=1;
        c.gridx = 0;
        add(new JLabel("Split size(mB):"), c);
        splitTxt = new JTextField(10);
        splitTxt.setText("3.5");
        c.gridx = 1;
        add(splitTxt, c);

        c.gridy = 2;
        c.gridx = 0;
        add(new JLabel("redundancy:"), c);
        c.gridx = 1;
        redundancyTxt = new JTextField(10);
        redundancyTxt.setText("2");
        add(redundancyTxt, c);

        c.gridy = 3;
        c.gridx = 0;
        stopBtn = new JButton("stop");
        add(stopBtn, c);
        c.gridx = 1;
        startBtn = new JButton("start");
        add(startBtn, c);

        setupJFrame();
    }

    private void setupJFrame() {
        setSize(340, 165);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public Insets getInsets() {
        return new Insets(20,20,20,20);
    }
}
