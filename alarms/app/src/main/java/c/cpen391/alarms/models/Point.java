package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Point {
    @SerializedName("x")
    private float x;
    @SerializedName("y")
    private float y;

    public float getX() {return this.x;}

    public float getY() {return this.y;}

}
