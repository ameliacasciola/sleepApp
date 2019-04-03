package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Alarm implements Serializable
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

    @SerializedName("spotify_uri")
    public String spotify_uri;

    public String tiffany_date;

    public void setTiffanyDate(String date){
        this.tiffany_date = date;
    }
    public Alarm(Integer alarmID, String alarmDescription, String alarmTime, Integer volume, Boolean active, String gameName, String spotify_uri){
        this.alarmId = alarmID;
        this.alarmDescription = alarmDescription;
        this.alarmTime = alarmTime;
        this.volume = volume;
        this.active = active;
        this.gameName = gameName;
        this.spotify_uri = spotify_uri;
    }

    public Alarm(){
        this.alarmId = 0;
        this.alarmDescription="";
        this.alarmTime=null;
        this.volume = 0;
        this.active = false;
        this.gameName = "";
        this.spotify_uri = "";
    }

    public Integer getID() {
        return alarmId;
    }

    public String getSpotifyURI(){return this.spotify_uri;}

    public void setSpotifyURI(String spotify_uri) { this.spotify_uri = spotify_uri; }

    public String getAlarmDescription(){
        return alarmDescription;
    }

    public void setAlarmDescription(String alarmDescription){
        this.alarmDescription = alarmDescription;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean getActive() {
        return active;
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

    public String getTiffanyDate(){
        return this.tiffany_date;
    }
    public String getFormattedDate() {
        String dt = alarmTime;
        String newFormat;

        return dt;
    }

    public String getGame(){
        return this.gameName;
    }

    public String getDay(){
        String dt = alarmTime;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("EEEE dd"));
        return newFormat;
    }

    public String getTime(){
        String dt = alarmTime;
        ZonedDateTime zdt = ZonedDateTime.parse(dt);
        String newFormat = zdt.format(DateTimeFormatter.ofPattern("hh:mm a"));
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

    public String getTimeUtil() {
        Date alarmDate = Date.from(ZonedDateTime.parse(alarmTime).toInstant());

        Calendar cal1 = Calendar.getInstance(); // alarmTime
        Calendar cal2 = Calendar.getInstance(); // today

        Calendar tmr = Calendar.getInstance(); // tmr
        tmr.add(Calendar.DAY_OF_YEAR, +1);

        cal1.setTime(alarmDate);

        boolean sameDay = cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
        if (sameDay){ // today
            int dest = (cal1.get(Calendar.HOUR_OF_DAY))*60 + (cal1.get(Calendar.MINUTE));
            int src = (cal2.get(Calendar.HOUR_OF_DAY))*60 + (cal2.get(Calendar.MINUTE));

            return Integer.toString((dest-src)/60) + " Hr "
                    + Integer.toString((dest-src)%60) + " min";

        } else if (tmr.get(Calendar.YEAR) == cal1.get(Calendar.YEAR) &&
                tmr.get(Calendar.DAY_OF_YEAR) == cal1.get(Calendar.DAY_OF_YEAR)){ // tmr
            int dest = (cal1.get(Calendar.HOUR_OF_DAY))*60 + (cal1.get(Calendar.MINUTE)) +24*60;
            int src = (cal2.get(Calendar.HOUR_OF_DAY))*60 + (cal2.get(Calendar.MINUTE));

            return Integer.toString((dest-src)/60) + " Hr "
                    + Integer.toString((dest-src)%60) + " min";
        } else {
            return Integer.toString(cal1.get(Calendar.DAY_OF_YEAR)-cal2.get(Calendar.DAY_OF_YEAR)) + " Days";
        }
    }

}
