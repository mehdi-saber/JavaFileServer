package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.filesys.FileCategory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSFile;
import ir.ac.aut.ceit.ap.fileserver.filesys.FSPath;
import ir.ac.aut.ceit.ap.fileserver.util.icon.IconUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

class ListItem extends JPanel {
    private FSPath info;

    ListItem(FSPath info) {
        super();
        this.info = info;
        JLabel label = new JLabel(info.getName());
        JLabel image = new JLabel();
        try {
            image.setIcon(IconUtil.getImageIcon(32, 32, getIconPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setAlignmentX(CENTER_ALIGNMENT);
        label.setAlignmentX(CENTER_ALIGNMENT);
        add(image);
        add(label);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        switchToNormal();
    }

    FSPath getInfo() {
        return info;
    }

    private String getIconPath() {
        if (info instanceof FSFile) {
            for (FileCategory cat : FileCategory.values())
                for (String ext : cat.getExtensions())
                    if (((FSFile) info).getExtension().equals(ext))
                        return cat.getIconPath();
        } else
            return "folder.png";
        return "files.png";
    }

    void switchToHighLight() {
        setBorder(new LineBorder(Color.BLACK));
    }

    void switchToNormal() {
        setBorder(new EmptyBorder(1,1,1,1));
    }

    @Override
    public void setBorder(Border border) {
        Border margin = new EmptyBorder(10, 10, 5, 5);
        super.setBorder(new CompoundBorder(border, margin));
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        d.width = Integer.MAX_VALUE;
        return d;
    }
}
