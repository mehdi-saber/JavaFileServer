package ir.ac.aut.ceit.ap.fileserver.util.icon;

import ir.ac.aut.ceit.ap.fileserver.client.Client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IconUtil {
    private static Map<IconKey, ImageIcon> cacheMap = new HashMap<>();

    public static ImageIcon getImageIcon(int w, int h, BufferedImage srcImg) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return new ImageIcon(resizedImg);
    }

    public static ImageIcon getImageIcon(int w, int h, String iconName) {
        IconKey cacheKey = new IconKey(w, h, iconName);
        ImageIcon cacheIcon = cacheMap.get(cacheKey);
        if (cacheIcon != null)
            return cacheIcon;

        BufferedImage srcImg = null;
        try {
            srcImg = ImageIO.read(Client.class.getResourceAsStream("/icon/" + iconName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageIcon imageIcon = getImageIcon(w, h, srcImg);
        cacheMap.put(cacheKey, imageIcon);
        return imageIcon;
    }
}
