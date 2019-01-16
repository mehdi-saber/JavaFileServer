package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.util.WrapLayout;

import javax.swing.*;
import java.awt.*;

class MainJFrame extends JFrame {
    JMenuBar menuBar;
    JPanel listPanel;

    JMenuItem previewMIP;
    JMenuItem downloadMIP;
    JMenuItem copyMIP;
    JMenuItem cutMIP;
    JMenuItem renameMIP;
    JMenuItem deleteMIP;
    JMenuItem propertiesMIP;

    JMenuItem uploadMIC;
    JMenuItem cutMIC;
    JMenuItem copyMIC;
    JMenuItem pasteMIC;
    JMenuItem renameMIC;
    JMenuItem deleteMIC;
    JMenuItem propertiesMIC;

    JMenuItem uploadMIB;
    JMenuItem pasteMIB;
    JMenuItem exitMIB;

    JMenuItem searchMIB;
    JMenuItem copyMIB;
    JMenuItem cutMIB;
    JMenuItem renameMIB;
    JMenuItem deleteMIB;

    JPopupMenu pathPopupMenu;
    JPopupMenu curDirPopupMenu;

    public MainJFrame() {
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
        previewMIP = new JMenuItem("Preview");
        downloadMIP = new JMenuItem("Download");
        copyMIP = new JMenuItem("Copy");
        cutMIP = new JMenuItem("Cut");
        renameMIP = new JMenuItem("Rename");
        deleteMIP = new JMenuItem("Delete");
        propertiesMIP = new JMenuItem("Properties");

        pathPopupMenu.add(previewMIP);
        pathPopupMenu.add(downloadMIP);
        pathPopupMenu.add(copyMIP);
        pathPopupMenu.add(cutMIP);
        pathPopupMenu.add(renameMIP);
        pathPopupMenu.add(deleteMIP);
        pathPopupMenu.add(propertiesMIP);

        curDirPopupMenu = new JPopupMenu();
        uploadMIC = new JMenuItem("Upload");
        cutMIC = new JMenuItem("Cut");
        copyMIC = new JMenuItem("Copy");
        pasteMIC = new JMenuItem("paste");
        renameMIC = new JMenuItem("Rename");
        deleteMIC = new JMenuItem("Delete");
        propertiesMIC = new JMenuItem("Properties");

        curDirPopupMenu.add(uploadMIC);
        curDirPopupMenu.add(cutMIC);
        curDirPopupMenu.add(copyMIC);
        curDirPopupMenu.add(pasteMIC);
        curDirPopupMenu.add(renameMIC);
        curDirPopupMenu.add(deleteMIC);
        curDirPopupMenu.add(propertiesMIC);

        listPanel.setLayout(new WrapLayout(FlowLayout.LEFT, 5, 5));
        add(listScrollPane);
    }

    private void setupMenuBar() {

        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        uploadMIB = new JMenuItem("Upload");
        exitMIB = new JMenuItem("Exit");
        fileMenu.add(uploadMIB);
        fileMenu.add(exitMIB);


        searchMIB = new JMenuItem("Search");
        cutMIB = new JMenuItem("Cut");
        copyMIB = new JMenuItem("Copy");
        pasteMIB = new JMenuItem("Paste");
        renameMIB = new JMenuItem("Rename");
        deleteMIB = new JMenuItem("Delete");
        editMenu.add(searchMIB);
        editMenu.add(copyMIB);
        editMenu.add(cutMIB);
        fileMenu.add(pasteMIB);
        editMenu.add(renameMIB);
        editMenu.add(deleteMIB);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }
}
