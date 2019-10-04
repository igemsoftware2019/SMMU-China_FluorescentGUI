package recogtest;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class bytetest {
    public static void main(String[] args) throws IOException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        Mat test1 = Imgcodecs.imread("img/test1.png", CvType.CV_8UC4);
        Mat test2 = Imgcodecs.imread("img/test2.png", CvType.CV_8UC4);
        BufferedImage bitest1 = FindContours.Mat2BufImg(test1, ".png");
        BufferedImage bitest2 = FindContours.Mat2BufImg(test2, ".png");
        byte[] btest1 = FindContours.makeColorTransparent(bitest1, Color.BLACK);
        byte[] btest2 = FindContours.makeColorTransparent(bitest2, Color.BLACK);
        File ftest1 = new File("sysout/ftest1.log");
        File ftest2 = new File("sysout/ftest2.log");
        PrintStream ps1 = new PrintStream(ftest1);
        PrintStream ps2 = new PrintStream(ftest2);
        for (int a = 0; a < btest1.length; a++) {
            ps1.println(a + 1 + " btest1 " + btest1[a]);
            ps2.println(a + 1 + " btest2 " + btest2[a]);
        }
        ps1.close();
        ps2.close();
        Imgcodecs.imwrite("sysout/test1.png", test1);
        Imgcodecs.imwrite("sysout/test2.png", test2);

        MatOfDouble dm = new MatOfDouble();
        dm.fromArray(1,2,3,4,5,6,7,8);
        System.out.println(dm.size());
        System.out.println(dm.get(0, 0));
        System.out.println(dm.rows()+" "+dm.cols());
        double[] a = dm.toArray();
        System.out.println(a.length);
        System.out.println(a[0]);
    }
}
