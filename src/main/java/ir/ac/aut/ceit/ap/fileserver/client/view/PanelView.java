package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

class PanelView extends JFrame {
    private JPanel mainPanel;
    JMenuBar menuBar;
    JPanel listPanel;
    JPanel previewPanel;
    JPanel propertiesPanel;
    JPanel rightPanel;
    JTabbedPane tabbedPane;
    JPanel fileOptionsPanel;

    JButton fileDownloadBtn;
    JButton fileCopyBtn;
    JButton fileMoveBtn;
    JButton fileRenameBtn;
    JButton fileDeleteBtn;

    JPopupMenu fileListPopupMenu;

    public PanelView() throws HeadlessException {
        super("File System Client");
        setupJFrame();
        setupMenuBar();
        setupMainPanel();
        setVisible(true);
    }

    private void setupJFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
    }

    private void setupMainPanel() {
        GridBagLayout mainLayout = new GridBagLayout();
        mainPanel = new JPanel(mainLayout);

        listPanel = new JPanel();
        rightPanel = new JPanel();
        JScrollPane listScrollPane = new JScrollPane(listPanel);
        listScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(listScrollPane);
        mainPanel.add(rightPanel);

        mainLayout.columnWeights = new double[]{1, 2};
        mainLayout.rowWeights = new double[]{1};

        GridBagConstraints listC = new GridBagConstraints();
        GridBagConstraints infoC = new GridBagConstraints();
        listC.fill = GridBagConstraints.BOTH;
        infoC.fill = GridBagConstraints.BOTH;
        mainLayout.setConstraints(listScrollPane, listC);
        mainLayout.setConstraints(rightPanel, infoC);

        setupListPanel();
        setupRightPanel();

        this.add(mainPanel);
    }

    private void setupListPanel() {
        fileListPopupMenu = new JPopupMenu();

        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
    }

    private void setupRightPanel() {
        propertiesPanel = new JPanel();
        previewPanel = new JPanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.add(previewPanel, "Preview");
        tabbedPane.add(propertiesPanel, "Properties");
        rightPanel.add(tabbedPane);

        fileOptionsPanel = new JPanel();
        fileDownloadBtn = new JButton("Download");
        fileCopyBtn = new JButton("Copy");
        fileMoveBtn = new JButton("Move");
        fileRenameBtn = new JButton("Rename");
        fileDeleteBtn = new JButton("Delete");
        fileOptionsPanel.add(fileDownloadBtn);
        fileOptionsPanel.add(fileCopyBtn);
        fileOptionsPanel.add(fileMoveBtn);
        fileOptionsPanel.add(fileRenameBtn);
        fileOptionsPanel.add(fileDeleteBtn);
        rightPanel.add(fileOptionsPanel);

        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
    }

    private void setupMenuBar() {

        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem eMenuItem = new JMenuItem("Exit");
        eMenuItem.setMnemonic(KeyEvent.VK_E);
        eMenuItem.setToolTipText("Exit application");
        eMenuItem.addActionListener((event) -> System.exit(0));

        fileMenu.add(eMenuItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }
}
