package MainGUI;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Mat;

public class Imshow extends JPanel implements KeyListener {


    public Imshow(Mat m, String window) {
        super();
        init(m, window);
    }

    //Elements for paint.
    private Mat mat;
    private Mat matbgr;
    private boolean firstPaint = true;
    private BufferedImage out;
    int type;
    private String WINDOW = "";
    private JFrame jframe = new JFrame();
    byte[] data;

    private void Mat2BufIm() {
        mat.get(0, 0, data);
        out.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
    }

    private void init(Mat m, String window) {
        this.mat = m;
        data = new byte[mat.cols() * mat.rows() * (int) mat.elemSize()];

        WINDOW = window;

        if (mat.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;
        out = new BufferedImage(mat.cols(), mat.rows(), type);
        Mat2BufIm();
        jframe.add(this);
        jframe.setSize(800, 600);
        jframe.setTitle(WINDOW);
        jframe.addKeyListener(this);

    }

    @Override
    public void paintComponent(Graphics g) {
        //AutoResize the picture here
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        int w = getWidth();
        int h = getHeight();
        int iw = out.getWidth();
        int ih = out.getHeight();
        double xScale = (double) w / iw;
        double yScale = (double) h / ih;
        double scale = Math.min(xScale, yScale);    // scale to fit
        //Math.max(xScale, yScale);  // scale to fill
        int width = (int) (scale * iw);
        int height = (int) (scale * ih);
        int x = (w - width) / 2;
        int y = (h - height) / 2;
        g2.drawImage(out, x, y, width, height, this);
    }

    public void imshow() {
        if (firstPaint) {
            jframe.setVisible(true);
            firstPaint = false;
        }
        Mat2BufIm();
        this.repaint();
    }


    //Elements for waitKey.
    private static Object mt = new Object();
    private static int lastKey = 0;
    private static int key = 0;

    public static int waitKey(int millisecond) {
        try {
            if (millisecond == 0) {
                synchronized (mt) {
                    mt.wait();
                }


            }
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int ret = -1;
        if (key != lastKey) {
            ret = key;
            lastKey = key;
        }

        return ret;

    }

    @Override
    public void keyPressed(KeyEvent e) {
//      System.out.println(e.getKeyChar() + ", " + e.getKeyCode()
//                + ", " + KeyEvent.getKeyText(e.getKeyCode())
//                + ", isActionKey: " + e.isActionKey());
        synchronized (mt) {
            mt.notifyAll();
        }
        this.key = e.getKeyCode();

    }

    @Override
    public void keyReleased(KeyEvent arg0) {

    }

    @Override
    public void keyTyped(KeyEvent arg0) {

    }


}