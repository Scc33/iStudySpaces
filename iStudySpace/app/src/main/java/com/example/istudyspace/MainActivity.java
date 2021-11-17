package com.example.istudyspace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.graphics.Rect;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import android.widget.EditText;
import android.widget.SearchView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import static android.view.View.NO_ID;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {


    private Button filtersButton;
    private Button zoomFiltersButton;
    private MaterialButtonToggleGroup zoomInteractionsTabGroup;
    private Button anyInteraction;
    private Button minInteraction;
    private Button medInteraction;
    private Button maxInteraction;
    private TabLayout tabLayout;

    private String tabOn = "Study";
    private String noiseLevel = "any";
    private Boolean groupWork = false;
    private Boolean coffee = false;
    private Boolean food = false;
    private SearchView searchView;
    private androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tabOn = extras.getString("tab");
            noiseLevel = extras.getString("noiseLevel");;
            groupWork = extras.getBoolean("groupWork");;
            coffee = extras.getBoolean("coffee");
            food = extras.getBoolean("food");
        }
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        mapFragment.getMapAsync(this);

        filtersButton = (Button) findViewById(R.id.filters);
        zoomFiltersButton = (Button) findViewById(R.id.zoomFilter);
        zoomInteractionsTabGroup = (MaterialButtonToggleGroup) findViewById(R.id.zoomInteractionToggle);
        anyInteraction = (Button) findViewById(R.id.anyButton);
        minInteraction = (Button) findViewById(R.id.lowButton);
        medInteraction = (Button) findViewById(R.id.medButton);
        maxInteraction = (Button) findViewById(R.id.maxButton);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (tabOn.equals("Study")) {
            zoomFiltersButton.setVisibility(View.GONE);
            tabLayout.selectTab(tabLayout.getTabAt(0));
        } else {
            tabOn = "Zoom";
            zoomFiltersButton.setVisibility(View.VISIBLE);
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }

        filtersButton.setOnClickListener(this);
        zoomFiltersButton.setOnClickListener(this);
        zoomInteractionsTabGroup.setOnClickListener(this);
        anyInteraction.setOnClickListener(this);
        minInteraction.setOnClickListener(this);
        medInteraction.setOnClickListener(this);
        maxInteraction.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                System.out.println("selected" + tab.getId() + tab.getText());
                if ((tab.getText()).equals("Study")) {
                    tabOn = "Study";
                    zoomFiltersButton.setVisibility(View.GONE);
                    zoomInteractionsTabGroup.setVisibility(View.GONE);
                } else {
                    tabOn = "Zoom";
                    zoomFiltersButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                System.out.println("unselected" + tab.getId());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                System.out.println("reselected" + tab.getId());
            }
        });
    }

    public void onClick(View v) {
        if (v.getId() == R.id.filters) {
            Intent intent = new Intent(this, FilterActivity.class);
            intent.putExtra("tab", tabOn);
            intent.putExtra("noiseLevel", noiseLevel);
            intent.putExtra("groupWork", groupWork);
            intent.putExtra("coffee", coffee);
            intent.putExtra("food", food);
            startActivity(intent);
        } else if (v.getId() == R.id.zoomFilter) {
            zoomFiltersButton.setVisibility(View.GONE);
            zoomInteractionsTabGroup.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.anyButton) {
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        } else if (v.getId() == R.id.lowButton) {
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        } else if (v.getId() == R.id.medButton) {
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        } else if (v.getId() == R.id.maxButton) {
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        }
    }

    @Override
    /*
    Closes soft keyboard when interact outside of searchView
     */
    public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            View v = getCurrentFocus();
            if (v instanceof EditText){
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())){
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {

        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();

        List<Location> locations = convertJSON(bufferedReader.lines().collect(Collectors.joining()), Location[].class);

        for(Location location : locations) {
            googleMap.addMarker(new MarkerOptions().position(location.getCoords()).title(location.getName()));
        }

        LatLng cur_position = new LatLng(40.108014, -88.227265);
        MarkerOptions markerOptions = new MarkerOptions().position(cur_position).title("You")
                .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(cur_position));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur_position, 15));
        googleMap.addMarker(markerOptions);

        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Grainger, 18), 4000, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // inflate menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_menu, menu);

        // initialize menu item search bar
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
        searchView = (SearchView) searchViewItem.getActionView();

        // searchAutoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
        }

//        final androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete =
//                (androidx.appcompat.widget.SearchView.SearchAutoComplete) searchView.findViewById();

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    searchViewItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });



        return super.onCreateOptionsMenu(menu);
    }


    public static <T> List<T> convertJSON(String s, Class<T[]> type) {
        return Arrays.asList(new Gson().fromJson(s, type));
    }
}