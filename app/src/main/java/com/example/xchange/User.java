package com.example.xchange;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Represents a user in the xChange application.
 * <p>
 * This class is a Room entity and implements {@link Parcelable} for data transfer.
 * Users are categorized into three types: "user", "xChanger", and "admin".
 * </p>
 */
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

    /**
     * Constructs a new {@link User}.
     *
     * @param username  The username of the user.
     * @param email     The email address of the user.
     * @param join_Date The date the user joined, represented as a {@link SimpleCalendar}.
     * @param password  The password of the user.
     * @param location  The location of the user.
     * @param user_type The type of user ("user", "xChanger", or "admin").
     */
    public User(String username, String email, SimpleCalendar join_Date, String password, String location, String user_type) {
        this.username = username;
        this.email = email;
        this.join_Date = join_Date;
        this.password = password;
        this.location = location;
        this.user_type = user_type;
    }

    /**
     * Constructs a {@link User} from a {@link Parcel}.
     *
     * @param in The {@link Parcel} containing the serialized {@link User}.
     */
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

    /**
     * A {@link Parcelable.Creator} implementation for creating {@link User} objects from a {@link Parcel}.
     */
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

    /**
     * Gets the unique ID of the user.
     *
     * @return The user ID.
     */
    public Long getUser_id() {
        return user_id;
    }

    /**
     * Sets the unique ID of the user.
     *
     * @param user_id The user ID to set.
     */
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email address of the user.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the user.
     *
     * @param email The email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the join date of the user.
     *
     * @return The join date as a {@link SimpleCalendar}.
     */
    public SimpleCalendar getJoin_Date() {
        return join_Date;
    }

    /**
     * Sets the join date of the user.
     *
     * @param join_Date The join date to set as a {@link SimpleCalendar}.
     */
    public void setJoin_Date(SimpleCalendar join_Date) {
        this.join_Date = join_Date;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the location of the user.
     *
     * @return The location.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the user.
     *
     * @param location The location to set.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the type of the user.
     *
     * @return The user type ("user", "xChanger", or "admin").
     */
    public String getUser_type() {
        return user_type;
    }

    /**
     * Sets the type of the user.
     *
     * @param user_type The user type to set ("user", "xChanger", or "admin").
     */
    public void setUser_type(String user_type) {
        this.user_type = user_type;
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
     * Writes the {@link User} object to a {@link Parcel}.
     *
     * @param dest  The {@link Parcel} to write to.
     * @param flags Additional flags for writing the parcel.
     */
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
