package c.cpen391.alarms.models;

import java.io.Serializable;

public class Game implements Serializable {
    private String gameName;
    private String gameDescription;

    public Game(String gameName, String gameDescription){
        this.gameName = gameName;
        this.gameDescription = gameDescription;
    }

    public String getGameName(){
        return this.gameName;
    }

    public String getGameDescription(){
        return this.gameDescription;
    }

    public void setGameName(String gameName){
        this.gameName = gameName;
    }

    public void setGameDescription(String gameDescription){
        this.gameDescription = gameDescription;
    }

}
