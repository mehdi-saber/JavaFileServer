package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.file.FSFile;
import ir.ac.aut.ceit.ap.fileserver.file.FileCategory;
import ir.ac.aut.ceit.ap.fileserver.util.icon.IconUtil;
import org.apache.commons.io.FileUtils;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

class PreviewDialog extends JDialog {

    PreviewDialog(JFrame window, FSFile file, File previewFile) {
        super(window, "Preview");
        setSize(500, 400);
        try {
            setModal(true);
            String extension = file.getExtension();
            boolean isTextFile = Stream.of(FileCategory.DOCUMENT.getExtensions()).anyMatch(s -> s.equals(extension));
            boolean isImage = Stream.of(FileCategory.IMAGE.getExtensions()).anyMatch(s -> s.equals(extension));
            boolean isMp4 = Stream.of(FileCategory.VIDEO.getExtensions()).anyMatch(s -> s.equals(extension));

            if (isTextFile) {
                String str = FileUtils.readFileToString(previewFile);
                JTextArea textArea = new JTextArea(str);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setHorizontalScrollBar(null);
                scrollPane.getVerticalScrollBar().setUnitIncrement(16);
                textArea.setLineWrap(true);
                textArea.setEditable(false);
                add(scrollPane);
            } else if (isImage) {
                JLabel imageBox = new JLabel();
                Dimension size = getSize();
                BufferedImage src = ImageIO.read(previewFile);
                int w = size.width, h = size.height;
                imageBox.setIcon(IconUtil.getImageIcon(w, h, src));

                setLayout(new GridLayout());
                GridBagConstraints c = new GridBagConstraints();
                c.weightx = c.weighty = 1;
                add(imageBox);
            } else if (isMp4) {
                EmbeddedMediaPlayerComponent mediaPlayerComponent;
                mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
                setContentPane(mediaPlayerComponent);
                mediaPlayerComponent.getMediaPlayer().playMedia(previewFile.getAbsolutePath());
//                mediaPlayerComponent.getVideoSurface().getSize()
                setBounds(100, 100, 600, 400);
                addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        mediaPlayerComponent.release();
                        System.exit(0);
                    }
                });
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
}
