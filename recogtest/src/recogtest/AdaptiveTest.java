package recogtest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class AdaptiveTest {
    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat src = Imgcodecs.imread("sysout/test.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println("The image has " + src.channels() + " channel(s)");
        Mat dst1 = src.clone();
        Mat dst2 = src.clone();
        Imgproc.adaptiveThreshold(src, dst1, 255.0, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 45, -3.0);
        Imgproc.threshold(src, dst2, 0.0, 255.0, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        Imgcodecs.imwrite("sysout/adaptivetest1.jpg", dst1);
        Imgcodecs.imwrite("sysout/adaptivetest2.jpg", dst2);
        System.out.println("File saved!");
        System.exit(0);
    }
}
