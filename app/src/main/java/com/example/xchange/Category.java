// File: Category.java

package com.example.xchange;

/**
 * Enum representing the available categories for items.
 */
public enum Category {
    ALL("All"),
    TECHNOLOGY("Technology"),
    BOOKS("Books"),
    FASHION("Fashion"),
    HOME("Home Appliances"),
    SPORTS("Sports"),
    FURNITURE("Furniture"),
    TOYS("Toys");
    // Add more categories as needed

    private final String displayName;

    /**
     * Constructor for the Category enum.
     *
     * @param displayName The display name of the category.
     */
    Category(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Retrieves the display name of the category.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Maps a display name to its corresponding Category enum.
     *
     * @param displayName The display name to map.
     * @return The corresponding Category enum, or ALL if no match is found.
     */
    public static Category fromDisplayName(String displayName) {
        for (Category category : Category.values()) {
            if (category.displayName.equalsIgnoreCase(displayName)) {
                return category;
            }
        }
        return ALL; // Default category if no match is found
    }

    @Override
    public String toString() {
        return displayName;
    }
}
