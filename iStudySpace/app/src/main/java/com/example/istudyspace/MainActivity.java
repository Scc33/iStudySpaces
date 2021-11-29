package com.example.istudyspace;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.icu.text.IDNA;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.example.istudyspace.Fragments.InfoCardFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener {
    private Context context;
    private GoogleMap map;

    private Button filtersButton;
    private TabLayout tabLayout;
    private LinearLayout bottomSheetLayout;

    private BottomSheetBehavior sheetBehavior;

    private Map<Marker, Location> markerLocationMap;

    private Button random;
    private Random randomGenerator;

    private String tabOn = "Study";
    private String noiseLevel = "any";
    private Boolean groupWork = false;
    private Boolean coffee = false;
    private Boolean food = false;
    private String zoomInteraction = "any";
    private List<Location> locations;
    private List<String> all_location;
    private MenuItem searchMenu;

    private ActivityResultLauncher<Intent> filterResultLauncher;

    private final int DEFAULT_WIDTH = 69;
    private final int DEFAULT_HEIGHT = 110;

    private final int ZOOM_WIDTH = 94;
    private final int ZOOM_HEIGHT = 135;
    private final double ZOOM_PIN_OFFSET = 0.0002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = getApplicationContext();
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

        // Setup Result Catcher to get data from Filter Activity
        // Allows us to leave MainActivity running for when user goes into filters
        filterResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras1 = result.getData().getExtras();
                        groupWork = extras1.getBoolean("groupWork");
                        coffee = extras1.getBoolean("coffee");
                        food = extras1.getBoolean("food");
                        //Log.d("Coffee", coffee.toString());
                        updateMap();
                    }
                });

        // Get ActionBar
        ActionBar actionBar = getSupportActionBar();
        // Set below attributes to add logo in ActionBar.
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("iStudySpace");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        mapFragment.getMapAsync(this);

        filtersButton = (Button) findViewById(R.id.filters);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        random = (Button) findViewById(R.id.random);
        randomGenerator = new Random();

        bottomSheetLayout = (LinearLayout) findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        random.setOnClickListener(this);
        sheetBehavior.setPeekHeight(400);
        sheetBehavior.setHideable(true);

        markerLocationMap = new HashMap<>();

        if (tabOn.equals("Study")) {
            tabOn = "Study";
            tabLayout.selectTab(tabLayout.getTabAt(0));
        } else {
            tabOn = "Zoom";
            tabLayout.selectTab(tabLayout.getTabAt(1));
        }
        filtersButton.setOnClickListener(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((tab.getText()).equals("Study")) {
                    tabOn = "Study";
                    noiseLevel="any";
                    zoomInteraction="any";
                    updateToDefaultPins();
                } else {
                    tabOn = "Zoom";
                    noiseLevel="any";
                    zoomInteraction="any";
                    updateToZoomPins();
                }
                updateMap();
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v) {
        if (v.getId() == R.id.filters) {
            Intent intent = new Intent(this, FilterActivity.class);
            intent.putExtra("tab", tabOn);
            intent.putExtra("noiseLevel", noiseLevel);
            intent.putExtra("groupWork", groupWork);
            intent.putExtra("coffee", coffee);
            intent.putExtra("food", food);
            intent.putExtra("zoom", zoomInteraction);
            filterResultLauncher.launch(intent);
        } else if (v.getId() == R.id.random) {
            int index = randomGenerator.nextInt(locations.size());
            Location l = locations.get(index);
            focus_pin(l.getName());
        }
    }

    private void updateMap() {
        // Check for markers that need to be removed by the filters
        for (Marker m: markerLocationMap.keySet()) {
            Location loc = markerLocationMap.get(m);
            // Start with marker visible and remove based on each filter
            m.setVisible(true);
            if (coffee && !loc.getCoffee()) {
                m.setVisible(false);
            }
            else if (groupWork && !loc.getGroupWork()) {
                m.setVisible(false);
            }
            else if (food && !loc.getFood()) {
                m.setVisible(false);
            }
            else if (tabOn.equals("Study") && !noiseLevel.equals("any") && !noiseLevel.equals(loc.getNoiseLevel())) {
                m.setVisible(false);
            }
            else if (tabOn.equals("Zoom") && !zoomInteraction.equals("any") && !zoomInteraction.equals(loc.getZoom())) {
                m.setVisible(false);
            }
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
                    searchMenu.collapseActionView();
                }
            }
        }

        return super.dispatchTouchEvent(event);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);

        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();

        BitmapDescriptor defaultPin = getBitmapIcon(R.drawable.pin_default, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        Type listType = new TypeToken<List<Location>>(){}.getType();
        locations = gson.fromJson(bufferedReader, listType);
//        locations = convertJSON(bufferedReader.lines().collect(Collectors.joining()), Location[].class);
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
            if (tabOn.equals("Study") && !noiseLevel.equals("any") && !noiseLevel.equals(location.getNoiseLevel())) {
                continue;
            }
            if (tabOn.equals("Zoom") && !zoomInteraction.equals("any") && !zoomInteraction.equals(location.getZoom())) {
                continue;
            }
            Marker m = googleMap.addMarker(
                    new MarkerOptions()
                            .icon(defaultPin)
                            .position(location.getCoords())
                            .title(location.getName())
            );
            markerLocationMap.put(m, location);
        }
        //Set position for current location in middle of campus
        LatLng cur_position = new LatLng(40.108014, -88.227265);
        MarkerOptions markerOptions = new MarkerOptions().position(cur_position).title("You")
                .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        googleMap.addMarker(markerOptions);

        //Center camera on current location
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(cur_position));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur_position, 15));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.bottom_sheet, fragment);
        fragmentTransaction.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onMarkerClick(final Marker marker) {
        Location l = markerLocationMap.get(marker);
        loadFragment(new InfoCardFragment(l));
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        return false;
    }

    @Override
    public void onMapClick(LatLng l) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        bottomSheetLayout.setVisibility(View.INVISIBLE);
        ((ImageView) findViewById(R.id.location_image)).setImageResource(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.nav_menu, menu);
        // Get the search menu.
        searchMenu = menu.findItem(R.id.app_bar_menu_search);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);
                closeKeyboard();

                focus_pin(queryString);
                //TODO: replace the Toast output with the action "clicking" the pin on the map
                //Toast.makeText(MainActivity.this, "You clicked " + queryString, Toast.LENGTH_LONG).show();

            }
        });
        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextSubmit(String query) {
                closeKeyboard();
                if (!all_location.contains(query)) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setMessage("Location is Invalid! ");
                    alertDialog.show();
                }
                else{
                    focus_pin(query);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void focus_pin(String loc){
        for (Map.Entry<Marker, Location> entry : markerLocationMap.entrySet()) {
            Location location = entry.getValue();

            if (location.getName().equals(loc)){
                Marker marker = entry.getKey();
                Marker m = map.addMarker(
                        new MarkerOptions()
                                .position(location.getCoords())
                                .title(location.getName()));

                map.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), 18));
                marker.showInfoWindow();
                onMarkerClick(marker);
                break;
            }
        }
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

    private void updateToDefaultPins() {
        // Remove a couple example markers
        BitmapDescriptor defaultPin = getBitmapIcon(R.drawable.pin_default, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        for (Marker m: markerLocationMap.keySet()) {
            if (m.getTitle().equals("Grainger Library")) {
                m.setIcon(defaultPin);
            } else if (m.getTitle().equals("Undergraduate Library")) {
                m.setIcon(defaultPin);
            }
        }
        /*
         Add 2 example zoom markers
        Location location = locations.get(4); // Grainger
        BitmapDescriptor defaultPin = getBitmapIcon(R.drawable.pin_default, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        markerLocationMap.put(map.addMarker(new MarkerOptions().icon(defaultPin).position(location.getCoords()).title(location.getName())), location);
        location = locations.get(1); // UGL
        markerLocationMap.put(map.addMarker(new MarkerOptions().icon(defaultPin).position(location.getCoords()).title(location.getName())), location);
        */
    }

    private void updateToZoomPins() {
        // Remove a couple example markers
        BitmapDescriptor zoomPin;
        for (Marker m: markerLocationMap.keySet()) {
            if (m.getTitle().equals("Grainger Library")) {
                zoomPin = getBitmapIcon(R.drawable.pin_6, ZOOM_WIDTH, ZOOM_HEIGHT);
                m.setIcon(zoomPin);
            } else if (m.getTitle().equals("Undergraduate Library")) {
                zoomPin = getBitmapIcon(R.drawable.pin_2, ZOOM_WIDTH, ZOOM_HEIGHT);
                m.setIcon(zoomPin);
            }
        }
        /*
         Add 2 example zoom markers
        Location location = locations.get(4); // Grainger
        BitmapDescriptor zoomPin = getBitmapIcon(R.drawable.pin_6, ZOOM_WIDTH, ZOOM_HEIGHT);
        markerLocationMap.put(map.addMarker(new MarkerOptions().icon(zoomPin).position(new LatLng(location.getLat(), location.getLon() + ZOOM_PIN_OFFSET)).title(location.getName())), location);
        location = locations.get(1); // UGL
        zoomPin = getBitmapIcon(R.drawable.pin_2, ZOOM_WIDTH, ZOOM_HEIGHT);
        markerLocationMap.put(map.addMarker(new MarkerOptions().icon(zoomPin).position(new LatLng(location.getLat(), location.getLon() + ZOOM_PIN_OFFSET)).title(location.getName())), location);
        */
    }

    private BitmapDescriptor getBitmapIcon(int resourceId, int width, int height) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }
}