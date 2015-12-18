package Utilities;

/**
 * Created by Brandon on 12/6/2015.
 */
public class Match {

    private String objectID;
    private String team1;
    private String team2;
    private String team1ID;
    private String team2ID;
    private int winner;

    public Match(String objectID, String team1, String team2, String team1ID, String team2ID) {
        this.objectID = objectID;
        this.team1 = team1;
        this.team2 = team2;
        this.team1ID = team1ID;
        this.team2ID = team2ID;
        this.winner = 0;
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

}
