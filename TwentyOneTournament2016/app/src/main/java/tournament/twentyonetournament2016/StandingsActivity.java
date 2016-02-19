package tournament.twentyonetournament2016;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import Adapters.StandingsListViewAdapter;
import Utilities.ParseOps;
import Utilities.Team;
import Utilities.TeamDetails;

public class StandingsActivity extends ActionBarActivity {

    List<Team> teams;
    ListView listView;
    StandingsListViewAdapter adapter;
    private static ProgressDialog dialog;
    TextView rankLabel;
    TextView teamLabel;
    TextView recordLabel;
    TextView CDLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        getSupportActionBar().setTitle("Current Standings");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);

        rankLabel = (TextView) findViewById(R.id.txt_rankTitle);
        teamLabel = (TextView) findViewById(R.id.txt_teamTitle);
        recordLabel = (TextView) findViewById(R.id.txt_RecordLabel);
        CDLabel = (TextView) findViewById(R.id.txt_CDtitle);

        rankLabel.setTextColor(Color.WHITE);
        teamLabel.setTextColor(Color.WHITE);
        recordLabel.setTextColor(Color.WHITE);
        CDLabel.setTextColor(Color.WHITE);

        Bundle extras = getIntent().getExtras();
        teams = (List<Team>) extras.getSerializable("standings");
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

        int x = getSupportActionBar().getHeight();
        int y = rankLabel.getHeight();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;

        Log.i("HEIGHT", String.format("%d --- %d --- %d", x, y, height));

        listView = (ListView) findViewById(R.id.lst_standingsView);
        adapter = new StandingsListViewAdapter(this, teams, height - (x+y));
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
                dialog = ProgressDialog.show(StandingsActivity.this, "", "Retrieving Team Info...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final TeamDetails team = ParseOps.getInstance().getTeamInfo(teams.get(position).getTeamName());
                        StandingsActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.hide();
                                Intent intent = new Intent(StandingsActivity.this, TeamDetailsActivity.class);
                                intent.putExtra("team", team);
                                startActivity(intent);
                            }
                        });
                    }
                }).start();
            }
        });
    }

}
