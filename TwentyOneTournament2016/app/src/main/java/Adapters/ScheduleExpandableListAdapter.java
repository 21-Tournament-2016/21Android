package Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import Utilities.Match;
import Utilities.Round;
import tournament.twentyonetournament2016.R;
import tournament.twentyonetournament2016.ScheduleActivity;

/**
 * Created by brandonniedert on 1/30/16.
 */
public class ScheduleExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<Round> rounds;
    private List<Match> matches;
    private int currentRound;
    private int lastExpandedGroupPosition = -1;
    ExpandableListView listView;
    private static ProgressDialog dialog;
    private int height;
    private String type;

    public ScheduleExpandableListAdapter(Context context, List<Round> listDataHeader, ExpandableListView listView, int currentRound, int height, String type){
        this.context = context;
        this.rounds = listDataHeader;
        this.currentRound = currentRound;
        this.matches = rounds.get(currentRound).getMatches();
        this.listView = listView;
        this.height = height / (matches.size()+1)+20;
        this.type = type;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.matches.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.schedule_header_item, null);

        convertView.setMinimumHeight(height);

        Match match = matches.get(groupPosition);
        TextView vs = (TextView) convertView.findViewById(R.id.lbl_vs);
        TextView lblTeam1 = (TextView) convertView.findViewById(R.id.lbl_team1);
        TextView lblTeam2 = (TextView) convertView.findViewById(R.id.lbl_team2);
        TextView lblTeam1Record = (TextView) convertView.findViewById(R.id.lbl_team1Record);
        TextView lblTeam2Record = (TextView) convertView.findViewById(R.id.lbl_team2Record);
        TextView lblwinnerCupDifferential = (TextView) convertView.findViewById(R.id.lbl_winnerCupDifferential);
        TextView team1Arrow = (TextView) convertView.findViewById(R.id.lbl_team1Arrow);
        TextView team2Arrow = (TextView) convertView.findViewById(R.id.lbl_team2Arrow);

        team1Arrow.setTextColor(Color.YELLOW);
        team2Arrow.setTextColor(Color.YELLOW);

        lblTeam1.setText(match.getTeam1());
        lblTeam2.setText(match.getTeam2());
        lblTeam1Record.setText(match.getTeam1Record());
        lblTeam2Record.setText(match.getTeam2Record());

        if (type.equals("playoffs")){
            lblwinnerCupDifferential.setVisibility(View.INVISIBLE);
        }

        if (match.getWinner() != 0) {
            String cups = (match.getCupDifferential() == 1) ? "cup" : "cups";
            lblwinnerCupDifferential.setText(String.format("%d %s", match.getCupDifferential(), cups));
        } else {
            lblwinnerCupDifferential.setVisibility(View.GONE);
        }
        if (match.getWinner() == 1) {
            lblTeam2.setTextColor(Color.GRAY);
            team1Arrow.setVisibility(View.VISIBLE);
        } else if (match.getWinner() == 2) {
            lblTeam1.setTextColor(Color.GRAY);
            team2Arrow.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            p.addRule(RelativeLayout.BELOW, R.id.lbl_team1);
            lblTeam1Record.setLayoutParams(p);
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.schedule_detail_item, null);
        convertView.setBackground(new ColorDrawable(Color.DKGRAY));

        final Match match = matches.get(groupPosition);

        Button btnSaveGame = (Button) convertView.findViewById(R.id.btn_saveGame);
        final RadioButton team1Button = (RadioButton) convertView.findViewById(R.id.rdo_team1);
        final RadioButton team2Button = (RadioButton) convertView.findViewById(R.id.rdo_team2);
        final TextView cups = (TextView) convertView.findViewById(R.id.lbl_cupDifferential);
        SeekBar cupBar = (SeekBar) convertView.findViewById(R.id.seekBar);
        cupBar.setProgress(0);

        btnSaveGame.setBackground(new ColorDrawable(Color.RED));
        btnSaveGame.setTextColor(Color.WHITE);
        cups.setTextColor(Color.WHITE);

        cupBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cups.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSaveGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                match.setCupDifferential(Integer.parseInt((String) cups.getText()));
                if (team1Button.isChecked()) {
                    match.setWinner(1);
                    if (type.equals("schedule")) {
                        match.setTeam1Record(updateStandingsString(match.getTeam1Record(), true));
                        match.setTeam2Record(updateStandingsString(match.getTeam2Record(), false));
                    }
                    ((ScheduleActivity) context).saveMatch(match.getObjectID(), 1, Integer.parseInt((String) cups.getText()));
                } else if (team2Button.isChecked()) {
                    match.setWinner(2);
                    if (type.equals("schedule")) {
                        match.setTeam2Record(updateStandingsString(match.getTeam2Record(), true));
                        match.setTeam1Record(updateStandingsString(match.getTeam1Record(), false));
                    }
                    ((ScheduleActivity) context).saveMatch(match.getObjectID(), 2, Integer.parseInt((String) cups.getText()));
                } else {
                    Toast.makeText(context, "You must select a match winner", Toast.LENGTH_LONG).show();
                }
            }
        });
        team1Button.setChecked(false);
        team2Button.setChecked(false);
        cupBar.setMax(20);

        lastExpandedGroupPosition = groupPosition;

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        Match match = matches.get(groupPosition);
        if (match.getWinner() == 0){
            return 1;
        }
        else{
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.matches.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.matches.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public void onGroupExpanded(int groupPosition){
        if(groupPosition != lastExpandedGroupPosition && lastExpandedGroupPosition != -1){
            listView.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);
        lastExpandedGroupPosition = groupPosition;
    }

    public String updateStandingsString(String oldRecord, boolean winner){
        String newRecord;
        String[] recordSplit = oldRecord.split("-");
        if (winner){
            int wins = Integer.parseInt(recordSplit[0]);
            wins++;
            newRecord = String.format("%d-%s", wins, recordSplit[1]);
        }
        else {
            int losses = Integer.parseInt(recordSplit[1]);
            losses++;
            newRecord = String.format("%s-%d", recordSplit[0], losses);
        }
        return newRecord;
    }

    public void setCurrentRound(int newRound)
    {
        this.currentRound = newRound;
        this.matches = rounds.get(currentRound).getMatches();
    }

}
