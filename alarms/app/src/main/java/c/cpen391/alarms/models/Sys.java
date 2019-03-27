package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
}