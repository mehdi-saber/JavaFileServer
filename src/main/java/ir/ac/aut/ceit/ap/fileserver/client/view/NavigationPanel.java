package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.util.icon.IconUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class NavigationPanel extends JPanel {
    CustomButton parentBtn;
    CustomButton searchBtn;
    JTextField urlField;
    JTextField searchField;

    public NavigationPanel() {
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(boxLayout);
        parentBtn = new CustomButton(10);
        searchBtn = new CustomButton(10);
        urlField = new JTextField();
        searchField = new JTextField();

        try {
            parentBtn.setIcon(IconUtil.getImageIcon(16, 16, "dis-up.png"));
            searchBtn.setIcon(IconUtil.getImageIcon(20, 20, "search.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        urlField.setEditable(false);

        add(parentBtn);
        add(urlField);
        add(searchField);
        add(searchBtn);
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension dimension = super.getMaximumSize();
        dimension.height = 20;
        return dimension;
    }

    void disableParentBtn() {
        try {
            parentBtn.setIcon(IconUtil.getImageIcon(16, 16, "dis-up.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void enableParentBtn() {
        try {
            parentBtn.setIcon(IconUtil.getImageIcon(16, 16, "up.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
