package diadigi;

import org.apache.commons.lang3.StringUtils;
import org.opencv.core.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        findContours(threshold, contours, hierarchy, RETR_TREE, CHAIN_APPROX_SIMPLE, new Point(0,0));

        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint matOfPoint = contours.get(i);

            if (matOfPoint.rows() > 3) {
                MatOfPoint2f matOfPoint2f = new MatOfPoint2f(matOfPoint.toArray());
                MatOfPoint2f approximatedPoly2f = new MatOfPoint2f();
                approxPolyDP(matOfPoint2f, approximatedPoly2f, 10, true);

                drawContours(image, contours, i, new Scalar(0, 255, 0), 2, 8, hierarchy, 0, new Point(10,10));
                MatOfPoint approximatedPoly = new MatOfPoint(approximatedPoly2f.toArray());
                drawContours(
                        image, Collections.singletonList(approximatedPoly), 0,
                        new Scalar(0, 0, 255), 2, 8, new Mat(),
                        0, new Point(-10,-10));

                System.out.println(matOfPoint2f + ", " + matOfPoint2f.cols() + ", " + matOfPoint2f.rows());
                System.out.println(approximatedPoly + ", " + approximatedPoly.cols() + ", " + approximatedPoly.rows());
            }
        }

        imwrite("test.jpg", image);
    }

    private static String getPathFor(String pathOnClasspath) {
        return StringUtils.removeStart(Main.class.getResource(pathOnClasspath).getPath(), "/");
    }
}
