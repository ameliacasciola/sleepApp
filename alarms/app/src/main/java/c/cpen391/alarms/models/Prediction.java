package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Prediction {
    @SerializedName("degree")
    private Integer degree;
    @SerializedName("function")
    private List<Point> points;

    public Integer getDegree() {
        return this.degree;
    }

    public List<Point> getPoints() {return this.points; }

}
