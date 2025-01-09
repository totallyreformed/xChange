package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private Long user_id;

    private String username;
    private String email;
    private SimpleCalendar join_Date; // Simplified for Room
    private String password;
    private String location;
    private String user_type; // Discriminator: "user", "xChanger", or "admin"

    // Constructor
    public User(String username, String email, SimpleCalendar join_Date, String password, String location, String user_type) {
        this.username = username;
        this.email = email;
        this.join_Date = join_Date;
        this.password = password;
        this.location = location;
        this.user_type = user_type;
    }

    // Parcelable implementation
    protected User(Parcel in) {
        if (in.readByte() == 0) {
            user_id = null;
        } else {
            user_id = in.readLong();
        }
        username = in.readString();
        email = in.readString();
        join_Date = in.readParcelable(SimpleCalendar.class.getClassLoader());
        password = in.readString();
        location = in.readString();
        user_type = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // Getters and Setters
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SimpleCalendar getJoin_Date() {
        return join_Date;
    }

    public void setJoin_Date(SimpleCalendar join_Date) {
        this.join_Date = join_Date;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (user_id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(user_id);
        }
        dest.writeString(username);
        dest.writeString(email);
        dest.writeParcelable(join_Date, flags);
        dest.writeString(password);
        dest.writeString(location);
        dest.writeString(user_type);
    }
}
