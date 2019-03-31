package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

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

    @SerializedName("game_name")
    public String gameName;

    public Alarm(Integer alarmID, String alarmDescription, String alarmTime, Integer volume, Boolean active, String gameName){
        this.alarmId = alarmID;
        this.alarmDescription = alarmDescription;
        this.alarmTime = alarmTime;
        this.volume = volume;
        this.active = active;
        this.gameName = gameName;
    }

    public Alarm(){
        this.alarmId = 0;
        this.alarmDescription="";
        this.alarmTime=null;
        this.volume = 0;
        this.active = false;
        this.gameName = "";
    }

    public String getAlarmDescription(){
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription){
        this.alarmDescription = alarmDescription;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public void setTime(String alarmTime){
        this.alarmTime = alarmTime;
    }

    public Integer getVolume(){
        return volume;
    }

    public void setVolume(Integer volume){
        this.volume = volume;
    }

    public void setGameName(String gamename) {this.gameName = gamename;}

    public String getFormattedDate(){
        String dt = alarmTime;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("dd/MM/yyy hh:mm:ss"));
        return newFormat;
    }

    public String getGame(){
        return this.gameName;
    }
    public String getDay(){
        String dt = alarmTime;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("EEEE MM"));
        return newFormat;
    }

    public String getTime(){
        String dt = alarmTime;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("hh:mm"));
        return newFormat;
    }

    public String getAlarmTime(){
        return this.alarmTime;
    }

    public String getRelativeDay() {
        Date alarmDate = Date.from(ZonedDateTime.parse(alarmTime).toInstant());

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();

        Calendar tmr = Calendar.getInstance(); // today
        tmr.add(Calendar.DAY_OF_YEAR, +1); // tmr

        cal1.setTime(alarmDate);

        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        if (sameDay){
            return "Today";
        } else if (tmr.get(Calendar.YEAR) == cal1.get(Calendar.YEAR) &&
                tmr.get(Calendar.DAY_OF_YEAR) == cal1.get(Calendar.DAY_OF_YEAR)){
            return "Tomorrow";
        } else {
            return ZonedDateTime.parse(alarmTime).format(DateTimeFormatter.ofPattern("EEEE"));
        }
    }

}
