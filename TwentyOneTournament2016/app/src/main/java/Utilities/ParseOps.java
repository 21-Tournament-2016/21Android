package Utilities;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import tournament.twentyonetournament2016.MainActivity;

/**
 * Created by Brandon on 12/6/2015.
 */
public class ParseOps {

    private static final ParseOps instance = new ParseOps();

    protected ParseOps(){
        Parse.initialize(MainActivity.getAppContext(), "uqmvXiqFfCkv2wwVMm1BGFrVuGqTlPjxbivHSM4N", "Q0hNnGIe0M643J8cQf6AfAVgsvRhMUh0mSa36nTI");
        Log.d("ParseOps", "Parse initialized");
    }

    public static ParseOps getInstance(){
        return instance;
    }

    public List<Team> getStandings(){
        List<Team> teams = new ArrayList<Team>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeamOld");
        query.addDescendingOrder("wins");
        query.addDescendingOrder("CD");
        try {
            List<ParseObject> teamsInParse = query.find();
            for (ParseObject parseTeam:teamsInParse){
                Team team = new Team(parseTeam.getString("teamName"), parseTeam.getInt("wins"), parseTeam.getInt("losses"), parseTeam.getInt("CD"));
                teams.add(team);
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return teams;
    }

    public List<Round> getSchedule(int numberOfRounds){
        List<Round> schedule = new ArrayList<Round>();
        int currentRound = 1;
        while (currentRound <= numberOfRounds) {
            ArrayList<Match> matches = new ArrayList<Match>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("MatchOld");
            query.whereEqualTo("RoundNumber", currentRound);
            query.addAscendingOrder("matchNumber");
            try {
                List<ParseObject> matchesInParse = query.find();
                for (ParseObject parseMatch:matchesInParse) {
                    Match match = new Match(parseMatch.getObjectId(), parseMatch.getString("Team1"), parseMatch.getString("Team2"), parseMatch.getString("Team1ID"), parseMatch.getString("Team2ID"));
                    matches.add(match);
                }
            } catch (ParseException e){
                e.printStackTrace();
            }
            Round round = new Round(numberOfRounds, matches);
            schedule.add(round);
            currentRound++;
        }
        return schedule;
    }

    public void saveMatch(String objectID, int winner, int CD){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MatchOld");
        try {
            ParseObject match = query.get(objectID);
            match.put("Winner", winner);
            match.put("CD", CD);
            match.save();
            startStandingsUpdate(match);
        } catch (ParseException e){
            e.printStackTrace();
        }
    }

    public void startStandingsUpdate(ParseObject match){
        String team1ID = match.getString("Team1ID");
        String team2ID = match.getString("Team2ID");
        int CD = match.getInt("CD");
        if(match.getInt("Winner") == 1) {
            updateStandings(team1ID, team2ID, CD);
        }
        else {
            updateStandings(team2ID, team1ID, CD);
        }
    }

    public void updateStandings(String winner, String loser, int CD){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeamOld");
        try {
            ParseObject team = query.get(winner);
            int wins = team.getInt("wins");
            int cdWinner = team.getInt("CD");
            wins++;
            cdWinner = cdWinner + CD;
            team.put("wins", wins);
            team.put("CD", cdWinner);
            team.save();

            team = query.get(loser);
            int losses = team.getInt("losses");
            int cdLoser = team.getInt("CD");
            losses++;
            cdLoser = cdLoser - CD;
            team.put("losses", losses);
            team.put("CD", cdLoser);
            team.save();

        } catch (ParseException e){
            e.printStackTrace();
        }
    }


}
