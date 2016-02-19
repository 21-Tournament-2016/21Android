package Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brandonniedert on 2/9/16.
 */
public class TeamDetails implements Serializable{

    private String name;
    private int wins;
    private int losses;
    private int cd;
    private int seasons;
    private String player1;
    private String player2;
    private String player3;
    private List<Round> schedule;
    private String light;
    private String dark;

    public TeamDetails(String name, int wins, int losses, int cd, int seasons, String player1, String player2, String player3, List<Round> schedule, String light, String dark) {
        this.name = name;
        this.wins = wins;
        this.losses = losses;
        this.cd = cd;
        this.seasons = seasons;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.schedule = schedule;
        this.light = light;
        this.dark = dark;
    }

    public String getLight() {
        return light;
    }

    public String getDark() {
        return dark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCd() {
        return cd;
    }

    public void setCd(int cd) {
        this.cd = cd;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public List<Round> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<Round> schedule) {
        this.schedule = schedule;
    }

    public int getSeasons() {
        return seasons;
    }

    public void setSeasons(int seasons) {
        this.seasons = seasons;
    }
}
