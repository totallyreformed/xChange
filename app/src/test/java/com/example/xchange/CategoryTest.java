package com.example.xchange;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(RobolectricTestRunner.class)
class CategoryTest {

    private Category[] categories;

    @BeforeEach
    void setUp() {
        categories = Category.values();
    }

    @Test
    void testGetDisplayName() {
        assertEquals("All", Category.ALL.getDisplayName());
        assertEquals("Technology", Category.TECHNOLOGY.getDisplayName());
        assertEquals("Books", Category.BOOKS.getDisplayName());
        assertEquals("Fashion", Category.FASHION.getDisplayName());
        assertEquals("Home Appliances", Category.HOME.getDisplayName());
        assertEquals("Sports", Category.SPORTS.getDisplayName());
        assertEquals("Furniture", Category.FURNITURE.getDisplayName());
        assertEquals("Toys", Category.TOYS.getDisplayName());
    }

    @Test
    void testToString() {
        for (Category category : categories) {
            assertEquals(category.getDisplayName(), category.toString());
        }
    }

    @Test
    void testFromDisplayName() {
        assertEquals(Category.TECHNOLOGY, Category.fromDisplayName("Technology"));
        assertEquals(Category.BOOKS, Category.fromDisplayName("books"));
        assertEquals(Category.ALL, Category.fromDisplayName("Nonexistent"));
        assertEquals(Category.ALL, Category.fromDisplayName(null));
    }

    @Test
    void testGetTotalCategories() {
        assertEquals(7, Category.getTotalCategories());
    }
}
