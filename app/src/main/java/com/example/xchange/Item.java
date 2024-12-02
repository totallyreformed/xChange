package com.example.xchange;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Item {
    private static Long lastItemId = 0L; // Static field to track the last used ID
    private final Long item_id; // Unique ID for each item
    private String item_name;
    private String item_description;
    private String item_category;
    private String item_condition;
    private Image item_image;

    // Constructor
    public Item(String item_name, String item_description, String item_category, String item_condition, Image item_image) {
        this.item_id = ++lastItemId; // Increment the ID for each new item
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_category = item_category;
        this.item_condition = item_condition;
        this.item_image = item_image;
    }

    // Getters
    public Long getItemId() {
        return item_id;
    }

    public String getItemName() {
        return item_name;
    }

    public String getItemDescription() {
        return item_description;
    }

    public String getItemCategory() {
        return item_category;
    }

    public String getItemCondition() {
        return item_condition;
    }

    public Image getItemImage() {
        return this.item_image;
    }

    // Setters
    public void setItemName(String item_name) {
        this.item_name = item_name;
    }

    public void setItemDescription(String item_description) {
        this.item_description = item_description;
    }

    public void setItemCategory(String item_category) {
        this.item_category = item_category;
    }

    public void setItemCondition(String item_condition) {
        this.item_condition = item_condition;
    }

    public void setItemImages(Image item_image) {
        this.item_image = item_image;
    }


    // Edit Item details including images
    public void editItem(String item_name, String item_description, String item_category, String item_condition, Item item_image) {
        this.setItemName(item_name);
        this.setItemDescription(item_description);
        this.setItemCategory(item_category);
        this.setItemCondition(item_condition);
        this.setItemImages(item_image.getItemImage());
    }
}
