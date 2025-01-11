package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

/**
 * Represents a notification in the xChange application.
 * <p>
 * Notifications are used to inform users about updates or events related to their activity on the platform.
 * This class is a Room entity and implements {@link Parcelable} for data transfer and persistence.
 * </p>
 */
@Entity(tableName = "notifications")
public class Notification implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "timestamp")
    private SimpleCalendar timestamp;

    @ColumnInfo(name = "xchange_id") // New field to store the associated xChange ID
    private Long xChangeId;

    /**
     * Constructs a new {@link Notification}.
     *
     * @param username  The username of the recipient.
     * @param message   The notification message.
     * @param timestamp The timestamp of the notification, represented as a {@link SimpleCalendar}.
     * @param xChangeId The ID of the associated xChange, if any.
     */
    public Notification(String username, String message, SimpleCalendar timestamp, Long xChangeId) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
        this.xChangeId = xChangeId;
    }

    /**
     * Gets the unique ID of the notification.
     *
     * @return The notification ID.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the notification.
     *
     * @param id The ID to set.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets the username of the recipient.
     *
     * @return The username of the recipient.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the recipient.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the notification message.
     *
     * @return The notification message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the notification message.
     *
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the timestamp of the notification.
     *
     * @return The notification timestamp as a {@link SimpleCalendar}.
     */
    public SimpleCalendar getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp of the notification.
     *
     * @param timestamp The timestamp to set as a {@link SimpleCalendar}.
     */
    public void setTimestamp(SimpleCalendar timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets the ID of the associated xChange, if any.
     *
     * @return The associated xChange ID, or {@code null} if not set.
     */
    public Long getXChangeId() {
        return xChangeId;
    }

    /**
     * Sets the ID of the associated xChange.
     *
     * @param xChangeId The xChange ID to set.
     */
    public void setXChangeId(Long xChangeId) {
        this.xChangeId = xChangeId;
    }

    /**
     * Constructs a {@link Notification} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link Notification}.
     */
    protected Notification(Parcel in) {
        id = in.readLong();
        username = in.readString();
        message = in.readString();
        timestamp = in.readParcelable(SimpleCalendar.class.getClassLoader());
        if (in.readByte() == 0) {
            xChangeId = null;
        } else {
            xChangeId = in.readLong();
        }
    }

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link Notification} objects from a {@link Parcel}.
     */
    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    /**
     * Writes the {@link Notification} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(username);
        dest.writeString(message);
        dest.writeParcelable(timestamp, flags);
        if (xChangeId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(xChangeId);
        }
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
}
