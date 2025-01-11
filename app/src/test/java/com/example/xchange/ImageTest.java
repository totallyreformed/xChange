package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageTest {

    private Image image;

    @BeforeEach
    void setUp() {
        image = new Image("path/to/image.jpg", "desc");
    }

    @Test
    void testGettersSettersToString() {
        assertEquals("path/to/image.jpg", image.getFilePath());
        assertEquals("desc", image.getDescription());

        image.setFilePath("new/path.jpg");
        image.setDescription("new desc");
        assertEquals("new/path.jpg", image.getFilePath());
        assertEquals("new desc", image.getDescription());

        String expected = "Image{" +
                "filePath='new/path.jpg'" +
                ", description='new desc'" +
                '}';
        assertEquals(expected, image.toString());
    }
}
