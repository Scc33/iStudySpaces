package com.example.istudyspace.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istudyspace.Fragments.InfoCardFragment;
import com.example.istudyspace.R;

import org.w3c.dom.Text;

import java.util.List;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.ViewHolder> {
    private List<InfoCardFragment.HoursObject> Hours;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView dayView;
        private final TextView hoursView;

        public ViewHolder(View view) {
            super(view);
            dayView = (TextView) view.findViewById(R.id.day_of_week);
            hoursView = (TextView) view.findViewById(R.id.hours);
        }

        public TextView getHoursView() { return hoursView; }
        public TextView getDayView() { return dayView; }
    }

    public HoursAdapter(List<InfoCardFragment.HoursObject> hours) { this.Hours = hours; }

    @Override
    public HoursAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hoursitem, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HoursAdapter.ViewHolder viewHolder, final int position) {
        String day = new String();
        switch (position) {
            case (0) :
                day = "S";
                break;
            case (1) :
                day = "M";
                break;
            case (2) :
                day = "T";
                break;
            case (3) :
                day = "W";
                break;
            case (4) :
                day = "TH";
                break;
            case (5) :
                day = "F";
                break;
            case (6) :
                day = "S";
                break;
        }
        viewHolder.getDayView().setText(day + ":");
        InfoCardFragment.HoursObject h = Hours.get(position);
        String toDisplay = h.getOpen() + "-" + h.getClosed();
        viewHolder.getHoursView().setText(toDisplay);
    }

    @Override
    public int getItemCount() {
        return Hours.size();
    }
}
