package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;

import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Controller {
    private PanelView view;
    private Client client;
    private FileListItem selectedFileItem;

    public Controller(Client client) {
        this.client = client;
        view = new PanelView();

        for (int i = 0; i < 100; i++)
            addNewFileToList("/dawd.dwa");

    }


    public void addNewFileToList(String name) {
        FileListItem item = new FileListItem(new FileInfo(name));
        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if (e.isPopupTrigger())
                    view.fileListPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                if (selectedFileItem != null)
                    selectedFileItem.setBorder(null);
                selectedFileItem = item;
                item.setBorder(new LineBorder(Color.BLACK));
            }
        });
        view.listPanel.add(item);
        view.revalidate();
    }
}
