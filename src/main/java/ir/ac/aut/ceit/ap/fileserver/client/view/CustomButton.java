package ir.ac.aut.ceit.ap.fileserver.client.view;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class CustomButton extends JLabel {

    public CustomButton(int padding) {
        Border paddingBorder = new EmptyBorder(0, padding / 2, 0, padding / 2);
        setBorder(paddingBorder);
    }
}