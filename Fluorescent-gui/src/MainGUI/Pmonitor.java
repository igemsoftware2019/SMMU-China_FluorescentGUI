package MainGUI;

import javax.swing.*;
import java.awt.*;

public class Pmonitor {
    private static final int MIN_PROGRESS = 0;
    private static final int MAX_PROGRESS = 100;
    public static final JLabel alert = new JLabel();
    public static final JProgressBar progressBar = new JProgressBar();
    public static JFrame frame;
    public static JPanel panel;

    public static void showui() {
        frame = new JFrame("Progress of Process");
        frame.setSize(250, 95);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setUndecorated(true);
        panel = new JPanel();
        progressBar.setPreferredSize(new Dimension(250, 60));
        progressBar.setMinimum(MIN_PROGRESS);
        progressBar.setMaximum(MAX_PROGRESS);

        progressBar.setStringPainted(true);
        panel.add(progressBar);
        panel.add(alert, JPanel.BOTTOM_ALIGNMENT);
        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    public static void setvalue(Integer currentProgress) {
        //ChangeListener
        frame.invalidate();
        progressBar.setValue(currentProgress);
    }
}
