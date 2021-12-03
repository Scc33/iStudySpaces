package com.example.istudyspace.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istudyspace.Adapters.AmentityAdapter;
import com.example.istudyspace.Adapters.HoursAdapter;
import com.example.istudyspace.Location;
import com.example.istudyspace.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InfoCardFragment extends Fragment implements View.OnClickListener {
    View view;
    Location location;
    String dayOfWeek;
    HashMap<String, HoursObject> hours = new HashMap<String, HoursObject>();
    List<Integer> amentityResourceIds = new ArrayList<Integer>();
    ImageView thumbsUp;
    ImageView thumbsDown;

    public class HoursObject {
        private String open;
        private String closed;

        public String getOpen() {return open;}
        public String getClosed() {return closed;}
    }

    public InfoCardFragment(Location l) {
        super(R.layout.info_card);
        location = l;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.info_card, container, false);
        if (location.getCoffee()) {
            int id = getResources().getIdentifier(
                    "com.example.istudyspace:drawable/coffee",
                    null,
                    null);
            amentityResourceIds.add(id);
        }
        if (location.getFood()) {
            int id = getResources().getIdentifier(
                    "com.example.istudyspace:drawable/food",
                    null,
                    null);
            amentityResourceIds.add(id);
        }
        if (location.getGroupWork()) {
            int id = getResources().getIdentifier(
                    "com.example.istudyspace:drawable/group",
                    null,
                    null);
            amentityResourceIds.add(id);
        }
        if (location.getZoom() != null) {
            int id = getResources().getIdentifier(
                    "com.example.istudyspace:drawable/zoom",
                    null,
                    null);
            amentityResourceIds.add(id);
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        thumbsUp = view.findViewById(R.id.thumbs_up);
        thumbsDown = view.findViewById(R.id.thumbs_down);
        thumbsUp.setOnClickListener(this);
        thumbsDown.setOnClickListener(this);

        RecyclerView rv = (RecyclerView) view.findViewById(R.id.amentity_list);
        rv.setAdapter(new AmentityAdapter(amentityResourceIds));
        //Set name
        ( (TextView) view.findViewById(R.id.location_name)).setText(location.getName());
//        ( (TextView) view.findViewById(R.id.location_name)).setText(location.getHours().toString());
        ( (TextView) view.findViewById(R.id.location_description)).setText(location.getDescription());
        //Set image
        int id = getResources().getIdentifier("com.example.istudyspace:drawable/" + location.getImageFile(), null, null);
        ( (ImageView) view.findViewById(R.id.location_image)).setImageResource(id);

        //Set open and closed times
        initializeHours(view);

        RecyclerView hoursView = (RecyclerView) view.findViewById(R.id.hoursRecycler);
        List<HoursObject> o = new ArrayList<>(hours.values());
        hoursView.setAdapter(new HoursAdapter(o));
        //Include link to room reservation

        //Include link to google maps directions

        //Include thumbs up/down component
    }

    public void onClick(View v) {
        if (v.getId() == R.id.thumbs_up) {
            int id = getResources().getIdentifier("com.example.istudyspace:drawable/" + "thumb_up_selected", null, null);
            thumbsUp.setImageResource(id);
            id = getResources().getIdentifier("com.example.istudyspace:drawable/" + "thumb_down", null, null);
            thumbsDown.setImageResource(id);
        }
        else if (v.getId() == R.id.thumbs_down) {
            int id = getResources().getIdentifier("com.example.istudyspace:drawable/" + "thumb_up", null, null);
            thumbsUp.setImageResource(id);
            id = getResources().getIdentifier("com.example.istudyspace:drawable/" + "thumb_down_selected", null, null);
            thumbsDown.setImageResource(id);
        }
    }

    private void initializeHours(View view) {
        Gson gson = new Gson();
        HashMap<String, Object> weeklyHours =
                gson.fromJson(location.getHours().toString(), HashMap.class);

        //Iterate through, adding times to list.
        for (String day : weeklyHours.keySet()) {
            HashMap<String, String> dayHours =
                    gson.fromJson(weeklyHours.get(day).toString(), HashMap.class);
            HoursObject h = new HoursObject();
            h.open = dayHours.get("open");
            h.closed = dayHours.get("close");
            hours.put(day, h);

            ((TextView) view.findViewById(R.id.hours_today)).setText(h.open + " - " + h.closed);
        }


        Calendar calendar;
    }

}
