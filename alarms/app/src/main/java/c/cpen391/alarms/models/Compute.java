package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Compute {
    @SerializedName("average_hr")
    private HeartRate average_hr;
    @SerializedName("average_ol")
    private Oxygen average_ol;

    public HeartRate getAverageHR() {
        return this.average_hr;
    }

    public Oxygen getAverageOxy() {return this.average_ol;}

}
