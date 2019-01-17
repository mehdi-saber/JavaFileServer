package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSFile;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSPath;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class MainWindowController {
    private MainWindowView view;
    private Client client;
    private ListItem selectedItem;
    private FSDirectory curDir;

    public MainWindowController(Client client) {
        this.client = client;
        view = new MainWindowView();
        setMouseListeners();


    }


    private File chooseNewFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(view);
        return fileChooser.getSelectedFile();
    }

    private String getNewName() {
        //todo:implement
        return null;
    }

    private void setMouseListeners() {
        view.listPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isPopupTrigger())
                    view.dirPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectFile();
            }
        });

        view.navPanel.parentBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (curDir.getParent() != null)
                    client.fetchDirectory(curDir.getParent());
            }
        });

        ActionListener
                downloadAL = e -> client.download((FSFile) selectedItem.getInfo()),
                previewAL = e -> new PreviewJFrame(),
                copyAL = e -> client.copy(selectedItem.getInfo()),
                cutAL = e -> client.cut(selectedItem.getInfo()),
                renameAL = e -> client.rename(selectedItem.getInfo(), getNewName()),
                deleteAL = e -> client.delete(selectedItem.getInfo()),
                propertiesAL = e -> new PropertiesJFrame(),
                uploadAL = e -> client.upload(chooseNewFile()),
                newFolderAL = e -> client.search(curDir),
                pasteAL = e -> client.paste(curDir),
                searchAL = e -> client.search(curDir),
                exitAL = e -> System.exit(0);

        view.pathPopupMenu.previewMI.addActionListener(previewAL);
        view.pathPopupMenu.downloadMI.addActionListener(downloadAL);
        view.pathPopupMenu.copyMI.addActionListener(copyAL);
        view.pathPopupMenu.cutMI.addActionListener(cutAL);
        view.pathPopupMenu.renameMI.addActionListener(renameAL);
        view.pathPopupMenu.deleteMI.addActionListener(deleteAL);
        view.pathPopupMenu.propertiesMI.addActionListener(propertiesAL);

        view.dirPopupMenu.uploadMI.addActionListener(uploadAL);
        view.dirPopupMenu.pasteMI.addActionListener(pasteAL);
        view.dirPopupMenu.pasteMI.addActionListener(newFolderAL);

        view.menuBar.uploadMI.addActionListener(uploadAL);
        view.menuBar.exitMI.addActionListener(exitAL);
        view.menuBar.searchMI.addActionListener(searchAL);
        view.menuBar.cutMI.addActionListener(cutAL);
        view.menuBar.copyMI.addActionListener(copyAL);
        view.menuBar.pasteMI.addActionListener(pasteAL);
        view.menuBar.renameMI.addActionListener(renameAL);
        view.menuBar.deleteMI.addActionListener(deleteAL);
    }


    public void showFileList(FSDirectory curDir, List<FSPath> infoList) {
        view.listPanel.removeAll();
        view.navPanel.urlField.setText(curDir.getAbsolutePath());
        this.curDir = curDir;

        if (curDir.getParent() == null)
            view.navPanel.disableParentBtn();
        else
            view.navPanel.enableParentBtn();

        for (FSPath path : infoList) {
            ListItem item = new ListItem(path);
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.isPopupTrigger())
                        view.pathPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    selectAFile(item);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() == 2)
                        if (path instanceof FSDirectory)
                            client.fetchDirectory((FSDirectory) path);
                }
            });
            view.listPanel.add(item);
        }
        view.revalidate();
        view.repaint();
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
