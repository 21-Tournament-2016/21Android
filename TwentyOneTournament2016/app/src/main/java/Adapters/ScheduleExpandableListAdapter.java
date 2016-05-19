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
import android.widget.ImageView;
import android.widget.RadioButton;
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
    private String type;

    public ScheduleExpandableListAdapter(Context context, List<Round> listDataHeader, ExpandableListView listView, int currentRound, int height, String type){
        this.context = context;
        this.rounds = listDataHeader;
        this.currentRound = currentRound;
        this.matches = rounds.get(currentRound).getMatches();
        this.listView = listView;
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
        convertView = infalInflater.inflate(R.layout.schedule_item, null);

        Match match = matches.get(groupPosition);
        TextView lblTeam1 = (TextView) convertView.findViewById(R.id.currentTeamName);
        TextView lblTeam2 = (TextView) convertView.findViewById(R.id.currentOpponentName);
        TextView lblTeam1Record = (TextView) convertView.findViewById(R.id.t1record);
        TextView lblTeam2Record = (TextView) convertView.findViewById(R.id.t2record);
        TextView lblwinnerCupDifferential = (TextView) convertView.findViewById(R.id.cupDifferential);
        TextView divider = (TextView) convertView.findViewById(R.id.dividerLine);
        ImageView team1Image = (ImageView) convertView.findViewById(R.id.currentTeamLogo);
        ImageView team2Image = (ImageView) convertView.findViewById(R.id.currentOpponentLogo);
        TextView team1Arrow = (TextView) convertView.findViewById(R.id.lbl_team1Arrow);
        TextView team2Arrow = (TextView) convertView.findViewById(R.id.lbl_team2Arrow);
        TextView vConnect = (TextView) convertView.findViewById(R.id.playoffConnector);
        View t1V = (View) convertView.findViewById(R.id.t1Line);
        View t2V = (View) convertView.findViewById(R.id.t2Line);
        View connect = (View) convertView.findViewById(R.id.hconnect);
        TextView championsText = (TextView) convertView.findViewById(R.id.txt_championTeam);
        TextView championsLabel = (TextView) convertView.findViewById(R.id.txt_championslbl);
        ImageView champsImage = (ImageView) convertView.findViewById(R.id.championImage);

        String team1 = match.getTeam1().split("\\s+")[0].toLowerCase();
        int resID1 = context.getResources().getIdentifier(team1, "drawable", context.getPackageName());
        String team2 = match.getTeam2().split("\\s+")[0].toLowerCase();
        int resID2 = context.getResources().getIdentifier(team2, "drawable", context.getPackageName());

        lblTeam1.setText(match.getTeam1());
        lblTeam2.setText(match.getTeam2());

        if (type.equals("schedule")) {
            lblTeam1Record.setText(match.getTeam1Record());
            lblTeam2Record.setText(match.getTeam2Record());
        }
        else if (type.equals("playoffs")){
            if (match.getSeed1() != 0) {
                lblTeam1Record.setText(Integer.toString(match.getSeed1()));
            }
            else{
                lblTeam1Record.setText("");
            }
            if (match.getSeed2() != 0) {
                lblTeam2Record.setText(Integer.toString(match.getSeed2()));
            }
            else {
                lblTeam2Record.setText("");
            }
            int paddingPixel = 65;
            float density = context.getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density);
            lblTeam1Record.setPadding(0,0,paddingDp,0);
            lblTeam2Record.setPadding(0,0,paddingDp,0);
        }

        team1Image.setImageResource(resID1);
        team2Image.setImageResource(resID2);

        if (type.equals("playoffs")){
            lblwinnerCupDifferential.setVisibility(View.INVISIBLE);
            divider.setVisibility(View.INVISIBLE);
            vConnect.setVisibility(View.VISIBLE);
            t1V.setVisibility(View.VISIBLE);
            t2V.setVisibility(View.VISIBLE);
            connect.setVisibility(View.VISIBLE);
            if (match.getWinner() == 1 && currentRound == 3){
                champsImage.setImageResource(resID1);
                championsText.setText(match.getTeam1());
                champsImage.setVisibility(View.VISIBLE);
                championsLabel.setVisibility(View.VISIBLE);
                championsText.setVisibility(View.VISIBLE);
            }
            else if (match.getWinner() == 2  && currentRound == 3){
                champsImage.setImageResource(resID2);
                championsText.setText(match.getTeam2());
                champsImage.setVisibility(View.VISIBLE);
                championsLabel.setVisibility(View.VISIBLE);
                championsText.setVisibility(View.VISIBLE);
            }
        }

        if (match.getWinner() != 0) {
            String cups = (match.getCupDifferential() == 1) ? "\ncup" : "\ncups";
            lblwinnerCupDifferential.setText(String.format("%d %s", match.getCupDifferential(), cups));
            int paddingPixel = 65;
            float density = context.getResources().getDisplayMetrics().density;
            int paddingDp = (int)(paddingPixel * density);
            lblTeam1Record.setPadding(0,0,paddingDp,0);
            lblTeam2Record.setPadding(0,0,paddingDp,0);
        } else {
            lblwinnerCupDifferential.setVisibility(View.INVISIBLE);
            divider.setVisibility(View.INVISIBLE);
        }
        if (match.getWinner() == 1) {
            lblTeam2.setTextColor((Color.parseColor("#B4B4B4")));
            lblTeam2Record.setTextColor((Color.parseColor("#B4B4B4")));
            if (type.equals("schedule")) {
                team1Arrow.setVisibility(View.VISIBLE);
            }
        } else if (match.getWinner() == 2) {
            lblTeam1.setTextColor(Color.parseColor("#B4B4B4"));
            lblTeam1Record.setTextColor((Color.parseColor("#B4B4B4")));
            if (type.equals("schedule")) {
                team2Arrow.setVisibility(View.VISIBLE);
            }
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
        TextView team1 = (TextView) convertView.findViewById(R.id.txtteam1);
        TextView team2 = (TextView) convertView.findViewById(R.id.txtteam2);
        SeekBar cupBar = (SeekBar) convertView.findViewById(R.id.seekBar);
        cupBar.setProgress(0);

        btnSaveGame.setBackground(new ColorDrawable(Color.RED));
        btnSaveGame.setTextColor(Color.WHITE);
        cups.setTextColor(Color.WHITE);
        team1.setTextColor(Color.WHITE);
        team2.setTextColor(Color.WHITE);
        team1.setText(match.getTeam1());
        team2.setText(match.getTeam2());

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
                    ((ScheduleActivity) context).saveMatch(match.getObjectID(), 1, Integer.parseInt((String) cups.getText()), match.getSeed1(), match.getSeed2());
                } else if (team2Button.isChecked()) {
                    match.setWinner(2);
                    if (type.equals("schedule")) {
                        match.setTeam2Record(updateStandingsString(match.getTeam2Record(), true));
                        match.setTeam1Record(updateStandingsString(match.getTeam1Record(), false));
                    }
                    ((ScheduleActivity) context).saveMatch(match.getObjectID(), 2, Integer.parseInt((String) cups.getText()), match.getSeed1(), match.getSeed2());
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
//        return 0;
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
