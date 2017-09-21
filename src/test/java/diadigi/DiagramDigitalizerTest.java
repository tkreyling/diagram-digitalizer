package diadigi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiagramDigitalizerTest {

    @Test
    void no_contours_detected_if_path_to_image_is_null() {
        assertEquals(0, DiagramDigitalizer.detectContours(null).size());
    }
}