package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressReader;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class MainWindowController {
    private MainWindowView window;
    private Client client;
    private ListItem selectedItem;
    private FSDirectory curDir;
    private FSPath pastePath;
    private PasteOperationType pasteOperation;
    private File desktopDir;
    private File downloadDir;

    public MainWindowController(Client client, Runnable finalCallback) {
        this.client = client;
        window = new MainWindowView();
        String homePath = System.getProperty("user.home") + File.separator;
        desktopDir = new File(homePath + "Desktop" + File.separator);
        downloadDir = new File(homePath + "Downloads" + File.separator);
        setupFinalizeCallback(finalCallback);
        setMouseListeners();
    }

    private void setupFinalizeCallback(Runnable finalCallback) {
        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                finalCallback.run();
            }
        });
    }

    private File openFileChoose() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(desktopDir);
        if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else
            return null;
    }


    private File saveFileChoose(FSFile file) {
        JFileChooser fileChooser = new JFileChooser();
        File defaultFile = new File(downloadDir + File.separator + file.getName());
        fileChooser.setSelectedFile(defaultFile);
        if (fileChooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else
            return null;
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

    public void showPathRepeatedError(String path) {
        showError("path \"" + path + "\"already exists.");
    }

    public boolean replacePrompt(String path) {
        String message = "path \"" + path + "\"already exists.\nDo you wand replace?";
        return JOptionPane.showConfirmDialog(window, message, "replace", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(window, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void renamePath(FSPath path) {
        String newName = getNotEmptyString("Enter new name:", path.getName());
        if (newName != null) {
            client.rename(path, newName);
        }
    }

    private void setMouseListeners() {
        ActionListener
                downloadAL = e -> download((FSFile) selectedItem.getInfo(), saveFileChoose((FSFile) selectedItem.getInfo())),
                previewAL = e -> new PreviewJFrame(),
                copyAL = e -> updatePasteInfo(selectedItem.getInfo(), PasteOperationType.COPY),
                cutAL = e -> updatePasteInfo(selectedItem.getInfo(), PasteOperationType.CUT),
                renameAL = e -> renamePath(selectedItem.getInfo()),
                deleteAL = e -> client.delete(selectedItem.getInfo()),
                propertiesAL = e -> new PropertiesJFrame(),
                uploadAL = e -> upload(openFileChoose(), curDir),
                newFolderAL = e -> createNewFolder(),
                pasteToCurAL = e -> paste(curDir),
                pasteToSelectedAL = e -> paste((FSDirectory) selectedItem.getInfo()),
                searchAL = e -> client.search(curDir),
                exitAL = e -> System.exit(0);

        window.pathPopupMenu.previewMI.addActionListener(previewAL);
        window.pathPopupMenu.downloadMI.addActionListener(downloadAL);
        window.pathPopupMenu.cutMI.addActionListener(cutAL);
        window.pathPopupMenu.copyMI.addActionListener(copyAL);
        window.pathPopupMenu.pasteMI.addActionListener(pasteToSelectedAL);
        window.pathPopupMenu.renameMI.addActionListener(renameAL);
        window.pathPopupMenu.deleteMI.addActionListener(deleteAL);
        window.pathPopupMenu.propertiesMI.addActionListener(propertiesAL);

        window.dirPopupMenu.uploadMI.addActionListener(uploadAL);
        window.dirPopupMenu.pasteMI.addActionListener(pasteToCurAL);
        window.dirPopupMenu.newFolderMI.addActionListener(newFolderAL);

        window.menuBar.uploadMI.addActionListener(uploadAL);
        window.menuBar.downloadMI.addActionListener(downloadAL);
        window.menuBar.newFolderMI.addActionListener(newFolderAL);
        window.menuBar.exitMI.addActionListener(exitAL);
        window.menuBar.searchMI.addActionListener(searchAL);
        window.menuBar.cutMI.addActionListener(cutAL);
        window.menuBar.copyMI.addActionListener(copyAL);
        window.menuBar.pasteMI.addActionListener(pasteToCurAL);
        window.menuBar.renameMI.addActionListener(renameAL);
        window.menuBar.deleteMI.addActionListener(deleteAL);
        window.menuBar.propertiesMI.addActionListener(propertiesAL);

        window.navPanel.parentBtn.addActionListener(e -> {
            if (curDir.getParent() != null)
                client.fetchDirectory(curDir.getParent());
        });

        window.navPanel.refreshBtn.addActionListener(e -> client.fetchDirectory(curDir));

        window.listPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (e.isPopupTrigger())
                    window.dirPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectPath();
            }
        });
    }

    private void paste(FSDirectory directory) {
        FSPath path = pastePath;
        PasteOperationType operationType = this.pasteOperation;
        if (pasteOperation.equals(PasteOperationType.CUT)) {
            pasteOperation = null;
            pastePath = null;
        }
        client.paste(path, directory, operationType, false);
    }

    private void updatePasteInfo(FSPath path, PasteOperationType type) {
        pastePath = path;
        pasteOperation = type;
    }

    private void upload(File file, FSDirectory directory) {
        if (file == null)
            return;
        long fileSize = file.length();
        ProgressWindow progressWindow = new ProgressWindow(window, "Uploading", 4 * fileSize);
        window.setEnabled(false);
        ResponseCallback responseCallback = response -> {
            if (response.getTitle().equals(ResponseSubject.REPEATED)) {
                showPathRepeatedError(directory.getAbsolutePath() + file.getName());
                closeProgressWindow(progressWindow);
            } else {
                SwingUtilities.invokeLater(() -> progressWindow.setOperationName("Server Distributing File"));
                try {
                    new ProgressReader(response.getInputStream("status"), progressWindow.getCallback()).start().join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                closeProgressWindow(progressWindow);
            }
        };
        client.upload(file, directory, progressWindow.getCallback(), responseCallback);
    }


    private void download(FSFile file, File newFile) {
        if (newFile == null)
            return;
        long fileSize = file.getSize();
        ProgressWindow progressWindow = new ProgressWindow(window, "Server Gathering Distributing File", 2 * fileSize);
        ProgressCallback progressCallback = progressWindow.getCallback();
        window.setEnabled(false);
        ResponseCallback responseCallback = response -> {
            try {
                new ProgressReader(response.getInputStream("status"), progressCallback).start().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            SwingUtilities.invokeLater(() -> {
                progressWindow.setOperationName("Downloading");
            });
        };
        client.download(file, newFile, progressWindow, responseCallback);
    }

    public void closeProgressWindow(ProgressWindow progressWindow) {
        SwingUtilities.invokeLater(() -> {
            progressWindow.setVisible(false);
            progressWindow.dispose();
            window.setEnabled(true);
        });
    }

    public void showPathList(FSDirectory curDir, Set<FSPath> pathSet) {
        deselectPath();
        List<FSPath> pathList = new ArrayList<>(pathSet);
        pathList.sort(Comparator.comparing(FSPath::getName));
        SwingUtilities.invokeLater(() -> {
            window.listPanel.removeAll();
            window.navPanel.urlField.setText(curDir.getAbsolutePath());
            this.curDir = curDir;

            if (curDir.getParent() == null)
                window.navPanel.disableParentBtn();
            else
                window.navPanel.enableParentBtn();

            for (FSPath path : pathList) {
                ListItem item = new ListItem(path);
                item.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        super.mousePressed(e);
                        if (e.isPopupTrigger())
                            window.pathPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                        selectPath(item);
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
        });
    }

    private void setNewSelectedItem(ListItem item) {
        if (selectedItem != null) {
            selectedItem.switchMode(false);
            selectedItem = null;
        }
        selectedItem = item;
        if (item != null) {
            item.switchMode(true);
        }
    }

    private void selectPath(ListItem item) {
        setNewSelectedItem(item);

        FSPath path = selectedItem.getInfo();

        window.menuBar.switchMode(true);
        window.menuBar.downloadMI.setEnabled(path instanceof FSFile);
        window.pathPopupMenu.downloadMI.setEnabled(path instanceof FSFile);

        boolean pasteEnable = pasteOperation != null && pastePath != null;
        window.menuBar.pasteMI.setEnabled(pasteEnable);
        window.dirPopupMenu.pasteMI.setEnabled(pasteEnable);
        window.pathPopupMenu.pasteMI.setEnabled(pasteEnable && path instanceof FSDirectory);
    }

    private void deselectPath() {
        setNewSelectedItem(null);
        window.menuBar.switchMode(false);
    }

    public FSDirectory getCurDir() {
        return curDir;
    }
}
