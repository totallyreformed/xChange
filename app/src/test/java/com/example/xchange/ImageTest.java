package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
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

    @Test
    void testParcelable() {
        Parcel parcel = Parcel.obtain();
        image.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Image created = Image.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(image.getFilePath(), created.getFilePath());
        assertEquals(image.getDescription(), created.getDescription());
    }
}
