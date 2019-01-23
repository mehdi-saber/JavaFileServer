package ir.ac.aut.ceit.ap.fileserver.server.view;

import javax.swing.*;
import java.awt.*;

class MainWindow extends JFrame {
    final JTextField portTxt;
    final JTextField splitTxt;
    final JTextField redundancyTxt;
    final JButton startBtn;
    final JButton stopBtn;

    MainWindow() throws HeadlessException {
        setLayout(new GridBagLayout());
        GridBagConstraints c =new GridBagConstraints();

        c.gridy=0;
        c.gridx=0;
        add(new JLabel("Server port:"),c);
        portTxt = new JTextField();
        c.gridx=1;
        add(portTxt,c);

        c.gridy=1;
        c.gridy
        add(new JLabel("Split size(mB):"));
        splitTxt = new JTextField();
        add(splitTxt);
        add(new JLabel("redundancy:"));
        redundancyTxt = new JTextField();
        add(redundancyTxt);

        startBtn = new JButton("start");
        add(startBtn);
        stopBtn = new JButton("stop");
        add(stopBtn);

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
