package ir.ac.aut.ceit.ap.fileserver.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ClientView extends JFrame {
    public JMenuBar menubar;

    public ClientView() throws HeadlessException {
        super("Distributed File System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createMenuBar();
        setSize(700, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMenuBar() {

        menubar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        fileMenu.add(eMenuItem);
        menubar.add(fileMenu);

        setJMenuBar(menubar);
    }
}
