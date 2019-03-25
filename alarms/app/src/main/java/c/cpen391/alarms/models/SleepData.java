package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SleepData {

    @SerializedName("id")
    public Integer sleepDataId;

    @SerializedName("oxygen_level")
    public Integer oxygen_level;

    @SerializedName("date")
    public String date;

    @SerializedName("heart_rate")
    public Integer heart_rate;

    public SleepData(Integer sleepDataId, Integer oxygen_level, Integer heart_rate, String date){
        this.sleepDataId = sleepDataId;
        this.oxygen_level = oxygen_level;
        this.heart_rate = heart_rate;
        this.date = date;
    }

    public Integer getOxygenLevel(){
        return oxygen_level;
    }

    public Integer getHeartRate(){
        return heart_rate;
    }

    public String getFormattedDate(){
        String dt = date;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm:ss"));
        return newFormat;
    }

    public String getDay(){
        String dt = date;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("EEEE MM"));
        return newFormat;
    }

    public String getTime(){
        String dt = date;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("hh:mm"));
        return newFormat;
    }
}
