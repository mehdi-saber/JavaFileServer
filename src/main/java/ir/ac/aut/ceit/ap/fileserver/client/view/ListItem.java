package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FSPath;
import ir.ac.aut.ceit.ap.fileserver.file.FileCategory;
import ir.ac.aut.ceit.ap.fileserver.util.icon.IconUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

class ListItem extends JPanel {
    private final JLabel nameLabel;
    private FSPath info;

    ListItem(FSPath info) {
        super();
        this.info = info;

        nameLabel = new JLabel(createName(true));
        JLabel image = new JLabel();
        try {
            image.setIcon(IconUtil.getImageIcon(32, 32, getIconPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setAlignmentX(CENTER_ALIGNMENT);
        nameLabel.setAlignmentX(CENTER_ALIGNMENT);
        add(image);
        add(nameLabel);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        switchMode(false);
    }

    FSPath getInfo() {
        return info;
    }

    private String createName(boolean emphasise) {
        String name = info.getName().replaceAll("(.{10})", "$1<br>");
        name = emphasise && name.length() > 20 ? name.substring(0, 20) + "..." : name;
        name = "<html>" + name + "</html>";
        return name;
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

    void switchMode(boolean selected) {
        if (selected) {
            setBorder(new LineBorder(Color.BLACK));
            nameLabel.setText(createName(false));
        } else {
            setBorder(new EmptyBorder(1, 1, 1, 1));
            nameLabel.setText(createName(true));
        }
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
