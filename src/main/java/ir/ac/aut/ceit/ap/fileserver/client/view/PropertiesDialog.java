package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.file.FSDirectory;
import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.server.ClientInfo;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * properties window
 */
class PropertiesDialog extends JDialog {

    PropertiesDialog(FSPath path, LinkedHashMap<Long, List<ClientInfo>> nodes, MainWindowView window) {
        super(window, "Properties");
        setModal(true);

        if (path instanceof FSFile) {
            setLayout(new GridLayout(0, 2));
            FSFile file = (FSFile) path;
            add(new JLabel("File name:"));
            add(new JLabel(file.getName()));
            add(new JLabel("File extension:"));
            add(new JLabel(file.getExtension()));
            add(new JLabel("Size:"));
            String size = humanReadableByteCount(file.getSize(), true);
            add(new JLabel(size));
            add(new JLabel("Created By:"));
            add(new JLabel(file.getCreator()));
            add(new JLabel("Partitions:"));
            add(new JLabel(file.getParts().size() + ""));
            add(new JLabel("Distribution:"));
            StringBuilder dist = new StringBuilder("[");
            for (Map.Entry<Long, List<ClientInfo>> entry : nodes.entrySet()) {
                String partId = entry.getKey() + "";
                for (ClientInfo client : entry.getValue())
                    dist.append(partId).append(":").append(client.getId()).append(",");
            }
            dist.append("]");
            add(new JLabel(dist.toString()));
            add(new JLabel("Created Date:"));
            add(new JLabel(file.getCreatedDate().toString()));
            add(new JLabel("Last Date:"));
            add(new JLabel(file.getLastAccessDate().toString()));
        } else if (path instanceof FSDirectory) {
            setLayout(new GridLayout(0, 2));
            add(new JLabel("Folder name:"));
            add(new JLabel(path.getName()));
        }
        setSize(300, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public Insets getInsets() {
        return new Insets(20, 20, 20, 20);
    }
}
