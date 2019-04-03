package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Point {
    @SerializedName("x")
    private Integer x;
    @SerializedName("y")
    private Integer y;

    public Integer getX() {return this.x;}

    public Integer getY() {return this.y;}

}
