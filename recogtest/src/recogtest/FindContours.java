package recogtest;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindContours {
    public static void main(String[] args) throws IOException {

        //Load Library and create List
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        List<MatOfPoint> points = new ArrayList<>();

        //Import the origin image
        System.out.println("Please input the origin file here:");
        System.out.println("(Please make sure the origin file is matched with the processed file!)");
        Scanner scanner = new Scanner(System.in);
        String originfile = scanner.nextLine();
        Mat origin = Imgcodecs.imread("img/" + originfile, Imgcodecs.IMWRITE_JPEG_OPTIMIZE);
        //System.out.println(origin.channels());
        if (origin.empty()) {
            System.out.println("Please Check the origin image!");
        } else {
            //Read the processed data
            Mat data1 = Imgcodecs.imread("r_data/1.jpg", Imgcodecs.IMREAD_GRAYSCALE);
            Mat data2 = Imgcodecs.imread("r_data/2.jpg", Imgcodecs.IMREAD_GRAYSCALE);
            Mat data3 = Imgcodecs.imread("r_data/3.jpg", Imgcodecs.IMREAD_GRAYSCALE);
            Mat data4 = Imgcodecs.imread("r_data/4.jpg", Imgcodecs.IMREAD_GRAYSCALE);

            //Recognize the contour
            Mat dpoints = new Mat();
            Imgproc.findContours(data2, points, dpoints, Imgproc.RETR_CCOMP, Imgproc.CV_LINK_RUNS);
            System.out.println("The Quantity: " + dpoints.size());
            System.out.println("Types of Data: " + dpoints);

            //Output the points
            File result = new File("sysout/Coordinate_Points.log");
            PrintStream ps = new PrintStream(result);
            ps.println("--------- Coordinate points start here! ---------");
            for (int k = 0; k < dpoints.cols(); k++) {
                ps.println("Contour counts：" + k + "{ ");
                double[] ds = dpoints.get(0, k);
                for (int l = 0; l < ds.length; l++) {
                    switch (l) {
                        case 0:
                            ps.println("Next contour subscript：" + ds[l]);
                            break;
                        case 1:
                            ps.println("previous contour subscript：" + ds[l]);
                            break;
                        case 2:
                            ps.println("Parental contour subscript：" + ds[l]);
                            break;
                        case 3:
                            ps.println("Embedded contour subscript：" + ds[l]);
                            break;
                        default:
                            break;
                    }
                }
                ps.println("}\n");
            }
            ps.println("--------- Coordinate points end here! ---------");
            ps.println("Total: " + dpoints.size());
            ps.close();
            if (result.exists()) {
                System.out.println("Result file saved successfully!");
                System.out.println("LOG File saved at: " + result.getAbsolutePath());
            } else {
                System.out.println("Please check the code!");
            }

            //Output the recognized range
            Mat result1 = Mat.zeros(data2.size(), CvType.CV_8U);
            //System.out.println(result1.channels());
            Scalar color = new Scalar(255);
            Imgproc.drawContours(result1, points, -1, color);
            ImageGui Result = new ImageGui(result1, "Crop Range");
            Result.imshow();
            Imgcodecs.imwrite("sysout/Contours.jpg", result1);
            File file1 = new File("sysout/Contours.jpg");
            if (file1.exists()) {
                System.out.println("Range file saved successfully!");
            } else {
                System.out.println("Range file saved failed!");
            }

            //Process the recognized range
            Mat cutrange = Mat.zeros(data2.size(), CvType.CV_8U);
            Imgproc.drawContours(cutrange, points, -1, color, Core.FILLED);
            Mat cut = Mat.zeros(origin.size(), CvType.CV_8UC4);
            origin.copyTo(cut, cutrange);

            //RGB2BGR
            Imgproc.cvtColor(cut, cut, Imgproc.COLOR_RGB2BGR);

            ImageGui cropped = new ImageGui(cut, "Cropped Image");
            cropped.imshow();

            //BGR2RGB
            Imgproc.cvtColor(cut, cut, Imgproc.COLOR_BGR2RGB);
            Imgcodecs.imwrite("sysout/Cropped.png", cut);
            File file2 = new File("sysout/Cropped.png");
            if (file2.exists()) {
                System.out.println("Cropped file saved successfully!");
            } else {
                System.out.println("Cropped file saved failed!");
            }

            //Transparent the cropped image
            BufferedImage BufImg = Mat2BufImg(cut, ".png");
            byte[] cutalpha = makeColorTransparent(BufImg, Color.BLACK);

            //System.out.println(cutalpha);
            System.out.println("The data size: " + cutalpha.length + " bytes");
            File file4 = new File("sysout/png.log");
            PrintStream ps2 = new PrintStream(file4);
            for (int i = 0; i < cutalpha.length; i++) {
                ps2.println(i + 1 + " " + cutalpha[i]);
            }
            ps2.close();
            System.out.println("PNG Log saved at: " + file4.getAbsolutePath());

            FileOutputStream fos = new FileOutputStream("sysout/cutalpha.png");
            fos.write(cutalpha);
            fos.close();
            File file3 = new File("sysout/cutalpha.png");
            if (file3.exists()) {
                System.out.println("Alpha file saved successfully!");
                System.out.println("Image saved at: " + file3.getAbsolutePath());
            } else {
                System.out.println("Alpha file saved failed! Please check the code!");
            }

            //Output the value

            if (cutrange.empty()) {
                System.out.println("Please check the selected mask!");
            } else {
                File file5 = new File("sysout/RESULT.log");
                PrintStream ps3 = new PrintStream(file5);
                MatOfDouble meanvalue = new MatOfDouble();
                MatOfDouble stdvalue = new MatOfDouble();
                //Core CODE
                Imgproc.cvtColor(origin, origin, Imgproc.COLOR_BGR2RGB);
                Core.meanStdDev(origin, meanvalue, stdvalue, cutrange);
                //System.out.println(meanvalue.get(0, 0));
                double[] mean = meanvalue.toArray();
                double[] std = stdvalue.toArray();
                System.out.println("Average matrix size: " + meanvalue.size());
                System.out.println("Std matrix size: " + stdvalue.size());
                ps3.println("----------RESULT  START  HERE----------" + "\n");
                for (int a = 0; a < meanvalue.rows(); a++) {
                    switch (a) {
                        case 0:
                            ps3.println("[Channel RED]Average value: " + mean[a]);
                            ps3.println("[Channel RED]Standard deviation value: " + std[a]);
                            ps3.println("\n");
                            break;
                        case 1:
                            ps3.println("[Channel GREEN]Average value: " + mean[a]);
                            ps3.println("[Channel GREEN]Standard deviation value: " + std[a]);
                            ps3.println("\n");
                            break;
                        case 2:
                            ps3.println("[Channel BLUE]Average value: " + mean[a]);
                            ps3.println("[Channel BLUE]Standard deviation value: " + std[a]);
                            break;
                        default:
                            break;
                    }
                }
                ps3.println("\n" + "----------RESULT  END  HERE----------");
                ps3.close();
                if (file5.exists()) {
                    System.out.println("Result file saved successfully!");
                    System.out.println("The file saved at: " + file5.getAbsolutePath());

                    if (std[0]>50){
                        System.out.println("*The relevant concentration may not be true*");
                    }
                    Double con = Criteria.criteriafigure(mean[0]);
                    Double concentration = new BigDecimal(con).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
                    System.out.println("The relevant protein concentration is: "+concentration.toString());
                    System.exit(0);
                } else {
                    System.out.println("Please check the code!");
                }
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
}