package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import com.example.xchange.xChange;

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

    public Notification(String username, String message, SimpleCalendar timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SimpleCalendar getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(SimpleCalendar timestamp) {
        this.timestamp = timestamp;
    }

    // Parcelable implementation
    protected Notification(Parcel in) {
        id = in.readLong();
        username = in.readString();
        message = in.readString();
        timestamp = in.readParcelable(SimpleCalendar.class.getClassLoader());
    }

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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(username);
        dest.writeString(message);
        dest.writeParcelable(timestamp, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
