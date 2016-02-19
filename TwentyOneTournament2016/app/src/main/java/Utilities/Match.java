package Utilities;

import java.io.Serializable;

/**
 * Created by Brandon on 12/6/2015.
 */
public class Match implements Serializable{

    private String objectID;
    private String team1;
    private String team2;
    private String team1ID;
    private String team2ID;
    private int winner;
    private int cupDifferential;
    private String team1Record;
    private String team2Record;
    private int round;
    private int matchNumber;

    public Match(String objectID, String team1, String team2, String team1ID, String team2ID, int winner, int cupDifferential, String team1Record, String team2Record) {
        this.objectID = objectID;
        this.team1 = team1;
        this.team2 = team2;
        this.team1ID = team1ID;
        this.team2ID = team2ID;
        this.winner = winner;
        this.cupDifferential = cupDifferential;
        this.team1Record = team1Record;
        this.team2Record = team2Record;
    }

    public Match (String team1, String team2, int round, int matchNumber){
        this.team1 = team1;
        this.team2 = team2;
        this.round = round;
        this.matchNumber = matchNumber;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTeam1ID() {
        return team1ID;
    }

    public void setTeam1ID(String team1ID) {
        this.team1ID = team1ID;
    }

    public String getTeam2ID() {
        return team2ID;
    }

    public void setTeam2ID(String team2ID) {
        this.team2ID = team2ID;
    }

    public int getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getCupDifferential() {
        return cupDifferential;
    }

    public void setCupDifferential(int cupDifferential) {
        this.cupDifferential = cupDifferential;
    }

    public String getTeam1Record() {
        return team1Record;
    }

    public void setTeam1Record(String team1Record) {
        this.team1Record = team1Record;
    }

    public String getTeam2Record() {
        return team2Record;
    }

    public void setTeam2Record(String team2Record) {
        this.team2Record = team2Record;
    }

    public int getMatchNumber() {
        return matchNumber;
    }
}
