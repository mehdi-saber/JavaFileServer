package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;

 class PathPopupMenu extends JPopupMenu {
    JMenuItem previewMI;
    JMenuItem downloadMI;
    JMenuItem copyMI;
    JMenuItem cutMI;
    JMenuItem renameMI;
    JMenuItem deleteMI;
    JMenuItem propertiesMI;

     PathPopupMenu() {
        previewMI = new JMenuItem("Preview");
        downloadMI = new JMenuItem("Download");
        copyMI = new JMenuItem("Copy");
        cutMI = new JMenuItem("Cut");
        renameMI = new JMenuItem("Rename");
        deleteMI = new JMenuItem("Delete");
        propertiesMI = new JMenuItem("Properties");

        add(previewMI);
        add(downloadMI);
        add(copyMI);
        add(cutMI);
        add(renameMI);
        add(deleteMI);
        add(propertiesMI);
    }
}
