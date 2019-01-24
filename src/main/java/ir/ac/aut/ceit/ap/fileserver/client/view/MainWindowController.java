package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.client.Client;
import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FileCategory;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressCallback;
import ir.ac.aut.ceit.ap.fileserver.network.progress.ProgressReader;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.PasteOperationType;
import ir.ac.aut.ceit.ap.fileserver.network.protocol.ResponseSubject;
import ir.ac.aut.ceit.ap.fileserver.network.receiver.ResponseCallback;
import ir.ac.aut.ceit.ap.fileserver.server.ClientInfo;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * connects main window to client
 */
public class MainWindowController {
    private MainWindowView window;
    private Client client;
    private ListItem selectedItem;
    private FSDirectory curDir;
    private FSPath pastePath;
    private PasteOperationType pasteOperation;
    private File desktopDir;
    private File downloadDir;

    /**
     * creates new controller object for client
     *
     * @param client the client
     */
    public MainWindowController(Client client) {
        this.client = client;
        window = new MainWindowView();
        String homePath = System.getProperty("user.home") + File.separator;
        desktopDir = new File(homePath + "Desktop" + File.separator);
        downloadDir = new File(homePath + "Downloads" + File.separator);
        setMouseListeners();
        updatePasteBtn();
    }

    /**
     * choose a file
     *
     * @return the file
     */
    private File openFileChoose() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(desktopDir);
        if (fileChooser.showOpenDialog(window) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else
            return null;
    }

    /**
     * choose a path to save
     *
     * @param file origibal file
     * @return the file path
     */
    private File saveFileChoose(FSFile file) {
        JFileChooser fileChooser = new JFileChooser();
        File defaultFile = new File(downloadDir + File.separator + file.getName());
        fileChooser.setSelectedFile(defaultFile);
        if (fileChooser.showSaveDialog(window) == JFileChooser.APPROVE_OPTION)
            return fileChooser.getSelectedFile();
        else
            return null;
    }

    /**
     * creates new folder
     */
    public void createNewFolder() {
        String name = getNotEmptyString("Enter folder name:", "untitled folder");
        if (name != null)
            client.createNewFolder(curDir, name);
    }

    /**
     * get a non empty string
     *
     * @param message    input message
     * @param initialStr initial string
     * @return the string
     */
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

    /**
     * repeated error
     *
     * @param path the path
     */
    public void showPathRepeatedError(String path) {
        showError("path \"" + path + "\"already exists.");
    }

    /**
     * shows a error
     *
     * @param message message
     */
    public void showError(String message) {
        JOptionPane.showMessageDialog(window, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * rename a path
     *
     * @param path the path
     */
    public void renamePath(FSPath path) {
        String newName = getNotEmptyString("Enter new name:", path.getName());
        if (newName != null) {
            client.rename(path, newName);
        }
    }

    /**
     * set mouse listeners
     */
    private void setMouseListeners() {
        ActionListener
                downloadAL = e -> download((FSFile) selectedItem.getInfo(), saveFileChoose((FSFile) selectedItem.getInfo())),
                previewAL = e -> preview((FSFile) selectedItem.getInfo()),
                copyAL = e -> updatePasteInfo(selectedItem.getInfo(), PasteOperationType.COPY),
                cutAL = e -> updatePasteInfo(selectedItem.getInfo(), PasteOperationType.CUT),
                renameAL = e -> renamePath(selectedItem.getInfo()),
                deleteAL = e -> delete(selectedItem.getInfo()),
                propertiesAL = e -> properties(selectedItem.getInfo()),
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
                super.mousePressed(e);
                if (e.isPopupTrigger())
                    window.dirPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectPath();
            }

            @Override
            public void mouseReleased(MouseEvent e){
                super.mouseReleased(e);
                if(e.isPopupTrigger())
                    window.dirPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                deselectPath();
            }
        });
    }

    /**
     * preview a file
     *
     * @param info file info
     */
    private void preview(FSFile info) {
        try {
            File previewFile = File.createTempFile("thePreviewTemp", "." + info.getExtension());
            ResponseCallback callback = response -> SwingUtilities.invokeLater(() -> {
                if (info.getExtension().equals("pdf")) {
//                    SwingController controller = new SwingController();
//                    SwingViewBuilder factory = new SwingViewBuilder(controller);
//                    JPanel viewerComponentPanel = factory.buildViewerPanel();
//                    // add interactive mouse link annotation support via callback
//                    controller.getDocumentViewController().setAnnotationCallback(
//                            new org.icepdf.ri.common.MyAnnotationCallback(
//                                    controller.getDocumentViewController()));
//                    JFrame applicationFrame = new JFrame();
//                    applicationFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//                    applicationFrame.getContentPane().add(viewerComponentPanel);
//                    // Now that the GUI is all in place, we can try opening a PDF
//                    controller.openDocument(previewFile.getAbsolutePath());
//                    // show the component
//                    applicationFrame.pack();
//                    applicationFrame.setVisible(true);
                } else
                    new PreviewDialog(window, info, previewFile);
                previewFile.delete();
            });
            new Thread(() -> client.preview(info, previewFile, callback)).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * show properties
     *
     * @param path the path
     */
    private void properties(FSPath path) {
        ResponseCallback callback = response -> {
            LinkedHashMap<Long, List<ClientInfo>> nodes =
                    (LinkedHashMap<Long, List<ClientInfo>>) response.getParameter("nodes");
            new PropertiesDialog(path, nodes, window);
        };
        if (path instanceof FSFile)
            client.getNodeDist((FSFile) path, callback);
        else if (path instanceof FSDirectory)
            new PropertiesDialog(path, null, window);
    }

    /**
     * delete a path
     *
     * @param path the path
     */
    private void delete(FSPath path) {
        StringBuilder message = new StringBuilder("Do you want delete path \"");
        message.append(path.getAbsolutePath());
        message.append("\" ?");
        int ans = JOptionPane.showConfirmDialog(window, message, "delete", JOptionPane.YES_NO_OPTION);
        if (ans == JOptionPane.YES_OPTION)
            client.delete(selectedItem.getInfo());
    }

    /**
     * paste files in clipboard
     *
     * @param directory to parent
     */
    private void paste(FSDirectory directory) {
        FSPath path = pastePath;
        PasteOperationType operationType = this.pasteOperation;
        if (pasteOperation.equals(PasteOperationType.CUT)) {
            pasteOperation = null;
            pastePath = null;
        }
        updatePasteBtn();
        client.paste(path, directory, operationType);
    }

    /**
     * update paste operation info
     *
     * @param path the path
     * @param type the operation type
     */
    private void updatePasteInfo(FSPath path, PasteOperationType type) {
        pastePath = path;
        pasteOperation = type;
        updatePasteBtn();
    }

    /**
     * upload a file
     *
     * @param file      file
     * @param directory directory
     */
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


    /**
     * download a file
     *
     * @param file    the file
     * @param newFile to new HDD location
     */
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

    /**
     * closes progress window
     *
     * @param progressWindow ther progress
     */
    public void closeProgressWindow(ProgressWindow progressWindow) {
        SwingUtilities.invokeLater(() -> {
            progressWindow.setVisible(false);
            progressWindow.dispose();
            window.setEnabled(true);
        });
    }

    /**
     * show files and folders
     *
     * @param curDir  directory
     * @param pathSet paths
     */
    public void showPathList(FSDirectory curDir, Set<FSPath> pathSet) {
        deselectPath();
        List<FSPath> pathList = new ArrayList<>(pathSet);
        //sorts file by names
        pathList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
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
                        if (e.isMetaDown())
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

    /**
     * update selected item
     *
     * @param item list item
     */
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

    /**
     * update selected item
     *
     * @param item list item
     */
    private void selectPath(ListItem item) {
        setNewSelectedItem(item);

        FSPath path = selectedItem.getInfo();

        window.menuBar.switchMode(true);
        window.menuBar.downloadMI.setEnabled(path instanceof FSFile);
        window.pathPopupMenu.downloadMI.setEnabled(path instanceof FSFile);

        boolean preview = false;
        if (path instanceof FSFile) {
            FSFile file = (FSFile) path;
            Set<String> previewAbleExtensions = Stream.of(FileCategory.DOCUMENT, FileCategory.IMAGE, FileCategory.VIDEO)
                    .flatMap(fileCategory -> Arrays.stream(fileCategory.getExtensions()))
                    .collect(Collectors.toSet());
            preview = previewAbleExtensions.contains(file.getExtension());
        }
        window.pathPopupMenu.previewMI.setEnabled(preview);

        updatePasteBtn();
    }

    /**
     * update selected item
     */
    private void deselectPath() {
        setNewSelectedItem(null);
        window.menuBar.switchMode(false);
    }

    /**
     * update save buttons
     */
    private void updatePasteBtn() {
        boolean pasteEnable = pasteOperation != null && pastePath != null;
        window.menuBar.pasteMI.setEnabled(pasteEnable);
        window.dirPopupMenu.pasteMI.setEnabled(pasteEnable);
        if (selectedItem != null)
            window.pathPopupMenu.pasteMI.setEnabled(pasteEnable && selectedItem.getInfo() instanceof FSDirectory);
    }

    /**
     * @return current directory
     */
    public FSDirectory getCurDir() {
        return curDir;
    }
}
