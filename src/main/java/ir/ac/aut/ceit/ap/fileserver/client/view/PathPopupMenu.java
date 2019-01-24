package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;

/**
 * path pop menu in main window
 */
class PathPopupMenu extends JPopupMenu {
    final JMenuItem pasteMI;
    final JMenuItem previewMI;
    final JMenuItem downloadMI;
    final JMenuItem copyMI;
    final JMenuItem cutMI;
    final JMenuItem renameMI;
    final JMenuItem deleteMI;
    final JMenuItem propertiesMI;

    PathPopupMenu() {
        previewMI = new JMenuItem("Preview");
        downloadMI = new JMenuItem("Download");
        copyMI = new JMenuItem("Copy");
        cutMI = new JMenuItem("Cut");
        pasteMI = new JMenuItem("paste");
        renameMI = new JMenuItem("Rename");
        deleteMI = new JMenuItem("Delete");
        propertiesMI = new JMenuItem("Properties");

        add(previewMI);
        add(downloadMI);
        add(cutMI);
        add(copyMI);
        add(pasteMI);
        add(renameMI);
        add(deleteMI);
        add(propertiesMI);
    }
}
