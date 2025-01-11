package com.example.xchange;

import android.os.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class ItemTest {

    private final SimpleCalendar cal = new SimpleCalendar("2025-01-05");

    private xChanger createXChanger(String username) {
        return new xChanger(username, username + "@example.com", cal, "pass", "Loc");
    }

    @Test
    void testGettersSettersAndAddMethods() {
        Item item = new Item(createXChanger("owner").getUsername(), "Item1", "Description",
                Category.TOYS, "New", null);
        item.setItemId(101L);
        assertEquals(101L, item.getItemId());
        assertEquals("Item1", item.getItemName());
        assertEquals("Description", item.getItemDescription());
        assertEquals(Category.TOYS, item.getItemCategory());
        assertEquals("New", item.getItemCondition());

        Image img = new Image("path1", "desc1");
        item.addItemImage(img);
        assertNotNull(item.getItemImages());
        assertEquals(1, item.getItemImages().size());
        assertEquals(img, item.getItemImages().get(0));

        assertEquals(img, item.getFirstImage());

        File tempFile;
        try {
            tempFile = File.createTempFile("test", ".jpg");
            tempFile.deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> paths = new ArrayList<>(Arrays.asList(tempFile.getAbsolutePath(), "nonexistent.jpg"));
        item.addImagesFromFilePaths(paths);
        int count = (item.getItemImages() != null) ? item.getItemImages().size() : 0;
        assertTrue(count >= 2);

        Image newImg = new Image("pathNew", "new desc");
        ArrayList<Image> newImgs = new ArrayList<>();
        newImgs.add(newImg);
        item.editItem("Item2", "NewDesc", Category.BOOKS, "Used", newImgs);
        assertEquals("Item2", item.getItemName());
        assertEquals("NewDesc", item.getItemDescription());
        assertEquals(Category.BOOKS, item.getItemCategory());
        assertEquals("Used", item.getItemCondition());
        assertEquals(newImgs, item.getItemImages());
    }

    @Test
    void testEqualsHashCodeAndToString() {
        Item item1 = new Item("owner", "TestItem", "Desc", Category.HOME, "Old", null);
        item1.setItemId(500L);
        Item item2 = new Item("owner", "TestItem", "Desc", Category.HOME, "Old", null);
        item2.setItemId(500L);
        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());
        assertEquals("TestItem", item1.toString());
    }

    @Test
    void testParcelable() {
        ArrayList<Image> images = new ArrayList<>();
        images.add(new Image("pathA", "descA"));
        Item original = new Item("owner", "ParcelItem", "ParcelDesc", Category.SPORTS, "New", images);
        original.setItemId(600L);
        Parcel parcel = Parcel.obtain();
        original.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Item created = Item.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(original.getItemId(), created.getItemId());
        assertEquals(original.getItemName(), created.getItemName());
        assertEquals(original.getItemDescription(), created.getItemDescription());
        assertEquals(original.getItemCategory(), created.getItemCategory());
        assertEquals(original.getItemCondition(), created.getItemCondition());
        assertEquals(original.getXchanger(), created.getXchanger());
        assertNotNull(created.getItemImages());
        assertEquals(original.getItemImages().size(), created.getItemImages().size());
        if (!original.getItemImages().isEmpty()) {
            assertEquals(original.getItemImages().get(0).getFilePath(), created.getItemImages().get(0).getFilePath());
        }
    }
}
