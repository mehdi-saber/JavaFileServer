package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;
import java.awt.*;

public class SearchDialog extends JDialog {
    private final JTextField searchTxt;

    public SearchDialog(Frame owner) {
        super(owner, "Search");
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridy = 0;
        c.gridx = 1;
        searchTxt = new JTextField();
        add(searchTxt, c);

        c.gridy = 1;

        JList<JPanel> searchList = new JList<>();
        JScrollPane scrollPane = new JScrollPane(searchList);
        scrollPane.setHorizontalScrollBar(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);


    }
}
