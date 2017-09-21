package diadigi;

import org.apache.commons.lang3.StringUtils;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.imgcodecs.Imgcodecs.*;
import static org.opencv.imgproc.Imgproc.*;

public class Main {
    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) throws IOException {
        Mat image = imread(getPathFor("/IMG_1879_low-res.JPG"));
        Mat gray = imread(getPathFor("/IMG_1879_low-res.JPG"), 0);

        Mat threshold = new Mat();
        threshold(gray, threshold, 180,255,1);
        Imgcodecs.imwrite("threshold.jpg", threshold);
        blur(threshold, threshold, new Size(5,5) );
        Imgcodecs.imwrite("blur.jpg", threshold);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(threshold, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE, new Point(0,0));

        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(0, 255, 0);
            drawContours(image, contours, i, color, 2, 8, hierarchy, 0, new Point(10,10));
        }

        Imgcodecs.imwrite("test.jpg", image);
    }

    private static String getPathFor(String pathOnClasspath) {
        return StringUtils.removeStart(Main.class.getResource(pathOnClasspath).getPath(), "/");
    }
}
