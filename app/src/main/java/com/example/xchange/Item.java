package com.example.xchange;

public class Item {
    private final Long item_id;
    private String item_name;
    private String item_description;
    private String item_category;
    private String item_condition;
    private ArrayList<ImageIO> item_image;

    // Constructor
    Item(Long item_id, String item_name, String item_description, String item_category, String item_condition, String item_location, String item_image, Long user_id) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_description = item_description;
        this.item_category = item_category;
        this.item_condition = item_condition;
        this.item_image = item_image;
        this.user_id = user_id;
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

    public String getItemImage() {
        return item_image;
    }

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

    public void setItemImage(String item_image) {
        this.item_image = item_image;
    }

    public void editItem(String item_name, String item_description, String item_category, String item_condition, String item_image) {
        this.setItemName(item_name);
        this.setItemDescription(item_description);
        this.setItemCategory(item_category);
        this.setItemCondition(item_condition);
        this.setItemImage(item_image);

    }
}
