package Utilities;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brandon on 12/6/2015.
 */
public class ParseOps {

    private static final ParseOps instance = new ParseOps();
    static int teamCount;

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
            teamCount = teamsInParse.size();
            for (ParseObject parseTeam:teamsInParse){
                Team team = new Team(parseTeam.getString("teamName"), parseTeam.getInt("wins"), parseTeam.getInt("losses"), parseTeam.getInt("CD"), parseTeam.getString("LightColor"), parseTeam.getString("DarkColor"));
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
        ParseQuery<ParseObject> recordQuery = ParseQuery.getQuery("Team");
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
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
                query.whereEqualTo("RoundNumber", currentRound);
                query.addAscendingOrder("matchNumber");
                List<ParseObject> matchesInParse = query.find();
                for (ParseObject parseMatch:matchesInParse) {
                    Match match = new Match(parseMatch.getObjectId(), parseMatch.getString("Team1"), parseMatch.getString("Team2"), parseMatch.getString("Team1ID"), parseMatch.getString("Team2ID"), parseMatch.getInt("Winner"), parseMatch.getInt("CD"), dictionary.get(parseMatch.getString("Team1")), dictionary.get(parseMatch.getString("Team2")));
                    matches.add(match);
                }
                Round round = new Round(currentRound, matches);
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
                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Match");
                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Match");
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Match");
        try {
            ParseObject match = query.get(objectID);
            match.put("Winner", winner);
            match.put("CD", CD);
            match.save();
            sendPush(match);
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
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
            String light = parseTeam.getString("LightColor");
            String dark = parseTeam.getString("DarkColor");
            List<Round> schedule = getSchedule(teamCount-1, team);

            currentTeam = new TeamDetails(team, wins, losses, CD, seasons, player1, player2, player3, schedule, light, dark);
            return currentTeam;
        } catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public void restartTournament() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Team");
        ParseQuery<ParseObject> matchQuery = ParseQuery.getQuery("Match");
        try {
            List<ParseObject> parseTeams = query.find();
            for (ParseObject parseTeam : parseTeams){
                parseTeam.put("wins", 0);
                parseTeam.put("losses", 0);
                parseTeam.put("CD", 0);
                parseTeam.saveInBackground();
            }
            List<ParseObject> parseMatches = matchQuery.find();
            for (ParseObject parseMatch : parseMatches){
                parseMatch.deleteInBackground();
                parseMatch.saveInBackground();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<Team> teams = getStandings();
        for (Team team : teams){
            if (team.getTeamName().equals("Purdue")){
                Collections.swap(teams, 0, teams.indexOf(team));
            }
            else if (team.getTeamName().equals("Salisbury")){
                Collections.swap(teams, 1, teams.indexOf(team));
            }
        }
        List<Team> secondTeams = new ArrayList<Team>(teams);
        int numTeams = teams.size();
        int numDays = (numTeams - 1); // Days needed to complete tournament
        int halfSize = numTeams / 2;

        secondTeams.remove(0);

        int teamsSize = secondTeams.size();

        for (int day = 0; day < numDays; day++)
        {
            ArrayList<Match> matches = new ArrayList<Match>();
            int round = day+1;
            int matchNum = 1;
            int teamIdx = day % teamsSize;
            Match firstMatch = new Match(teams.get(0).getTeamName(), secondTeams.get(teamIdx).getTeamName(), round, matchNum);
            matches.add(firstMatch);
            for (int idx = 1; idx < halfSize; idx++)
            {
                matchNum++;
                int firstTeam = (day + idx) % teamsSize;
                int secondTeam = (day  + teamsSize - idx) % teamsSize;
                Match otherMatch = new Match(secondTeams.get(firstTeam).getTeamName(), secondTeams.get(secondTeam).getTeamName(), round, matchNum);
                matches.add(otherMatch);
            }
            if (round != 1){
                Collections.shuffle(matches);
            }
            int x = 1;
            for (Match match : matches){
                ParseObject parseMatch = new ParseObject("Match");
                parseMatch.put("Team1", match.getTeam1());
                parseMatch.put("Team2", match.getTeam2());
                parseMatch.put("RoundNumber", round);
                parseMatch.put("matchNumber", x);
                parseMatch.saveInBackground();
                x++;
            }
        }
    }

    public void sendPush(ParseObject match){
        String winner;
        String loser;
        if (match.getInt("Winner") == 1){
            winner = match.getString("Team1");
            loser = match.getString("Team2");
        }
        else{
            loser = match.getString("Team1");
            winner = match.getString("Team2");
        }
        ParsePush push = new ParsePush();
        push.setMessage(String.format("%s has beaten %s by %d cups", winner, loser,match.getInt("CD")));
        push.sendInBackground();
    }



    public String getTweets(){
        String tweetString = "                 ";
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Twitter");
            query.orderByDescending("createdAt");
            List<ParseObject> objects = query.find();
            for (ParseObject tweet : objects){
                String currentTweet = tweet.getString("tweet");
                tweetString = tweetString + "     ・     " + currentTweet;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return tweetString;
    }

    public void sendTweet(String tweet){
        ParseObject object = new ParseObject("Twitter");
        object.put("tweet", tweet);
        object.saveInBackground();
    }

    public List<Round> getPlayoffs(){
        List<Team> teams = getStandings();

        int[] twos = {2,4,8,16,32,64};
        int teamCount = teams.size();
        int roundCount = 0;


        for (int i = 0; i < twos.length; i++){
            if (teamCount <= twos[i]){
                roundCount = i+1;
                break;
            }
        }

        List<Round> schedule = new ArrayList<Round>();
        int currentRound = 1;
        try{
            while (currentRound <= roundCount) {
                ArrayList<Match> matches = new ArrayList<Match>();
                ParseQuery<ParseObject> query = ParseQuery.getQuery("PlayoffMatch");
                query.whereEqualTo("RoundNumber", currentRound);
                query.addAscendingOrder("MatchNumber");
                List<ParseObject> matchesInParse = query.find();
                for (ParseObject parseMatch:matchesInParse) {
                    Match match = new Match(parseMatch.getString("Team1"), parseMatch.getString("Team2"), parseMatch.getInt("RoundNumber"), parseMatch.getInt("MatchNumber"));
                    matches.add(match);
                }
                Round round = new Round(currentRound, matches);
                schedule.add(round);
                currentRound++;
            }
        } catch (ParseException e){
            e.printStackTrace();
        }
        return schedule;
    }

    public void startPlayoffs(){
        List<Team> teams = getStandings();

        int[] twos = {2,4,8,16,32,64};
        int teamCount = teams.size();
        int roundCount = 0;
        int numByes = 0;
        int firstRealRoundCount = 0;

        for (int i = 0; i < twos.length; i++){
            if (teamCount <= twos[i]){
                roundCount = i+1;
                numByes = twos[i]-teamCount;
                firstRealRoundCount = twos[i-1]/2;
                break;
            }
        }
        List<Team> firstRoundTeams;
        List<Team> byeTeams;
        if (numByes != 0){
            byeTeams = teams.subList(0,numByes);
            firstRoundTeams = teams.subList(numByes, teams.size());
        }
        else {
            firstRoundTeams = teams;
            byeTeams = new ArrayList<>();
        }


        int currentRound = 1;
        int numMatchesInNextRound = firstRoundTeams.size()/2;
        while (currentRound <= roundCount){
            int currentMatch = 0;
            if (numByes != 0 && currentRound == 2){
                while (currentMatch < numMatchesInNextRound){
                    ParseObject parseMatch = new ParseObject("PlayoffMatch");
                    if (currentMatch < byeTeams.size()){
                        parseMatch.put("Team1", byeTeams.get(currentMatch).getTeamName());
                    }
                    parseMatch.put("RoundNumber", currentRound);
                    parseMatch.put("MatchNumber", currentMatch+1);
                    parseMatch.saveInBackground();
                    currentMatch++;
                }
                numMatchesInNextRound = numMatchesInNextRound/2;
            }
            else {
                while (currentMatch < numMatchesInNextRound){
                    ParseObject parseMatch = new ParseObject("PlayoffMatch");
                    if (currentRound == 1) {
                        parseMatch.put("Team1", firstRoundTeams.get(currentMatch).getTeamName());
                        parseMatch.put("Team2", firstRoundTeams.get(firstRoundTeams.size() - (currentMatch + 1)).getTeamName());
                    }
                    parseMatch.put("RoundNumber", currentRound);
                    parseMatch.put("MatchNumber", currentMatch+1);
                    parseMatch.saveInBackground();
                    currentMatch++;
                }
                if(currentRound == 1 && numByes != 0){
                    numMatchesInNextRound = firstRealRoundCount;
                }
                else {
                    numMatchesInNextRound = numMatchesInNextRound/2;
                }
            }

            currentRound++;
        }

    }

    public void savePlayoffMatch(String objectID, int winner, int CD){
        /*
        ParseQuery<ParseObject> query = ParseQuery.getQuery("PlayoffMatch");
        try {
            ParseObject match = query.get(objectID);
            match.put("Winner", winner);
            match.put("CD", CD);
            match.save();
            sendPush(match);
            startStandingsUpdate(match);
        } catch (ParseException e){
            e.printStackTrace();
        }*/
    }


}
