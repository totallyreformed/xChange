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

/**
 * Represents an item in the xChange application.
 * <p>
 * This class is used to store information about an item, including its name, description, category,
 * condition, and associated images. It is a Room entity and implements {@link Parcelable} for data persistence and transfer.
 * </p>
 */
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

    /**
     * Default constructor for Room.
     */
    public Item() {}

    /**
     * Constructs a new {@link Item}.
     *
     * @param xchanger       The xChanger username associated with the item.
     * @param itemName       The name of the item.
     * @param itemDescription The description of the item.
     * @param itemCategory   The category of the item.
     * @param itemCondition  The condition of the item.
     * @param itemImages     A list of images associated with the item.
     */
    public Item(String xchanger, String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        this.xchanger = xchanger;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemCategory = itemCategory;
        this.itemCondition = itemCondition;
        this.itemImages = itemImages;
    }


    /**
     * Gets the unique ID of the item.
     *
     * @return The item's unique ID.
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * Sets the unique ID of the item.
     *
     * @param itemId The ID to set.
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * Gets the xChanger username associated with the item.
     *
     * @return The xChanger username.
     */
    public String getXchanger() {
        return xchanger;
    }

    /**
     * Sets the xChanger username associated with the item.
     *
     * @param xchanger The xChanger username to set.
     */
    public void setXchanger(String xchanger) {
        this.xchanger = xchanger;
    }

    /**
     * Gets the name of the item.
     *
     * @return The item's name.
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Sets the name of the item.
     *
     * @param itemName The name to set.
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * Gets the description of the item.
     *
     * @return The item's description.
     */
    public String getItemDescription() {
        return itemDescription;
    }

    /**
     * Sets the description of the item.
     *
     * @param itemDescription The description to set.
     */
    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    /**
     * Gets the category of the item.
     *
     * @return The item's category.
     */
    public Category getItemCategory() {
        return itemCategory;
    }

    /**
     * Sets the category of the item.
     *
     * @param itemCategory The category to set.
     */
    public void setItemCategory(Category itemCategory) {
        this.itemCategory = itemCategory;
    }

    /**
     * Gets the condition of the item.
     *
     * @return The item's condition.
     */
    public String getItemCondition() {
        return itemCondition;
    }

    /**
     * Sets the condition of the item.
     *
     * @param itemCondition The condition to set.
     */
    public void setItemCondition(String itemCondition) {
        this.itemCondition = itemCondition;
    }

    /**
     * Gets the list of images associated with the item.
     *
     * @return The list of images.
     */
    public ArrayList<Image> getItemImages() {
        return itemImages;
    }

    /**
     * Sets the list of images associated with the item.
     *
     * @param itemImages The list of images to set.
     */
    public void setItemImages(ArrayList<Image> itemImages) {
        this.itemImages = itemImages;
    }

    /**
     * Adds an image to the item's image list.
     *
     * @param image The {@link Image} to add.
     */
    public void addItemImage(Image image) {
        if (this.itemImages == null) {
            this.itemImages = new ArrayList<>();
        }
        this.itemImages.add(image);
    }

    /**
     * Adds images to the item's image list from a list of file paths.
     *
     * @param filePaths A list of file paths to the images.
     */
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

    /**
     * Edits the item with new details.
     *
     * @param itemName       The new name of the item.
     * @param itemDescription The new description of the item.
     * @param itemCategory   The new category of the item.
     * @param itemCondition  The new condition of the item.
     * @param itemImages     The new list of images.
     */
    public void editItem(String itemName, String itemDescription, Category itemCategory, String itemCondition, ArrayList<Image> itemImages) {
        this.setItemName(itemName);
        this.setItemDescription(itemDescription);
        this.setItemCategory(itemCategory);
        this.setItemCondition(itemCondition);
        this.setItemImages(itemImages);
    }

    /**
     * Constructs an {@link Item} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link Item}.
     */
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

    /**
     * Writes the {@link Item} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Flags for writing the parcel.
     */
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

    /**
     * Describes the contents of the {@link Parcelable} implementation.
     *
     * @return Always returns 0 as no special objects are contained.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link Item} objects from a {@link Parcel}.
     */
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

    /**
     * Gets the first image in the item's image list.
     *
     * @return The first {@link Image}, or {@code null} if the list is empty.
     */
    public Image getFirstImage() {
        if (itemImages != null && !itemImages.isEmpty()) {
            return itemImages.get(0);
        }
        return null;
    }

    /**
     * Checks whether this {@link Item} is equal to another object.
     *
     * @param o The object to compare with.
     * @return {@code true} if the objects are equal; {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;
        return Objects.equals(itemId, item.itemId); // Compare using unique ID
    }

    /**
     * Generates a hash code for the {@link Item}.
     *
     * @return The hash code for the item.
     */
    @Override
    public int hashCode() {
        return Objects.hash(itemId); // Hash based on unique ID
    }

    /**
     * Returns a string representation of the {@link Item}.
     *
     * @return The name of the item.
     */
    @Override
    public String toString() {
        return itemName; // Επιστρέφουμε το όνομα του αντικειμένου
    }
}
