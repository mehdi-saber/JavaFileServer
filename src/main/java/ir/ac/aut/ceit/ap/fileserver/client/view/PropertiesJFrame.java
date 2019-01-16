package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.util.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

class PropertiesJFrame extends JFrame {
    JMenuBar menuBar;
    JPanel listPanel;

    JMenuItem previewMI;
    JMenuItem downloadMI;
    JMenuItem copyMI;
    JMenuItem cutMI;
    JMenuItem renameMI;
    JMenuItem deleteMI;
    JMenuItem propertiesMI;

    JPopupMenu pathPopupMenu;
    JPopupMenu curDirPopupMenu;

    public PropertiesJFrame() throws HeadlessException {
        super("File System Client");
        setupJFrame();
        setupMenuBar();
        setupListPanel();
        setVisible(true);
    }

    private void setupJFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }


    private void setupListPanel() {
        listPanel = new JPanel();
        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.setHorizontalScrollBar(null);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);

        pathPopupMenu = new JPopupMenu();
        previewMI = new JMenuItem("Preview");
        downloadMI = new JMenuItem("Download");
        copyMI = new JMenuItem("Copy");
        cutMI = new JMenuItem("Cut");
        renameMI = new JMenuItem("Rename");
        deleteMI = new JMenuItem("Delete");
        propertiesMI = new JMenuItem("Properties");

        pathPopupMenu.add(previewMI);
        pathPopupMenu.add(downloadMI);
        pathPopupMenu.add(copyMI);
        pathPopupMenu.add(cutMI);
        pathPopupMenu.add(renameMI);
        pathPopupMenu.add(deleteMI);
        pathPopupMenu.add(propertiesMI);

        listPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 5, 5));
        add(listScrollPane);
    }

    private void setupMenuBar() {

        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }
}
