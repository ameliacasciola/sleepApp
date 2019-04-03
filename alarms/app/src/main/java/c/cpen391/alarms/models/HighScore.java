package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class HighScore {
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("name")
    private String username;
    @SerializedName("total_points")
    private Integer score;

    public int getUser() {
        return this.user_id;
    }

    public String getUsername() {return this.username;}

    public Integer getScore() {return this.score;}

}
