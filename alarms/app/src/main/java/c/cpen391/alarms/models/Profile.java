package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

import java.net.URL;

public class Profile {

    @SerializedName("user")
    private int user;
    @SerializedName("bio")
    private String bio;
    @SerializedName("name")
    private String name;
    @SerializedName("location")
    private String location;
    @SerializedName("image")
    private URL image;

    public Profile(int user, String bio, String name, String location, URL image) {
        this.user = user;
        this.bio = bio;
        this.name = name;
        this.location = location;
        this.image = image;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "user=" + Integer.toString(user) +
                ", bio='" + bio + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
