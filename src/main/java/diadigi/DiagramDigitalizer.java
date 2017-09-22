package diadigi;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;

import java.net.URL;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.*;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.*;

public class DiagramDigitalizer {
    static {
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    private static List<Scalar> COLORS = Arrays.asList(
            new Scalar(255, 0, 0),
            new Scalar(0, 255, 0),
            new Scalar(0, 0, 255)
    );

    private static int INDEX_OF_PARENT = 3;

    public static List<MatOfPoint> detectContours(String pathToImage) {
        return getPathFor(pathToImage).map(path -> {
            Mat gray = imread(path, 0);

            String filename =  substringBeforeLast(removeStart(pathToImage, "/") , ".");
            String format =  substringAfterLast(pathToImage , ".");

            imwrite(filename + "-01-gray." + format, gray);

            for (int i = 90; i <= 195; i += 15) {
                Mat threshold = new Mat();
                threshold(gray, threshold, i,255, THRESH_BINARY_INV);
                imwrite(filename + "-02-threshold-" + i + "." + format, threshold);
            }

            Mat threshold = new Mat();
            threshold(gray, threshold, 140,255, THRESH_BINARY_INV);

            List<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            findContours(threshold, contours, hierarchy, RETR_CCOMP, CHAIN_APPROX_SIMPLE, new Point(0,0));

            for (int i = 0; i < contours.size(); i++) {
                Mat originalImage = imread(path);
                drawContours(originalImage, contours, i, COLORS.get(0), 2, 8, hierarchy, 0, new Point(0,0));
                imwrite(filename + "-03-contours-" + i + "." + format, originalImage);
            }

            List<MatOfPoint> approximatedContours = contours.stream()
                    .map(matOfPoint -> {
                        MatOfPoint2f matOfPoint2f = new MatOfPoint2f(matOfPoint.toArray());
                        MatOfPoint2f approximatedPoly2f = new MatOfPoint2f();
                        approxPolyDP(matOfPoint2f, approximatedPoly2f, 10, true);
                        return new MatOfPoint(approximatedPoly2f.toArray());
                    })
                    .collect(toList());

            List<MatOfPoint> result = new ArrayList<>();
            for (int i = 0; i < approximatedContours.size(); i++) {
                int parentIndex = (int) hierarchy.get(0, i)[INDEX_OF_PARENT];
                if (parentIndex < 0) {
                    result.add(approximatedContours.get(i));
                }
            }

            for (int i = 0; i < result.size(); i++) {
                Mat originalImage = imread(path);
                drawContours(originalImage, result, i, COLORS.get(0), 2, 8, hierarchy, 0, new Point(0,0));
                imwrite(filename + "-04-approximated-contours-" + i + "." + format, originalImage);
            }

            return result;
        }).orElse(emptyList());
    }

    private static Optional<String> getPathFor(String pathOnClasspath) {
        if (pathOnClasspath == null) return empty();

        URL resource = DiagramDigitalizer.class.getResource(pathOnClasspath);
        if (resource == null) return empty();

        return of(removeStart(resource.getPath(), "/"));
    }
}
