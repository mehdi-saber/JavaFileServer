package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;

 class MainMenuBar extends JMenuBar {
    JMenuItem uploadMI;
    JMenuItem pasteMI;
    JMenuItem exitMI;

    JMenuItem searchMI;
    JMenuItem copyMI;
    JMenuItem cutMI;
    JMenuItem renameMI;
    JMenuItem deleteMI;

     MainMenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        uploadMI = new JMenuItem("Upload");
        exitMI = new JMenuItem("Exit");
        fileMenu.add(uploadMI);
        fileMenu.add(exitMI);


        searchMI = new JMenuItem("Search");
        cutMI = new JMenuItem("Cut");
        copyMI = new JMenuItem("Copy");
        pasteMI = new JMenuItem("Paste");
        renameMI = new JMenuItem("Rename");
        deleteMI = new JMenuItem("Delete");
        editMenu.add(searchMI);
        editMenu.add(copyMI);
        editMenu.add(cutMI);
        fileMenu.add(pasteMI);
        editMenu.add(renameMI);
        editMenu.add(deleteMI);

        add(fileMenu);
        add(editMenu);
        add(helpMenu);
    }
}
