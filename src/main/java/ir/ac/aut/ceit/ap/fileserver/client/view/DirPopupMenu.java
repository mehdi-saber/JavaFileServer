package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;

public class DirPopupMenu extends JPopupMenu {
    JMenuItem uploadMI;
    JMenuItem cutMI;
    JMenuItem copyMI;
    JMenuItem pasteMI;
    JMenuItem renameMI;
    JMenuItem deleteMI;
    JMenuItem propertiesMI;

    public DirPopupMenu() {
        uploadMI = new JMenuItem("Upload");
        cutMI = new JMenuItem("Cut");
        copyMI = new JMenuItem("Copy");
        pasteMI = new JMenuItem("paste");
        renameMI = new JMenuItem("Rename");
        deleteMI = new JMenuItem("Delete");
        propertiesMI = new JMenuItem("Properties");

        add(uploadMI);
        add(cutMI);
        add(copyMI);
        add(pasteMI);
        add(renameMI);
        add(deleteMI);
        add(propertiesMI);
    }
}
