package MainGUI;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Process {

    public Double p_data(String filename, JFrame jFrame, Double minarea,
                         Double maxarea, Double whratio, Integer mode,
                         Double c_zero, Double stdvalue) throws IOException {
        //Load Library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Controller.controller(10, "Load Library");
        //Read the Image
        Mat o_image = Imgcodecs.imread(filename, Imgcodecs.IMWRITE_JPEG_OPTIMIZE);
        if (o_image.empty()) {
            JOptionPane.showMessageDialog(jFrame, "Oops! Image Error!");
            Pmonitor.frame.dispose();
            return null;
        } else {
            Mat o_image_BGR = new Mat();
            Imgproc.cvtColor(o_image, o_image_BGR, Imgproc.COLOR_BGR2RGB);
            Controller.controller(15, "Reading Image");
            //Image Processing
            List<Mat> channel = new ArrayList<>();
            List<MatOfPoint> points = new ArrayList<>();
            Mat dpoints = new Mat();
            Core.split(o_image, channel);
            Mat o_image_c = channel.get(mode - 1);
            Mat p_image_c = o_image_c.clone();
            Controller.controller(20, "Processing Image");
            Imgproc.threshold(o_image_c, p_image_c, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            Imgproc.findContours(p_image_c, points, dpoints, Imgproc.RETR_CCOMP, Imgproc.CV_LINK_RUNS);
            Controller.controller(30, "Processing Image");
            //Remove the error value
            int maxareaIdx = 0;
            int minareaIdx = 0;
            int ratioareaIdx = 0;
            for (int i = 0; i < points.size(); i++) {
                double tmparea = Math.abs(Imgproc.contourArea(points.get(i)));
                if (tmparea > maxarea) {
                    points.remove(i);
                    maxarea = tmparea;
                    maxareaIdx = i;
                    System.out.println("The area " + maxareaIdx + " has been removed [Exceed the maximum value set]");
                    continue;
                }
                if (tmparea < minarea) {
                    points.remove(i);
                    minarea = tmparea;
                    minareaIdx = i;
                    System.out.println("The area " + minareaIdx + " has been removed [Exceed the minimum value set]");
                    continue;
                }
                Rect arect = Imgproc.boundingRect(points.get(i));
                if ((arect.width / arect.height) < whratio) {
                    points.remove(i);
                    continue;
                }
            }
            if (points.isEmpty()) {
                JOptionPane.showMessageDialog(jFrame, "Oops! Fatal error!");
                System.exit(1);
            }
            Controller.controller(40, "Processing Image");
            Mat o_image_mask = Mat.zeros(p_image_c.size(), CvType.CV_8U);
            Mat c_image = Mat.zeros(o_image.size(), CvType.CV_8UC4);
            Imgproc.drawContours(o_image_mask, points, -1, new Scalar(255), Core.FILLED);
            o_image.copyTo(c_image, o_image_mask);
            BufferedImage BufImg = Mat2BufImg(c_image, ".png");
            byte[] c_alpha_image = makeColorTransparent(BufImg, Color.black);
            outfile(filename, c_alpha_image);
            Controller.controller(50, "Processing Image");
            //Output value
            MatOfDouble o_average = new MatOfDouble();
            MatOfDouble o_std = new MatOfDouble();
            Core.meanStdDev(o_image, o_average, o_std, o_image_mask);
            double[] average = o_average.toArray();
            double[] std = o_std.toArray();
            double corr_mean = 0.45;
            double hsv_v = Math.max(Math.max(average[0], average[1]), average[2]) / 255;
            double quotient = corr_mean / hsv_v;
            double[] correction = new double[3];
            System.out.println("The HSV channel is: " + hsv_v);
            System.out.println("The quotient is: " + quotient);
            correction[0] = average[0] * quotient;
            correction[1] = average[1] * quotient;
            correction[2] = average[2] * quotient;
            double meanstd_d = (std[0] + std[1] + std[2]) / 3;
            Double meanstd_b = new BigDecimal(meanstd_d).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
            Imshow o_show = new Imshow(o_image_BGR, "Original File");
            o_show.imshow();
            Controller.controller(60, "Processing Image");
            if (meanstd_b > stdvalue) {
                JOptionPane.showMessageDialog(jFrame, "The output value may not be deviated");
            }
            if (mode == 1) {
                if (correction[mode - 1] >= c_zero) {
                    System.out.println("The correction value is: " + correction[mode - 1]);
                    Controller.controller(70, "Processing Image");
                    return correction[mode - 1];
                } else {
                    return 255.0;
                }
            }
            if (mode == 2) {
                if (correction[mode - 1] >= c_zero) {
                    System.out.println("The correction value is: " + correction[mode - 1]);
                    Controller.controller(70, "Processing Image");
                    return correction[mode - 1];
                } else {
                    return 255.0;
                }
            }
            if (mode == 3) {
                if (correction[mode - 1] >= c_zero) {
                    System.out.println("The correction value is: " + correction[mode - 1]);
                    Controller.controller(70, "Processing Image");
                    return correction[mode - 1];
                } else {
                    return 255.0;
                }
            } else {
                System.exit(1);
                System.out.println("Oops! Fatal Error!");
                return 254.0;
            }
        }
    }

    //Core.Mat translate to BufferedImage (type: BufferedImage)
    public static BufferedImage Mat2BufImg(Mat matrix, String fileExtension) {
        // convert the matrix into a matrix of bytes appropriate for
        // this file extension
        MatOfByte mob = new MatOfByte();
        Imgcodecs.imencode(fileExtension, matrix, mob);
        // convert the "matrix of bytes" into a byte array
        byte[] byteArray = mob.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufImage = ImageIO.read(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufImage;
    }

    //BufferedImage specific color to alpha (type: byte[])
    public static byte[] makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        Image toolkitImage = Toolkit.getDefaultToolkit().createImage(ip);

        BufferedImage bi = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(toolkitImage, 0, 0, im.getWidth(), im.getHeight(), null);
        g2d.dispose();

        byte[] pngByte = null;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ImageIO.write(bi, "png", os);
            pngByte = os.toByteArray();
        } catch (Exception e) {
        }
        return pngByte;
    }

    //Output file
    public static void outfile(String filename, byte[] alphaimagebyte) throws IOException {
        File file = new File(filename);
        String filepath = file.getParent();
        FileOutputStream fos = new FileOutputStream(filepath + "/o_alpha.png");
        fos.write(alphaimagebyte);
        fos.close();
    }
}
