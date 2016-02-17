package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import Utilities.Match;
import tournament.twentyonetournament2016.R;

/**
 * Created by brandonniedert on 2/10/16.
 */
public class TeamScheduleAdapter extends BaseAdapter {
    Context context;
    List<Match> matches;
    String name;

    public TeamScheduleAdapter(Context context, List<Match> matches, String name) {
        this.context = context;
        this.matches = matches;
        this.name = name;
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = infalInflater.inflate(R.layout.team_schedule_item, null);

        Match match = matches.get(i);

        TextView roundNumber = (TextView) view.findViewById(R.id.txt_roundNumber);
        TextView opponent = (TextView) view.findViewById(R.id.txt_opponent);
        TextView result = (TextView) view.findViewById(R.id.txt_result);
        TextView cupDifferential = (TextView) view.findViewById(R.id.txt_cupDifferential);

        roundNumber.setText(Integer.toString(i+1));
        opponent.setText(getOpponent(match));
        result.setText(getResult(match));
        cupDifferential.setText(Integer.toString(getCupDifferential(match)));


        return view;
    }

    private String getOpponent(Match match){
        String opponenet =  (match.getTeam1().equals(name)) ?  match.getTeam2() : match.getTeam1();
        return opponenet;
    }

    private String getResult(Match match){
        int teamNumber = (match.getTeam1().equals(name)) ? 1 : 2;
        if (match.getWinner() == 0){
            return "";
        }
        else if (match.getWinner() == teamNumber){
            return "W";
        }
        else{
            return "L";
        }

    }

    private int getCupDifferential(Match match){
        String result = getResult(match);
        if (result.equals("")){
            return 0;
        }
        else if (result.equals("W")){
            return match.getCupDifferential();
        }
        else{
            return (match.getCupDifferential() * -1);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
