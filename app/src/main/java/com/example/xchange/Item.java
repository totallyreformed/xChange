package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.xchange.database.ImageConverter;
import com.example.xchange.database.CategoryConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

@Entity(tableName = "items")
public class Item implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long itemId;

    @ColumnInfo(name = "item_name")
    private String itemName;

    @ColumnInfo(name = "item_description")
    private String itemDescription;

    @ColumnInfo(name = "item_category")
    @TypeConverters(CategoryConverter.class)
    private Category itemCategory;

    @ColumnInfo(name = "item_condition")
    private String itemCondition;

    @TypeConverters(ImageConverter.class)
    private ArrayList<Image> itemImages;

    private String xchanger;

    // Default constructor for Room
    public Item() {}

    // Constructor for creating objects
    public Item(String xchanger, String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        this.xchanger = xchanger;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemCondition = itemCondition;
        this.itemImages = itemImages;
    }

    // Getters and Setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
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

    public Category getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(Category itemCategory) {
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

    public void addItemImage(Image image) {
        if (this.itemImages == null) {
            this.itemImages = new ArrayList<>();
        }
        this.itemImages.add(image);
    }

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

    public void editItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        this.setItemName(itemName);
        this.setItemDescription(itemDescription);
        this.setItemCategory(itemCategory);
        this.setItemCondition(itemCondition);
        this.setItemImages(itemImages);
    }

    // Parcelable Implementation
    protected Item(Parcel in) {
        if (in.readByte() == 0) {
            itemId = null;
        } else {
            itemId = in.readLong();
        }
        itemName = in.readString();
        itemDescription = in.readString();
        itemCategory = (Category) in.readSerializable(); // Assuming Category is Serializable
        itemCondition = in.readString();
        itemImages = in.createTypedArrayList(Image.CREATOR); // Assuming Image is Parcelable
        xchanger = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (itemId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(itemId);
        }
        dest.writeString(itemName);
        dest.writeString(itemDescription);
        dest.writeSerializable(itemCategory); // Assuming Category is Serializable
        dest.writeString(itemCondition);
        dest.writeTypedList(itemImages); // Assuming Image is Parcelable
        dest.writeString(xchanger);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
    public Image getFirstImage() {
        if (itemImages != null && !itemImages.isEmpty()) {
            return itemImages.get(0);
        }
        return null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return itemId == item.itemId; // Compare using unique ID
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId); // Hash based on unique ID
    }

    @Override
    public String toString() {
        return itemName; // Επιστρέφουμε το όνομα του αντικειμένου
    }
}
