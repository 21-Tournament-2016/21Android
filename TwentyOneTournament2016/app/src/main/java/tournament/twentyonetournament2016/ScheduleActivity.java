package tournament.twentyonetournament2016;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.List;

import Adapters.OnSwipeTouchListener;
import Adapters.ScheduleExpandableListAdapter;
import Utilities.ParseOps;
import Utilities.Round;

public class ScheduleActivity extends ActionBarActivity {

    List<Round> rounds;
    ScheduleExpandableListAdapter listAdapter;
    ExpandableListView listView;
    private static ProgressDialog dialog;
    int currentRound;
    Button btn_prevRound;
    Button btn_nextRound;
    TextView blankspace;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        Bundle extras = getIntent().getExtras();
        rounds = (List<Round>) extras.getSerializable("schedule");
        type = (String) extras.getSerializable("type");
        if (type.equals("schedule")){
            currentRound = ParseOps.getCurrentScheduleRound() - 1;
        }
        else if (type.equals("playoffs")){
            currentRound = ParseOps.getCurrentPlayoffRound() - 1;
        }
        getSupportActionBar().setTitle(String.format("Round %d", currentRound + 1));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.RED));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the listview
        listView = (ExpandableListView) findViewById(R.id.exListView_schedule);
        btn_prevRound = (Button) findViewById(R.id.btn_previousRound);
        btn_nextRound = (Button) findViewById(R.id.btn_nextRound);
        blankspace = (TextView) findViewById(R.id.txt_placeholder);
        blankspace.setBackground(new ColorDrawable(Color.RED));
        btn_nextRound.setBackground(new ColorDrawable(Color.RED));
        btn_nextRound.setTextColor(Color.WHITE);
        btn_prevRound.setBackground(new ColorDrawable(Color.RED));
        btn_prevRound.setTextColor(Color.WHITE);

        btn_prevRound.setVisibility(View.INVISIBLE);
        if (type.equals("playoffs")){
            blankspace.setVisibility(View.INVISIBLE);
        }
    }

    public void pressedTeam1Button(View view)
    {
        RadioButton team2Button = (RadioButton) findViewById(R.id.rdo_team2);
        team2Button.setChecked(false);
    }

    public void pressedTeam2Button(View view)
    {
        RadioButton team1Button = (RadioButton) findViewById(R.id.rdo_team1);
        team1Button.setChecked(false);
    }

    public void saveMatch(final String objectId, final int winner, final int cd){
        dialog = ProgressDialog.show(ScheduleActivity.this, "", "Saving Match...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (type.equals("schedule")) {
                    ParseOps.getInstance().saveMatch(objectId, winner, cd);
                }
                else if (type.equals("playoffs")){
                    ParseOps.getInstance().savePlayoffMatch(objectId, winner, cd);
                    if (currentRound < rounds.size()-1) {
                        Round newRound = ParseOps.getInstance().getPlayoffs().get(currentRound + 1);
                        rounds.set(currentRound + 1, newRound);
                    }
            }
                ScheduleActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.hide();
                        listAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void nextRound(View view)
    {
        int count =  listAdapter.getGroupCount();
        for (int i = 0; i <count ; i++)
            listView.collapseGroup(i);
        btn_prevRound.setVisibility(View.VISIBLE);
        currentRound = currentRound + 1;
        if (currentRound < rounds.size() - 1) {
            listAdapter.setCurrentRound(currentRound);
            listAdapter.notifyDataSetChanged();
        }
        else {
            listAdapter.setCurrentRound(currentRound);
            listAdapter.notifyDataSetChanged();
            btn_nextRound.setVisibility(View.INVISIBLE);
        }
        getSupportActionBar().setTitle(String.format("Round %d", currentRound + 1));
    }

    public void prevRound(View view)
    {
        int count =  listAdapter.getGroupCount();
        for (int i = 0; i <count ; i++)
            listView.collapseGroup(i);
        btn_nextRound.setVisibility(View.VISIBLE);
        currentRound = currentRound - 1;
        if (currentRound == 0) {
            listAdapter.setCurrentRound(currentRound);
            listAdapter.notifyDataSetChanged();
            btn_prevRound.setVisibility(View.INVISIBLE);
        }
        else {
            listAdapter.setCurrentRound(currentRound);
            listAdapter.notifyDataSetChanged();
        }
        getSupportActionBar().setTitle(String.format("Round %d", currentRound + 1));
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

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        int y = btn_nextRound.getHeight();

        listAdapter = new ScheduleExpandableListAdapter(this, rounds, listView, currentRound, height - (x + y), type);

        // setting list adapter
        listView.setAdapter(listAdapter);

        listView.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                prevRound(listView);
            }

            public void onSwipeLeft() {
                nextRound(listView);
            }
        });

    }
}
