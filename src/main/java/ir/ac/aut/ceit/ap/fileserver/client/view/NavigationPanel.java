package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.util.icon.IconUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * client main window top panel
 */
class NavigationPanel extends JPanel {
    final JButton parentBtn;
    final JButton refreshBtn;
    final JButton searchBtn;
    final JTextField urlField;
    final JTextField searchField;

    NavigationPanel() {
        setLayout(new GridBagLayout());
        parentBtn = new JButton();
        searchBtn = new JButton();
        refreshBtn = new JButton();
        urlField = new JTextField();
        searchField = new JTextField();

        parentBtn.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 0, 5),
                new LineBorder(Color.BLACK)
        ));
        urlField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK),
                new EmptyBorder(0, 10, 0, 10)
        ));
        refreshBtn.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(0, 0, 0, 5),
                new LineBorder(Color.BLACK)
        ));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK),
                new EmptyBorder(0, 5, 0, 5)
        ));
        searchBtn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK),
                new EmptyBorder(0, 5, 0, 5)
        ));
        setBorder(new EmptyBorder(5, 5, 5, 5));

        parentBtn.setIcon(IconUtil.getImageIcon(20, 20, "dis-up.png"));
        searchBtn.setIcon(IconUtil.getImageIcon(16, 16, "search.png"));
        refreshBtn.setIcon(IconUtil.getImageIcon(16, 16, "refresh.png"));
        urlField.setEditable(false);

        GridBagConstraints c = new GridBagConstraints();
        add(parentBtn);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.8;
        add(urlField, c);
        c.weightx = 0.2;
        add(refreshBtn);
        add(searchField, c);
        add(searchBtn);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension dimension = super.getMaximumSize();
        dimension.height = 20;
        return dimension;
    }

    void disableParentBtn() {
        parentBtn.setIcon(IconUtil.getImageIcon(16, 16, "dis-up.png"));
    }

    void enableParentBtn() {
        parentBtn.setIcon(IconUtil.getImageIcon(16, 16, "up.png"));
    }
}
