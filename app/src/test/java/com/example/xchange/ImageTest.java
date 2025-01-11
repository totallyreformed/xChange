package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class ImageTest {

    @Test
    void testGettersSettersToString() {
        Image image = new Image("path/to/image.jpg", "desc");
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
        Image original = new Image("path/for/parcel.jpg", "parcel desc");
        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Image created = Image.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getFilePath(), created.getFilePath());
        assertEquals(original.getDescription(), created.getDescription());
    }
}
