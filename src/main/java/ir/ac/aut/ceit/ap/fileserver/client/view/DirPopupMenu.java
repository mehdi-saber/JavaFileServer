package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;

/**
 * cur directory pop menu
 */
class DirPopupMenu extends JPopupMenu {
    JMenuItem uploadMI;
    JMenuItem newFolderMI;
    JMenuItem pasteMI;

    DirPopupMenu() {
        uploadMI = new JMenuItem("Upload");
        newFolderMI = new JMenuItem("New Folder");
        pasteMI = new JMenuItem("Paste");

        add(uploadMI);
        add(newFolderMI);
        add(pasteMI);
    }
}
