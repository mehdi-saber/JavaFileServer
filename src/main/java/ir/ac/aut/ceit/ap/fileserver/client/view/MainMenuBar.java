package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;

class MainMenuBar extends JMenuBar {
   final JMenuItem uploadMI;
   final JMenuItem downloadMI;
   final JMenuItem newFolderMI;
   final JMenuItem searchMI;
   final JMenuItem renameMI;
   final JMenuItem deleteMI;
    final JMenuItem propertiesMI;
   final JMenuItem exitMI;

   final JMenuItem copyMI;
   final JMenuItem cutMI;
   final JMenuItem pasteMI;

   final private JComponent[] justSelected;

    MainMenuBar() {
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        uploadMI = new JMenuItem("Upload");
        downloadMI = new JMenuItem("Download");
        newFolderMI = new JMenuItem("New Folder");
        searchMI = new JMenuItem("Search");
        renameMI = new JMenuItem("Rename");
        deleteMI = new JMenuItem("Delete");
        propertiesMI = new JMenuItem("Properties");
        exitMI = new JMenuItem("Exit");
        fileMenu.add(uploadMI);
        fileMenu.add(downloadMI);
        fileMenu.add(newFolderMI);
        fileMenu.add(searchMI);
        fileMenu.add(renameMI);
        fileMenu.add(deleteMI);
        fileMenu.add(propertiesMI);
        fileMenu.add(exitMI);

        cutMI = new JMenuItem("Cut");
        copyMI = new JMenuItem("Copy");
        pasteMI = new JMenuItem("Paste");
        editMenu.add(copyMI);
        editMenu.add(cutMI);
        editMenu.add(pasteMI);

        add(fileMenu);
        add(editMenu);
        add(helpMenu);

        justSelected = new JComponent[]{
                propertiesMI,downloadMI, renameMI, deleteMI, cutMI, copyMI
        };

        switchMode(false);
     }

   void switchMode(boolean fileSelected) {
      for (JComponent component : justSelected)
         component.setEnabled(fileSelected);
   }
}
