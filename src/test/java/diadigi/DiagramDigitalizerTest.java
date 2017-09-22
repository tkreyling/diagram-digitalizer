package diadigi;

import org.junit.jupiter.api.Test;
import org.opencv.core.MatOfPoint;

import java.util.List;

import static diadigi.DiagramDigitalizer.detectContours;
import static org.junit.jupiter.api.Assertions.*;

class DiagramDigitalizerTest {

    @Test
    void no_contours_detected_if_path_to_image_is_null() {
        assertEquals(0, detectContours(null).size());
    }

    @Test
    void no_contours_detected_if_image_does_not_exist() {
        assertEquals(0, detectContours("/not-existent.jpg").size());
    }

    @Test
    void no_contours_detected_if_image_is_empty() {
        assertEquals(0, detectContours("/empty.jpg").size());
    }

    @Test
    void one_rectangle_detected_if_image_contains_one_rectangle() {
        assertEquals(1, detectContours("/one-rectangle.jpg").size());
    }

    @Test
    void two_rectangles_detected_if_image_contains_two_rectangles() {
        assertEquals(2, detectContours("/two-rectangles.jpg").size());
    }

    @Test
    void two_rectangles_detected_if_image_contains_two_nested_rectangles() {
        assertEquals(2, detectContours("/nested-rectangles.jpg").size());
    }

    @Test
    void three_rectangles_detected_if_image_contains_three_narrow_nested_rectangles() {
        assertEquals(3, detectContours("/narrow-nested-rectangles.jpg").size());
    }

    @Test
    void one_rectangle_detected_if_image_contains_one_hand_drawn_rectangle() {
        List<MatOfPoint> contours = detectContours("/hand-drawn-rectangle.jpg");

        assertEquals(1, contours.size());
        assertEquals(4, contours.get(0).toList().size());
    }

    @Test
    void one_pentagon_detected_if_image_contains_one_pentagon() {
        List<MatOfPoint> contours = detectContours("/one-pentagon.jpg");

        assertEquals(1, contours.size());
        assertEquals(5, contours.get(0).toList().size());
    }

    @Test
    void one_pentagon_detected_if_image_contains_one_hand_drawn_pentagon() {
        List<MatOfPoint> contours = detectContours("/one-hand-drawn-pentagon.jpg");

        assertEquals(1, contours.size());
        assertEquals(5, contours.get(0).toList().size());
    }

    @Test
    void three_rectangles_detected_on_scanned_diagram() {
        List<MatOfPoint> contours = detectContours("/scanned-diagram.jpg");

        assertEquals(7, contours.size());
        contours.forEach(contour -> assertEquals(4, contour.toList().size()));
    }
}