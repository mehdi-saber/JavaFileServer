package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.filesys.DirectoryInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.PathInfo;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainJFrameController {
    private MainJFrame mainJFrame;
    private Client client;
    private ListItem selectedItem;
    private DirectoryInfo curDir;

    public MainJFrameController(Client client) {
        this.client = client;
        mainJFrame = new MainJFrame();
        setupListListeners();
        setupBarListeners();



        List<PathInfo> list = new ArrayList<>();
        for (int i = 0; i < 100; i++)
            list.add(new FileInfo("/dawd.dwad"));
        showFileList(list);
    }

    private void setupBarListeners() {

    }

    private void setupListListeners() {
        mainJFrame.listPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isPopupTrigger())
                    mainJFrame.curDirPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectFile();
            }
        });

        ActionListener
                downloadAL = e -> client.download((FileInfo) selectedItem.getInfo()),
                previewAL = e -> new PreviewJFrame(),
                copyAL = e -> client.copy(selectedItem.getInfo()),
                cutAL = e -> client.cut(selectedItem.getInfo()),
                renameAL = e -> client.rename(selectedItem.getInfo(), getNewName()),
                deleteAL = e -> client.delete(selectedItem.getInfo()),
                propertiesAL = e -> new PropertiesJFrame(),
                uploadAL = e -> client.upload(chooseNewFile()),
                pasteAL = e -> client.paste(curDir),
                searchAL = e -> client.search(curDir),
                exitAL = e -> System.exit(0);

        mainJFrame.previewMIP.addActionListener(previewAL);
        mainJFrame.downloadMIP.addActionListener(downloadAL);
        mainJFrame.copyMIP.addActionListener(copyAL);
        mainJFrame.cutMIP.addActionListener(cutAL);
        mainJFrame.renameMIP.addActionListener(renameAL);
        mainJFrame.deleteMIP.addActionListener(deleteAL);
        mainJFrame.propertiesMIP.addActionListener(propertiesAL);

        mainJFrame.uploadMIC.addActionListener(uploadAL);
        mainJFrame.copyMIC.addActionListener(copyAL);
        mainJFrame.cutMIC.addActionListener(cutAL);
        mainJFrame.pasteMIC.addActionListener(pasteAL);
        mainJFrame.renameMIC.addActionListener(renameAL);
        mainJFrame.deleteMIC.addActionListener(deleteAL);
        mainJFrame.propertiesMIC.addActionListener(propertiesAL);

        mainJFrame.uploadMIB.addActionListener(uploadAL);
        mainJFrame.exitMIB.addActionListener(exitAL);
        mainJFrame.searchMIB.addActionListener(searchAL);
        mainJFrame.cutMIB.addActionListener(cutAL);
        mainJFrame.copyMIB.addActionListener(copyAL);
        mainJFrame.pasteMIB.addActionListener(pasteAL);
        mainJFrame.renameMIB.addActionListener(renameAL);
        mainJFrame.deleteMIB.addActionListener(deleteAL);
    }

    private File chooseNewFile() {
        //todo:implement
        return null;
    }

    private String getNewName() {
        //todo:implement
        return null;
    }

    public void showFileList(List<PathInfo> infoList) {
        mainJFrame.listPanel.removeAll();
        for (PathInfo info : infoList) {
            ListItem item = new ListItem(info);
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.isPopupTrigger())
                        mainJFrame.pathPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    selectAFile(item);
                }
            });
            mainJFrame.listPanel.add(item);
        }
        mainJFrame.revalidate();
        mainJFrame.repaint();
    }

    private void selectAFile(ListItem item) {
        deselectFile();
        selectedItem = item;
        item.switchToHighLight();
    }

    private void deselectFile() {
        if (selectedItem != null)
            selectedItem.switchToNormal();
    }
}
