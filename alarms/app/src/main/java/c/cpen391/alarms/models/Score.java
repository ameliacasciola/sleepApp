package c.cpen391.alarms.models;

import com.google.gson.annotations.SerializedName;

public class Score {
    @SerializedName("user_id")
    private int user_id;
    @SerializedName("game")
    private String game;
    @SerializedName("score")
    private int score;

    public int getUser() {
        return this.user_id;
    }

    public void setUser(int user_id) {this.user_id = user_id;}

    public String getGame() {return this.game;}

    public void setGame(String game) {this.game = game;}

    public int getScore() {return this.score;}

    public void setScore(int score) {this.score = score;}

    @Override
    public String toString() {
        return "Score{" +
                "user=" + Integer.toString(user_id) +
                ", game='" + game + '\'' +
                ", score='" + Integer.toString(score) +
                '}';
    }
}
