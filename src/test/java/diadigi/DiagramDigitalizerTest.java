package diadigi;

import org.junit.jupiter.api.Test;

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
}