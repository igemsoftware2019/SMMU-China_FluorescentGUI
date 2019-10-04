package recogtest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        //Load library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Read image
        System.out.println("Please copy the origin image to source folder and input the file name first: ");
        Scanner scanner = new Scanner(System.in);
        String filesrc = scanner.nextLine();
        Mat image1 = Imgcodecs.imread("img/" + filesrc, Imgcodecs.IMREAD_COLOR);
        //RGB2BGR
        Imgproc.cvtColor(image1, image1, Imgproc.COLOR_RGB2BGR);

        Mat image1G;
        List<Mat> channels = new ArrayList<>();

        //Determine the file has been read
        if (image1.empty()) {
            System.out.println("Please check the file!");
        } else {
            //System.out.println("test");
            ImageGui imagev = new ImageGui(image1, "Origin");
            imagev.imshow();

            //Split the color channels

            //BGR2RGB
            Imgproc.cvtColor(image1, image1, Imgproc.COLOR_BGR2RGB);

            Core.split(image1, channels);
            image1G = channels.get(2);
            ImageGui imagevG = new ImageGui(image1G, "Processed");
            imagevG.imshow();
            Imawrite imawrite = new Imawrite(image1G);
            System.exit(0);

        }

    }
}