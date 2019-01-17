package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.util.WrapLayout;

import javax.swing.*;
import java.awt.*;

class MainWindowView extends JFrame {
    MainMenuBar menuBar;
    JPanel listPanel;
    PathPopupMenu pathPopupMenu;
    DirPopupMenu dirPopupMenu;
    NavigationPanel navPanel;
    ConnectWindow connectWindow;

    public MainWindowView() {
        super("File System Client");

        menuBar = new MainMenuBar();
        setJMenuBar(menuBar);

        navPanel=new NavigationPanel();
        add(navPanel);

        setupListPanel();

        pathPopupMenu = new PathPopupMenu();
        dirPopupMenu = new DirPopupMenu();

        setupJFrame();
    }

    private void setupJFrame() {
        setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }


    private void setupListPanel() {
        listPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 5, 5));
        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.setHorizontalScrollBar(null);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(listScrollPane);
    }
}
