package Utilities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Brandon on 12/6/2015.
 */
public class Round implements Serializable{

    private int roundNumber;
    private ArrayList<Match> matches;

    public Round(int roundNumber, ArrayList<Match> matches) {
        this.roundNumber = roundNumber;
        this.matches = matches;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }
}
