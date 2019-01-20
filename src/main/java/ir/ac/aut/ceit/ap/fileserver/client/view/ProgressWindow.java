package ir.ac.aut.ceit.ap.fileserver.client.view;

import ir.ac.aut.ceit.ap.fileserver.network.ProgressCallback;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class ProgressWindow extends JFrame {
    private ProgressCallback callback;

    private JLabel operationLabel;
    private JLabel number;
    private JProgressBar progressBar;

    private String operationName;
    private int done = 0;
    private int dot = 0;

    ProgressWindow(Frame owner, String operationName, long max) {
        super(operationName);
        setLayout(new BorderLayout());
        this.operationName = operationName;

        operationLabel = new JLabel(operationName);
        number = new JLabel();
        progressBar = new JProgressBar(0, 100);
        JPanel btnPanel = new JPanel(new BorderLayout());

        number.setText("00%");
        number.setBorder(new EmptyBorder(0, 10, 0, 10));
        btnPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        add(BorderLayout.CENTER, progressBar);
        add(BorderLayout.NORTH, operationLabel);
        add(BorderLayout.EAST, number);
//        btnPanel.add(cancelBtn, BorderLayout.EAST);
//        add(BorderLayout.SOUTH, btnPanel);

        callback = doneDelta -> SwingUtilities.invokeLater(() -> {
            done += doneDelta;
            StringBuilder dotStr = new StringBuilder();
            for (int j = 0; j < dot % 4; j++)
                dotStr.append(".");
            dot++;

            operationLabel.setText(this.operationName + dotStr);
            int percent = ((Double) ((double) done / max * 100)).intValue();
            number.setText((percent < 10 ? "0" + percent : percent) + "%");
            progressBar.setValue(percent);
        });

        getRootPane().setBorder(new EmptyBorder(10, 10, 5, 10));
        setSize(300, 110);
        setLocationRelativeTo(owner);
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    ProgressCallback getCallback() {
        return callback;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}