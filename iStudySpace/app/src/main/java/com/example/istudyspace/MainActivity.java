package com.example.istudyspace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap map;

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
    private String zoomInteraction = "any";
    private List<Location> locations;
    private List<String> all_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tabOn = extras.getString("tab");
            noiseLevel = extras.getString("noiseLevel");
            groupWork = extras.getBoolean("groupWork");
            coffee = extras.getBoolean("coffee");
            food = extras.getBoolean("food");
            zoomInteraction = extras.getString("zoom");
            /*map.addMarker(new MarkerOptions()
                    .position(new LatLng( 40.10954764345154,-88.227268))
                    .title("This is my title")
                    .snippet("and snippet")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));*/
        }
        setContentView(R.layout.activity_main);

        // Get ActionBar
        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("iStudySpace");

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
                    zoomInteraction="any";
                    updateZoom();
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
            intent.putExtra("zoom", zoomInteraction);
            startActivity(intent);
        } else if (v.getId() == R.id.zoomFilter) {
            zoomInteraction="any";
            zoomFiltersButton.setVisibility(View.GONE);
            zoomInteractionsTabGroup.setVisibility(View.VISIBLE);
        } else if (v.getId() == R.id.anyButton) {
            zoomInteraction="any";
            updateZoom();
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        } else if (v.getId() == R.id.lowButton) {
            zoomInteraction="minimal";
            updateZoom();
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        } else if (v.getId() == R.id.medButton) {
            zoomInteraction="moderate";
            updateZoom();
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        } else if (v.getId() == R.id.maxButton) {
            zoomInteraction="maximal";
            updateZoom();
            String buttonText = anyInteraction.getText().toString();
            Toast.makeText(this, "asdf", Toast.LENGTH_SHORT).show();
            zoomFiltersButton.setVisibility(View.VISIBLE);
            zoomInteractionsTabGroup.setVisibility(View.GONE);
        }
    }

    private void updateZoom() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", tabOn);
        intent.putExtra("noiseLevel", noiseLevel);
        intent.putExtra("groupWork", groupWork);
        intent.putExtra("coffee", coffee);
        intent.putExtra("food", food);
        intent.putExtra("zoom", zoomInteraction);
        startActivity(intent);
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
        map = googleMap;

        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();

        List<Location> locations = convertJSON(bufferedReader.lines().collect(Collectors.joining()), Location[].class);

        for(Location location : locations) {
            if (coffee && !location.getCoffee()) {
                continue;
            }
            if (groupWork && !location.getGroupWork()) {
                continue;
            }
            if (food && !location.getFood()) {
                continue;
            }
            if ((noiseLevel.equals("quiet") || noiseLevel.equals("ambient") || noiseLevel.equals("loud"))&& !noiseLevel.equals(location.getNoiseLevel())) {
                continue;
            }
            if ((tabOn.equals("Zoom") && !zoomInteraction.equals("any")) && !zoomInteraction.equals(location.getZoom())) {
                continue;
            }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nav_menu, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.app_bar_menu_search);
        // Get SearchView object.
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchMenu.getActionView();
        // Get SearchView autocomplete object.
        final androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete = (androidx.appcompat.widget.SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);

        searchAutoComplete.setBackgroundColor(Color.parseColor("#FFDD3403"));
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.darker_gray);

        // Create a new ArrayAdapter and add data to search auto complete object.
        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();

        List<Location> locations = convertJSON(bufferedReader.lines().collect(Collectors.joining()),
                Location[].class);

        String dataArr[] = new String[locations.size()];
        all_location = new ArrayList<String>();
        all_location.toArray(dataArr);
        for (Location location : locations){
            all_location.add(location.getName());
        }
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, all_location);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                closeKeyboard();

                //TODO: replace the Toast output with the action "clicking" the pin on the map
                Toast.makeText(MainActivity.this, "You clicked " + queryString, Toast.LENGTH_LONG).show();

            }
        });
        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                if (!all_location.contains(query)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("Location is Invalid! ");
                    alertDialog.show();
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });



        return super.onCreateOptionsMenu(menu);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public static <T> List<T> convertJSON(String s, Class<T[]> type) {
        return Arrays.asList(new Gson().fromJson(s, type));
    }

}