package ir.ac.aut.ceit.ap.fileserver.server.view;

import ir.ac.aut.ceit.ap.fileserver.server.Server;

/**
 * connects main window to server
 */
public class MainWindowController {
    private MainWindow window;
    private Server server;

    public MainWindowController(Server server, Runnable finalCallback) {
        window = new MainWindow();
        this.server = server;

        window.startBtn.addActionListener(e -> {
            int port = Integer.valueOf(window.portTxt.getText());
            int splitSize = ((Double) (Double.valueOf(window.splitTxt.getText()) * 1024 * 1024)).intValue();
            int redundancy = Integer.valueOf(window.redundancyTxt.getText());

            window.stopBtn.setEnabled(true);
            window.startBtn.setEnabled(false);
            window.portTxt.setEnabled(false);
            window.splitTxt.setEnabled(false);
            window.redundancyTxt.setEnabled(false);

            this.server.start(port, splitSize, redundancy);
        });

        window.stopBtn.addActionListener(e -> {

            window.stopBtn.setEnabled(false);
            window.startBtn.setEnabled(true);
            window.portTxt.setEnabled(true);
            window.splitTxt.setEnabled(true);
            window.redundancyTxt.setEnabled(true);

            server.stop();
        });

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                finalCallback.run();
            }
        });

        window.startBtn.getActionListeners()[0].actionPerformed(null);
    }
}
