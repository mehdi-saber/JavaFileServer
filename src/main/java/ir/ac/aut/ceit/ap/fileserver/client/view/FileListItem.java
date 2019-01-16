package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.filesys.FileCategory;
import ir.ac.aut.ceit.ap.fileserver.filesys.FileInfo;
import ir.ac.aut.ceit.ap.fileserver.filesys.PathInfo;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileListItem extends JButton {
    private PathInfo info;

    public FileListItem(PathInfo info) {
        super(info.getName());
        this.info = info;
        switchToNormal();

        setHorizontalAlignment(SwingConstants.LEFT);
        setIconTextGap(10);
        setIcon(getImageIcon());
    }

    private String getIconPath() {
        if (info instanceof FileInfo) {
            for (FileCategory cat : FileCategory.values())
                for (String ext : cat.getExtensions())
                    if (((FileInfo) info).getExtension().equals(ext))
                        return cat.getIconPath();
        } else
            return "src/main/resources/folder.png";
        return "src/main/resources/files.png";
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
        super.setBorder(new CompoundBorder(margin, new CompoundBorder(border, margin)));
    }

    @Override
    public Dimension getMaximumSize() {
        Dimension d = super.getMaximumSize();
        d.width = Integer.MAX_VALUE;
        return d;
    }

    private ImageIcon getImageIcon() {
        int w, h;
        w = h = 32;
        BufferedImage srcImg = null;
        try {
            srcImg = ImageIO.read(new File(getIconPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }
}
