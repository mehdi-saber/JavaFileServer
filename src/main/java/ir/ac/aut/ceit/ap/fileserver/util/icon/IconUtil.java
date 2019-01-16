package ir.ac.aut.ceit.ap.fileserver.util.icon;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IconUtil {
    private static Map<IconKey, ImageIcon> cacheMap = new HashMap<>();

    public static ImageIcon getImageIcon(int w, int h, String iconName) throws IOException {
        IconKey cacheKey = new IconKey(w, h, iconName);
        ImageIcon cacheIcon = cacheMap.get(cacheKey);
        if (cacheIcon != null)
            return cacheIcon;

        BufferedImage srcImg = ImageIO.read(new File("src/main/resources/icon/" + iconName));

        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        ImageIcon imageIcon = new ImageIcon(resizedImg);
        cacheMap.put(cacheKey, imageIcon);
        return imageIcon;
    }
}
