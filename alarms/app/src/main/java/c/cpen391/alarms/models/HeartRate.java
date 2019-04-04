package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class HeartRate {
    @SerializedName("heart_rate__avg")
    private float AverageHR;

    public float getAverageHR(){
        return this.AverageHR;
    }
}
