package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Alarm
{

    @SerializedName("id")
    public Integer alarmId;

    @SerializedName("description")
    public String alarmDescription;

    @SerializedName("alarm_time")
    public String alarmTime;

    @SerializedName("volume")
    public Integer volume;

    @SerializedName("active")
    public Boolean active;

    public Alarm(Integer alarmID, String alarmDescription, String alarmTime, Integer volume, Boolean active){
        this.alarmId = alarmID;
        this.alarmDescription = alarmDescription;
        this.alarmTime = alarmTime;
        this.volume = volume;
        this.active = active;
    }

    public String getAlarmDescription(){
        return alarmDescription;
    }

    public Integer getVolume(){
        return volume;
    }

    public void setVolume(Integer volume){
        this.volume = volume;
    }

}
