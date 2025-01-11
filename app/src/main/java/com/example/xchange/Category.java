package com.example.xchange;

/**
 * Enum representing the available categories for items.
 * <p>
 * Each category has a display name for user-friendly representation.
 * The {@code Category} enum provides utility methods for retrieving the display name,
 * mapping from display names to enum values, and calculating the total number of categories.
 * </p>
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

    /**
     * Returns the display name of the category.
     *
     * @return The display name.
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Returns the total number of defined categories, excluding ALL.
     *
     * @return The count of actual categories.
     */
    public static int getTotalCategories() {
        // Exclude the ALL category if it's a placeholder
        return (int) java.util.Arrays.stream(Category.values())
                .filter(category -> category != ALL)
                .count();
    }
}