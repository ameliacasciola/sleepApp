package c.cpen391.alarms.api;

import com.google.gson.annotations.SerializedName;

public class AlarmPost {

    @SerializedName("description")
    private String alarmDescription;
    @SerializedName("alarm_time")
    private String alarmTime;
    @SerializedName("volume")
    private Integer volume;
    @SerializedName("username")
    private Integer userid;
    @SerializedName("game_name")
    private String gameName;
    @SerializedName("active")
    private boolean active;

    public AlarmPost(String description, String alarm_time, Integer volume, String game_name, boolean active, Integer userid) {
        this.alarmDescription = description;
        this.alarmTime = alarm_time;
        this.volume = volume;
        this.userid = userid;
        this.gameName = game_name;
        this.active = active;
    }

    @Override
    public String toString() {
        return "Post{" +
                "description='" + alarmDescription + '\'' +
                ", alarm_time='" + alarmTime + '\'' +
                ", volume='" + volume + '\'' +
                ", active='" + active + '\'' +
                ", username='" + userid + '\'' +
                ", game_name='" + gameName + '\'' +
                '}';
    }
}