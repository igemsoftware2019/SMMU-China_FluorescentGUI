package recogtest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class Sobel {
    public static void main(String[] args) {

        //Load Library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Read output image
        Mat origin = Imgcodecs.imread("sysout/test.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        Mat sobel = origin.clone();
        Mat sobelx = origin.clone();
        Mat sobely = origin.clone();

        //Determine the file has been read
        if (origin.empty()) {
            System.out.println("Please check the test file!");
        } else {
            //Processing the image
            Size size = new Size(3, 3);
            Imgproc.GaussianBlur(origin, sobel, size, 0);

            //Processing the image with sobel
            Imgproc.Sobel(sobel, sobelx, -1, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT);
            Imgproc.Sobel(sobel, sobely, -1, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT);

            //Output the x/y file
            Imgcodecs.imwrite("sysout/sobelX.jpg", sobelx);
            Imgcodecs.imwrite("sysout/sobelY.jpg", sobely);

            //Processing the merge image
            Core.addWeighted(sobelx, 0.5, sobely, 0.5, 0, sobel);
            Imgcodecs.imwrite("sysout/sobelMerged.jpg", sobel);
            ImageGui sobelM = new ImageGui(sobel, "Sobel Output");
            sobelM.imshow();

            //Determine if the file exist
            File d1 = new File("sysout/sobelMerged.jpg");
            if (d1.exists()) {
                System.out.println("Sobel File saved successfully!");

                //CV_THRESH_OTSV processing image
                Mat sobel2t = Imgcodecs.imread("sysout/sobelMerged.jpg", Imgcodecs.IMREAD_GRAYSCALE);
                Mat Tout = sobel2t.clone();
                Mat Tout1 = sobel2t.clone();
                Mat Tout2 = sobel2t.clone();

                //Core Code
                Imgproc.threshold(sobel2t, Tout1, 0.0, 255.0,
                        Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
                Imgproc.adaptiveThreshold(sobel2t, Tout2, 255.0, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                        Imgproc.THRESH_BINARY, 11, -5.3);
                //Imgproc.threshold(sobel2t,Tout2,0.0,255.0,Imgproc.THRESH_OTSU);
                //Core.addWeighted(Tout1,0.5,Tout2,0.5,0,Tout);
                Imgcodecs.imwrite("sysout/sobelT1(Double).jpg", Tout1);
                Imgcodecs.imwrite("sysout/sobelT2(Single).jpg", Tout2);

                //Determine if the file saved succeed
                File d2 = new File("sysout/sobelT2(Single).jpg");
                if (d2.exists()) {
                    System.out.println("Threshold Files saved successfully!");
                    //Jframe output the image
                    ImageGui sobelT1 = new ImageGui(Tout1, "Thresh Output1(Double)");
                    sobelT1.imshow();
                    ImageGui sobelT2 = new ImageGui(Tout2, "Thresh Output2(Single)");
                    sobelT2.imshow();
                } else {
                    System.out.println("Please Check the code!");
                }
            } else {
                System.out.println("File save failed! Please check the code!");
            }

        }
    }
}
