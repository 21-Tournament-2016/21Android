package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import Utilities.Team;
import tournament.twentyonetournament2016.R;

/**
 * Created by brandonniedert on 2/3/16.
 */
public class StandingsListViewAdapter extends BaseAdapter {

    int teamCount;
    List<Team> teams;
    Context context;
    int height;
    public StandingsListViewAdapter(Context context, List<Team> teams, int height) {
        this.teams = teams;
        this.context = context;
        teamCount = teams.size();
        this.height = (height/(teamCount + 1)) - 4;
    }

    @Override
    public int getCount() {
        return teamCount;
    }

    @Override
    public Object getItem(int i) {
        return this.teams.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.standings_item, null);
        if (i % 2 == 0){
            view.setBackground(new ColorDrawable(Color.GRAY));
        }
        else {
            view.setBackground(new ColorDrawable(Color.DKGRAY));
        }

        view.setMinimumHeight(height);

        Team team = teams.get(i);

        TextView rank = (TextView) view.findViewById(R.id.txt_rank);
        TextView teamName = (TextView) view.findViewById(R.id.txt_teamName);
        TextView record = (TextView) view.findViewById(R.id.txt_record);
        TextView cd = (TextView) view.findViewById(R.id.txt_cd);

        String recordString = String.format("%d-%d", team.getWins(), team.getLosses());

        rank.setText(Integer.toString(i + 1));
        teamName.setText(team.getTeamName());
        record.setText(recordString);
        cd.setText(Integer.toString(team.getCD()));
        rank.setGravity(Gravity.CENTER_VERTICAL);
        record.setGravity(Gravity.CENTER_VERTICAL);
        teamName.setGravity(Gravity.CENTER_VERTICAL);
        cd.setGravity(Gravity.CENTER_VERTICAL);
        cd.setHeight(height);
        rank.setHeight(height);
        teamName.setHeight(height);
        record.setHeight(height);

        if (i % 2 == 0){
            view.setBackground(new ColorDrawable(Color.GRAY));
        }

        else {
            view.setBackground(new ColorDrawable(Color.DKGRAY));
            rank.setTextColor(Color.WHITE);
            teamName.setTextColor(Color.WHITE);
            record.setTextColor(Color.WHITE);
            cd.setTextColor(Color.WHITE);
        }

        return view;
    }
}
