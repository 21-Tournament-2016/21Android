package tournament.twentyonetournament2016;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.util.List;

import Utilities.ParseOps;
import Utilities.Round;
import Utilities.Team;
import Utilities.TeamDetails;


public class MainActivity extends ActionBarActivity {

    private static Context appContext;
    private static List<Team> teams;
    private static List<Round> rounds;
    private static ProgressDialog dialog;

    private static TeamDetails teamDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static Context getAppContext(){
        return appContext;
    }

    public void loadSchedule(View view){
        dialog = ProgressDialog.show(MainActivity.this, "", "Retrieving Schedule...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseQuery query = new ParseQuery("Team");
                try {
                    List<ParseObject> list = query.find();
                    rounds = ParseOps.getInstance().getSchedule(list.size()-1);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.hide();
                            Intent intent = new Intent(getAppContext(), ScheduleActivity.class);
                            intent.putExtra("schedule", (Serializable) rounds);
                            startActivity(intent);
                        }
                    });
                } catch (ParseException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void loadStandings(View view){
        dialog = ProgressDialog.show(MainActivity.this, "", "Retrieving Standings...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                teams = ParseOps.getInstance().getStandings();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        Intent intent = new Intent(getAppContext(), StandingsActivity.class);
                        intent.putExtra("standings", (Serializable) teams);
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    public void restart(View view){
        dialog = ProgressDialog.show(MainActivity.this, "", "Generating Schedule.\nThis could take a moment...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                ParseOps.getInstance().restartTournament();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                });
            }
        }).start();
    }
}
