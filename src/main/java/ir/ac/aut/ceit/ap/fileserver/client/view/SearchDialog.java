package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.file.FSPath;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class SearchDialog extends JDialog {
    public SearchDialog(Map<FSPath, String> found) {
        setModal(true);
        setTitle("Search");
        setLayout(new GridLayout(0, 2));

        for (Map.Entry<FSPath, String> entry : found.entrySet()) {
            add(new JLabel(entry.getKey().getAbsolutePath()));
            add(new JLabel(entry.getValue()));
        }

        setSize(300, 250);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public Insets getInsets() {
        return new Insets(10, 10, 10, 10);
    }
}
