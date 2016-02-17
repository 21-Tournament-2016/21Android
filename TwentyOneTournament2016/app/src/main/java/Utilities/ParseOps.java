package Utilities;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brandon on 12/6/2015.
 */
public class ParseOps {

    private static final ParseOps instance = new ParseOps();

    protected ParseOps(){
//        Parse.initialize(MainActivity.getAppContext(), "uqmvXiqFfCkv2wwVMm1BGFrVuGqTlPjxbivHSM4N", "Q0hNnGIe0M643J8cQf6AfAVgsvRhMUh0mSa36nTI");
//        Log.d("ParseOps", "Parse initialized");
    }

    public static ParseOps getInstance(){
        return instance;
    }

    public List<Team> getStandings(){
        List<Team> teams = new ArrayList<Team>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
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
        Map<String, String> dictionary = new HashMap<String, String>();
        ParseQuery<ParseObject> recordQuery = ParseQuery.getQuery("TeamOld");
        try{
            List<ParseObject> teams = recordQuery.find();
            for (ParseObject team : teams){
                int wins = team.getInt("wins");
                int losses = team.getInt("losses");
                String record = String.format("%d-%d", wins, losses);
                dictionary.put(team.getString("teamName"), record);
            }
            while (currentRound <= numberOfRounds) {
                ArrayList<Match> matches = new ArrayList<Match>();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("MatchOld");
                query.whereEqualTo("RoundNumber", currentRound);
                query.addAscendingOrder("matchNumber");
                List<ParseObject> matchesInParse = query.find();
                for (ParseObject parseMatch:matchesInParse) {
                    Match match = new Match(parseMatch.getObjectId(), parseMatch.getString("Team1"), parseMatch.getString("Team2"), parseMatch.getString("Team1ID"), parseMatch.getString("Team2ID"), parseMatch.getInt("Winner"), parseMatch.getInt("CD"), dictionary.get(parseMatch.getString("Team1")), dictionary.get(parseMatch.getString("Team2")));
                    matches.add(match);
                }
                Round round = new Round(numberOfRounds, matches);
                schedule.add(round);
                currentRound++;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return schedule;
    }

    public List<Round> getSchedule(int numberOfRounds, String team){
        List<Round> schedule = new ArrayList<Round>();
        int currentRound = 1;
        try{
            while (currentRound <= numberOfRounds) {
                ArrayList<Match> matches = new ArrayList<Match>();
                List<ParseQuery<ParseObject>> queryList = new ArrayList<ParseQuery<ParseObject>>();
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("MatchOld");
                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("MatchOld");
                query1.whereEqualTo("RoundNumber", currentRound);
                query1.whereEqualTo("Team1", team);
                query2.whereEqualTo("RoundNumber", currentRound);
                query2.whereEqualTo("Team2", team);
                queryList.add(query1);
                queryList.add(query2);
                ParseQuery<ParseObject> query = ParseQuery.or(queryList);
                query.addAscendingOrder("RoundNumber");
                List<ParseObject> matchesInParse = query.find();
                for (ParseObject parseMatch:matchesInParse) {
                    Match match = new Match(parseMatch.getObjectId(), parseMatch.getString("Team1"), parseMatch.getString("Team2"), parseMatch.getString("Team1ID"), parseMatch.getString("Team2ID"), parseMatch.getInt("Winner"), parseMatch.getInt("CD"), null, null);
                    matches.add(match);
                }
                Round round = new Round(numberOfRounds, matches);
                schedule.add(round);
                currentRound++;
            }
        } catch (ParseException e){
            e.printStackTrace();
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
        String team1 = match.getString("Team1");
        String team2 = match.getString("Team2");
        int CD = match.getInt("CD");
        if(match.getInt("Winner") == 1) {
            updateStandings(team1, team2, CD);
        }
        else {
            updateStandings(team2, team1, CD);
        }
    }

    public void updateStandings(String winner, String loser, int CD){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeamOld");
        try {
            query.whereEqualTo("teamName", winner);
            ParseObject team = query.find().get(0);
            int wins = team.getInt("wins");
            int cdWinner = team.getInt("CD");
            wins++;
            cdWinner = cdWinner + CD;
            team.put("wins", wins);
            team.put("CD", cdWinner);
            team.save();

            query.whereEqualTo("teamName", loser);
            team = query.find().get(0);
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

    public TeamDetails getTeamInfo(String team){
        TeamDetails currentTeam;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TeamOld");
        try {
            query.whereEqualTo("teamName", team);
            ParseObject parseTeam = query.find().get(0);
            int wins = parseTeam.getInt("wins");
            int losses = parseTeam.getInt("losses");
            int CD = parseTeam.getInt("CD");
            int seasons = parseTeam.getInt("seasons");
            String player1 = parseTeam.getString("player1");
            String player2 = parseTeam.getString("player2");
            String player3 = parseTeam.getString("player3");
            List<Round> schedule = getSchedule(10, team);

            currentTeam = new TeamDetails(team, wins, losses, CD, seasons, player1, player2, player3, schedule);
            return currentTeam;
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public void restartTournament(){
//        List<Team> teams = getStandings();
//        int subsetSize = teams.size()/2;
//        List<Team> team1 = teams.subList(0,subsetSize);
//        List<Team> team2 = teams.subList(subsetSize, teams.size());
//        Integer[] twos = {2,4,8,16,32};
//      //Used for finding how many playoff rounds are needed
//        int x;
//        for (int i : twos){
//            if (subsetSize > i){
//                continue;
//            }
//            else{
//                x = Arrays.asList(twos).indexOf(i);
//                Log.i("test", Integer.toString(x +1));
//                break;
//            }
//        }
//        int halvedSize = team1.size();
//        for (int i = 0; i < 11; i++) {
//            for (int j = 0; j < halvedSize; j++) {
//                Log.i("Match", String.format("%s vs %s", team1.get(j).getTeamName(), team2.get(j).getTeamName()));
//            }
//            Team removed1 = team1.remove(team1.size() - 1);
//            Team removed2 = team2.get(0);
//            team1.add(1, removed2);
//            team2.add(removed1);
//        }
    }


}
