package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Oxygen {
    @SerializedName("oxygen_level__avg")
    private float AverageOxygen;

    public float getAverageOxygen(){
        return this.AverageOxygen;
    }
}
