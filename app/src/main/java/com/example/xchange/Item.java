package com.example.xchange;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.xchange.database.ImageConverter;

import java.io.File;
import java.util.ArrayList;

@Entity(tableName = "items")
public class Item {
    @PrimaryKey(autoGenerate = true)
    private Long itemId;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "item_description")
    private String itemDescription;

    @ColumnInfo(name = "item_category")
    private String itemCategory;

    @ColumnInfo(name = "item_condition")
    private String itemCondition;

    @TypeConverters(ImageConverter.class)
    private ArrayList<Image> itemImages;

    private String xchanger;

    // Default constructor for Room
    public Item() {
    }

    // Constructor for creating objects
    public Item(String xchanger, String itemName, String itemDescription, String itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        this.xchanger = xchanger;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemCondition = itemCondition;
        this.itemImages = itemImages;
    }

    // Getters and setters

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getxChanger() {
        return xchanger;
    }

    public String getXchanger() {
        return xchanger;
    }
    public void setXchanger(String xchanger) {
        this.xchanger = xchanger;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemCondition() {
        return itemCondition;
    }

    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    public ArrayList<Image> getItemImages() {
        return itemImages;
    }

    public void setItemImages(ArrayList<Image> itemImages) {
        this.itemImages = itemImages;
    }

    // Add a single image to the list
    public void addItemImage(Image image) {
        if (this.itemImages == null) {
            this.itemImages = new ArrayList<>();
        }
        this.itemImages.add(image);
    }

    // Add images from file paths
    public void addImagesFromFilePaths(ArrayList<String> filePaths) {
        if (this.itemImages == null) {
            this.itemImages = new ArrayList<>();
        }

        for (String filePath : filePaths) {
            try {
                File imgFile = new File(filePath);
                if (imgFile.exists()) {
                    Image image = new Image(filePath, "Image description");
                    this.itemImages.add(image);
                } else {
                    System.err.println("File does not exist: " + filePath);
                }
            } catch (Exception e) {
                System.err.println("Error loading image from file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    // Edit Item details including images
    public void editItem(String itemName, String itemDescription, String itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        this.setItemName(itemName);
        this.setItemDescription(itemDescription);
        this.setItemCategory(itemCategory);
        this.setItemCondition(itemCondition);
        this.setItemImages(itemImages);
    }
}
