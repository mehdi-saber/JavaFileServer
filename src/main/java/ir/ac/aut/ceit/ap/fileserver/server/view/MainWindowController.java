package ir.ac.aut.ceit.ap.fileserver.server.view;

import ir.ac.aut.ceit.ap.fileserver.server.Server;

public class MainWindowController {
    private MainWindow window;
    private Server server;

    public MainWindowController() {
        window = new MainWindow();

        window.startBtn.addActionListener(e -> {
            int port = Integer.valueOf(window.portTxt.getText());
            int splitSize = Integer.valueOf(window.splitTxt.getText()) * 1024 * 1024;
            int redundancy = Integer.valueOf(window.redundancyTxt.getText());
            server.start(port, splitSize, redundancy);
        });

        window.stopBtn.addActionListener(e->server.stop());
    }
}
