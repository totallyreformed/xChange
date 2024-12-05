package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ImageTest {

    private Image image;

    @BeforeEach
    public void setUp() {
        image = new Image("path/to/image.jpg", "A sample description");
    }

    @Test
    public void testImageInitialization() {
        assertNotNull(image); // Ensure the image object is created
        assertEquals("path/to/image.jpg", image.getFilePath()); // Check file path
        assertEquals("A sample description", image.getDescription()); // Check description
    }

    @Test
    public void testSetFilePath() {
        image.setFilePath("new/path/to/image.jpg");
        assertEquals("new/path/to/image.jpg", image.getFilePath()); // Verify file path update
    }

    @Test
    public void testSetDescription() {
        image.setDescription("Updated description");
        assertEquals("Updated description", image.getDescription()); // Verify description update
    }

    @Test
    public void testToString() {
        String expected = "Image{filePath='path/to/image.jpg', description='A sample description'}";
        assertEquals(expected, image.toString()); // Verify `toString` format
    }

    @Test
    public void testNullFilePath() {
        image.setFilePath(null);
        assertNull(image.getFilePath()); // Ensure null value is handled correctly
    }

    @Test
    public void testNullDescription() {
        image.setDescription(null);
        assertNull(image.getDescription()); // Ensure null value is handled correctly
    }

    @Test
    public void testEmptyFilePath() {
        image.setFilePath("");
        assertEquals("", image.getFilePath()); // Ensure empty string is handled
    }

    @Test
    public void testEmptyDescription() {
        image.setDescription("");
        assertEquals("", image.getDescription()); // Ensure empty string is handled
    }
}
