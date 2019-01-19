package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.ResponseCallback;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class MainWindowController {
    private MainWindowView window;
    private Client client;
    private ListItem selectedItem;
    private FSDirectory curDir;

    public MainWindowController(Client client) {
        this.client = client;
        window = new MainWindowView();
        setMouseListeners();
    }

    private File chooseNewFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(window);
        return fileChooser.getSelectedFile();
    }

    public void createNewFolder() {
        String name = getNotEmptyString("Enter folder name:", "untitled folder");
        if (name != null)
            client.createNewFolder(curDir, name);
    }

    private String getNotEmptyString(String message, String initialStr) {
        while (true) {
            String name = JOptionPane.showInputDialog(window, message, initialStr);
            if (name == null)
                return null;
            else if (!name.equals(""))
                return name;
            else
                showError("Field can not be empty.");
        }
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(window, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void renamePath(FSPath path) {
        String newName = getNotEmptyString("Enter new name:", path.getName());
        if (newName != null) {
            FSPath newPath = null;
            if (path instanceof FSDirectory)
                newPath = new FSDirectory(path.getParent(), newName);
            else if (path instanceof FSFile) {
                newPath = new FSFile(path.getParent(), newName, ((FSFile) path).getParts());
            }
            client.rename(path, newPath, true);
        }
    }

    private void setMouseListeners() {
        window.listPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isPopupTrigger())
                    window.dirPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectFile();
            }
        });

        window.navPanel.parentBtn.addMouseListener(new MouseAdapter() {
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
                renameAL = e -> renamePath(selectedItem.getInfo()),
                deleteAL = e -> client.delete(selectedItem.getInfo()),
                propertiesAL = e -> new PropertiesJFrame(),
                uploadAL = e -> upload(chooseNewFile(),curDir),
                newFolderAL = e -> createNewFolder(),
                pasteAL = e -> client.paste(curDir),
                searchAL = e -> client.search(curDir),
                exitAL = e -> System.exit(0);

        window.pathPopupMenu.previewMI.addActionListener(previewAL);
        window.pathPopupMenu.downloadMI.addActionListener(downloadAL);
        window.pathPopupMenu.copyMI.addActionListener(copyAL);
        window.pathPopupMenu.cutMI.addActionListener(cutAL);
        window.pathPopupMenu.renameMI.addActionListener(renameAL);
        window.pathPopupMenu.deleteMI.addActionListener(deleteAL);
        window.pathPopupMenu.propertiesMI.addActionListener(propertiesAL);

        window.dirPopupMenu.uploadMI.addActionListener(uploadAL);
        window.dirPopupMenu.pasteMI.addActionListener(pasteAL);
        window.dirPopupMenu.newFolderMI.addActionListener(newFolderAL);

        window.menuBar.uploadMI.addActionListener(uploadAL);
        window.menuBar.downloadMI.addActionListener(downloadAL);
        window.menuBar.newFolderMI.addActionListener(newFolderAL);
        window.menuBar.exitMI.addActionListener(exitAL);
        window.menuBar.searchMI.addActionListener(searchAL);
        window.menuBar.cutMI.addActionListener(cutAL);
        window.menuBar.copyMI.addActionListener(copyAL);
        window.menuBar.pasteMI.addActionListener(pasteAL);
        window.menuBar.renameMI.addActionListener(renameAL);
        window.menuBar.deleteMI.addActionListener(deleteAL);
    }

    private void upload(File file, FSDirectory directory) {
        if (file != null) {
            long fileSize = file.length();
            ProgressWindow progressWindow = new ProgressWindow(window, "Uploading", fileSize);
            window.setEnabled(false);
            ResponseCallback responseCallback = response -> {
                progressWindow.setVisible(false);
                progressWindow.dispose();
                window.setEnabled(true);
                client.fetchDirectory(directory);
            };
            client.upload(file, directory, fileSize, progressWindow.getCallback(), responseCallback);
        }
    }

    public void showFileList(FSDirectory curDir, List<FSPath> infoList) {
        window.listPanel.removeAll();
        window.navPanel.urlField.setText(curDir.getAbsolutePath());
        this.curDir = curDir;

        if (curDir.getParent() == null)
            window.navPanel.disableParentBtn();
        else
            window.navPanel.enableParentBtn();

        for (FSPath path : infoList) {
            ListItem item = new ListItem(path);
            item.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    if (e.isPopupTrigger())
                        window.pathPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    selectFile(item);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    if (e.getClickCount() == 2)
                        if (path instanceof FSDirectory)
                            client.fetchDirectory((FSDirectory) path);
                }
            });
            window.listPanel.add(item);
        }
        window.revalidate();
        window.repaint();
    }

    private void selectFile(ListItem item) {
        deselectFile();
        selectedItem = item;
        item.switchMode(true);
        window.menuBar.switchMode(true);
    }

    private void deselectFile() {
        if (selectedItem != null) {
            selectedItem.switchMode(false);
            selectedItem = null;
        }
        window.menuBar.switchMode(false);
    }

    public FSDirectory getCurDir() {
        return curDir;
    }
}
