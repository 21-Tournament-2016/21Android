package Utilities;

import java.io.Serializable;

/**
 * Created by Brandon on 12/6/2015.
 */
public class Team implements Serializable{

    private String teamName;
    private int wins;
    private int losses;
    private int CD;
    private String dark;
    private String light;

    public Team(String teamName, int wins, int losses, int CD, String light, String dark){
        this.teamName = teamName;
        this.wins = wins;
        this.losses = losses;
        this.CD = CD;
        this.dark = dark;
        this.light = light;
    }

    public String getDark() {
        return dark;
    }

    public String getLight() {
        return light;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getCD() {
        return CD;
    }

    public void setCD(int CD) {
        this.CD = CD;
    }
}
