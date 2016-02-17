package tournament.twentyonetournament2016;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        team = (TeamDetails) extras.getSerializable("team");
        getSupportActionBar().setTitle(team.getName());

        List<Match> matches = new ArrayList<Match>();
        for (Round round:team.getSchedule())
        {
            matches.add(round.getMatches().get(0));
        }

        listView = (ListView) findViewById(R.id.lv_teamSchedule);
        adapter = new TeamScheduleAdapter(this, matches, team.getName());
        listView.setAdapter(adapter);

        TextView name = (TextView) findViewById(R.id.txt_teamName);
        TextView seasons = (TextView) findViewById(R.id.txt_numTournament);
        TextView player1 = (TextView) findViewById(R.id.txt_player1);
        TextView player2 = (TextView) findViewById(R.id.txt_player2);
        TextView player3 = (TextView) findViewById(R.id.txt_player3);
        ImageView img = (ImageView) findViewById(R.id.imageView);

        name.setText(team.getName());
        seasons.setText(getSeasonString(team.getSeasons()));
        player1.setText(team.getPlayer1());
        player2.setText(team.getPlayer2());
        player3.setText(team.getPlayer3());
        //img.setImageResource(R.drawable.i);

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

}
