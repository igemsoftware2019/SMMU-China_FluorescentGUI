package recogtest;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.File;
import java.util.Scanner;

public class Imawrite {

    //A set of way to export the image file

    public Imawrite(Mat m) {

        //Load Library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        //Get the name of the new file
        System.out.println("Please input the file name: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        name = "sysout/" + name + ".jpg";
        //System.out.println(name);

        //Output the file
        Imgcodecs.imwrite(name, m);

        //Determine if the file exists
        File file = new File(name);
        if (file.exists()) {
            System.out.println("File saved successfully!");
        } else {
            System.out.println("Please Check the file name and retry!");
        }


    }
}
