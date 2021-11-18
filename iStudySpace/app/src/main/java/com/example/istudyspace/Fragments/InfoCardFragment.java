package com.example.istudyspace.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.istudyspace.Adapters.AmentityAdapter;
import com.example.istudyspace.Location;
import com.example.istudyspace.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfoCardFragment extends Fragment {
    View view;
    Location location;
    List<Integer> amentityResourceIds = new ArrayList<Integer>();

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

        //TODO: add zoom to icons
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.amentity_list);
        rv.setAdapter(new AmentityAdapter(amentityResourceIds));
        //Set name
        ( (TextView) view.findViewById(R.id.location_name)).setText(location.getName());

        //Set image
        int id = getResources().getIdentifier("com.example.istudyspace:drawable/" + location.getImageFile(), null, null);
        ( (ImageView) view.findViewById(R.id.location_image)).setImageResource(id);

        //Set open and closed times

        //Configure business hours

        //Include link to room reservation

        //Include link to google maps directions

        //Include thumbs up/down component
    }


}
