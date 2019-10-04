package MainGUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends JFrame {

    private JPanel rootwindow;
    private JLabel label1;
    private JTextField textField1;
    private JButton recognizeButton;
    private JButton exitButton;
    private JButton chooseFileButton;
    public ActionListener pacl;
    public Process process;
    public Double processed;
    private Chart showchart;
    private Double zero = 12.342;

    public static JFrame jFrame = new MainActivity("FluorescentGUI V-1.0");
    public static void main(String[] args) {
        jFrame.setVisible(true);
    }

    public MainActivity(String title) {
        super(title);
        this.setContentPane(rootwindow);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        Dimension frameSize = this.getSize();
        this.setLocation(screenSize.width / 2 - frameSize.width, screenSize.height / 2 - frameSize.height);
        this.pack();
        chooseFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser();
                jfc.setCurrentDirectory(new File("."));
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.setFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));
                int result = jfc.showOpenDialog(rootwindow);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = jfc.getSelectedFile();
                    textField1.setText(file.getAbsolutePath());
                } else {
                    System.out.println("FileChooser Cancel Button Pressed");
                }
                //System.out.println(jfc.getSelectedFile().getName());
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        pacl = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainSwitch();
            }
        };
        recognizeButton.addActionListener(pacl);

    }

    public void MainSwitch() {
        SwingWorker<Void, Void> MainController = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Pmonitor.showui();
                Controller.controller(5, "Initializing...");
                if (textField1.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(jFrame, "Please select the image file first!");
                    Pmonitor.frame.dispose();
                } else {
                    process = new Process();
                    try {
                        processed = process.p_data(textField1.getText(), jFrame, 2000.0, 15000.0,
                                0.0, 3, zero, 60.0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (processed == 255.0) {
                        JOptionPane.showMessageDialog(jFrame, "Image out of range!");
                    } else if (processed == 254.0) {
                        JOptionPane.showMessageDialog(jFrame, "Oops! Fatal Error!");
                    } else {
                        showchart = new Chart();
                        showchart.dochart(processed, zero);
                    }
                }
                return null;
            }

            @Override
            protected void process(List<Void> chunks) {
                super.process(chunks);
            }

            @Override
            protected void done() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Pmonitor.frame.dispose();
            }
        };

        MainController.execute();
    }
}