package tournament.twentyonetournament2016;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import java.util.List;

import Utilities.Match;
import Utilities.ParseOps;
import Utilities.Round;
import Utilities.Team;


public class MainActivity extends ActionBarActivity {

    private static Context appContext;
    private static List<Team> teams;
    private static ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        dialog = ProgressDialog.show(MainActivity.this, "", "Loading...", true);
        new Thread(new Runnable() {
            public void run() {
                teams = ParseOps.getInstance().getStandings();
                int rank = 1;
                for (Team team:teams){
                    System.out.printf("Rank: %d\t%s\t%d\t%d\t%d\n", rank, team.getTeamName(), team.getWins(), team.getLosses(), team.getCD());
                    rank++;
                }
                System.out.println("-------------------------------------");
                Round round = ParseOps.getInstance().getSchedule(8).get(4);
                for (Match match:round.getMatches()) {
                    System.out.printf("%s vs %s\n", match.getTeam1(), match.getTeam2());
                }
                System.out.println("-------------------------------------");
                String thing = round.getMatches().get(0).getObjectID();
                System.out.println(thing);
                ParseOps.getInstance().saveMatch(round.getMatches().get(0).getObjectID(), 1, 14);
                System.out.println("-------------------------------------");
                teams = ParseOps.getInstance().getStandings();
                rank = 1;
                for (Team team:teams){
                    System.out.printf("Rank: %d\t%s\t%d\t%d\t%d\n", rank, team.getTeamName(), team.getWins(), team.getLosses(), team.getCD());
                    rank++;
                }
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        dialog.hide();
                    }
                });
            }
        }).start();
    }

    public static Context getAppContext(){
        return appContext;
    }
}
