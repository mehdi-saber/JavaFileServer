package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.PathInfo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class MainJFrameController {
    private MainJFrame view;
    private Client client;
    private FileListItem selectedFileItem;

    public MainJFrameController(Client client) {
        this.client = client;
        view = new MainJFrame();
        setupListListeners();
        List<PathInfo> list = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            list.add(new FileInfo("/dawd.dwad"));
        showFileList(list);
    }

    private void setupListListeners() {
        view.listPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isPopupTrigger())
                    view.pathPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectFile();
            }
        });
    }

    public void showFileList(List<PathInfo> infoList) {
        view.listPanel.removeAll();
        for (PathInfo info : infoList) {
            FileListItem item = new FileListItem(info);
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.isPopupTrigger())
                        view.pathPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    selectAFile(item);
                }
            });
            view.listPanel.add(item);
        }
        view.revalidate();
    }

    private void selectAFile(FileListItem item) {
        deselectFile();
        selectedFileItem = item;
        item.switchToHighLight();
    }

    private void deselectFile() {
        if (selectedFileItem != null)
            selectedFileItem.switchToNormal();
    }
}
