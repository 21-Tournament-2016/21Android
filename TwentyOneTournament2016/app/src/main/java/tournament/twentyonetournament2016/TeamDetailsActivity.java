package tournament.twentyonetournament2016;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Display;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import Adapters.TeamScheduleAdapter;
import Utilities.Match;
import Utilities.Round;
import Utilities.TeamDetails;

public class TeamDetailsActivity extends ActionBarActivity {

    TeamDetails team;
    TeamScheduleAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        team = (TeamDetails) extras.getSerializable("team");
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#" + team.getLight() + "\">" + team.getName() + "</font>")));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + team.getDark())));
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#" + team.getLight()));

    }

    private String getSeasonString(int season){
        int num = season % 10;
        switch (num){
            case 1:
                return "1st Season";
            case 2:
                return "2nd Season";
            case 3:
                return "3rd Season";
            default:
                return String.format("%dth Season", season);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        TextView seasons = (TextView) findViewById(R.id.txt_numTournament);
        TextView player1 = (TextView) findViewById(R.id.txt_player1);
        TextView player2 = (TextView) findViewById(R.id.txt_player2);
        TextView player3 = (TextView) findViewById(R.id.txt_player3);

        int x = getSupportActionBar().getHeight();
        int x2 = seasons.getHeight();
        int x3 = player1.getHeight();
        int x4 = player3.getHeight();
        int x5 = player2.getHeight();
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int big = size.y;

        List<Match> matches = new ArrayList<Match>();
        for (Round round:team.getSchedule())
        {
            matches.add(round.getMatches().get(0));
        }

        seasons.setTextColor(Color.parseColor("#" + team.getDark()));
        player1.setTextColor(Color.parseColor("#" + team.getDark()));
        player2.setTextColor(Color.parseColor("#" + team.getDark()));
        player3.setTextColor(Color.parseColor("#" + team.getDark()));

        ImageView img = (ImageView) findViewById(R.id.teamPicture);

        String imageName = team.getName().split("\\s+")[0].toLowerCase();
        int resID = getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        seasons.setText(getSeasonString(team.getSeasons()));
        player1.setText(team.getPlayer1());
        player2.setText(team.getPlayer2());
        player3.setText(team.getPlayer3());
        img.setImageResource(resID);

        int height =  big - (x + x2 + x3 + x4 + x5);

        listView = (ListView) findViewById(R.id.lv_teamSchedule);
        adapter = new TeamScheduleAdapter(this, matches, team.getName(), team.getLight(), team.getDark(), height);
        listView.setAdapter(adapter);

    }

}
