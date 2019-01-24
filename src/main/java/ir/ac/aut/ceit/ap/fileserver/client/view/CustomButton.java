package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * button with padding
 */
class CustomButton extends JButton {

    CustomButton(int padding, boolean hasBorder) {
        Border paddingBorder;
        if (!hasBorder)
            paddingBorder = new EmptyBorder(0, padding / 2, 0, padding / 2);
        else
            paddingBorder = new LineBorder(Color.BLACK, padding);
        setBorder(paddingBorder);
    }
}
