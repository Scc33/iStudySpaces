package com.example.istudyspace;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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

//    Location currentLocation;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    private static final int REQUEST_CODE = 101;
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

    private GoogleMap map;
    private List<Location> locations;
    private List<Marker> markers;

    private final int DEFAULT_WIDTH = 69;
    private final int DEFAULT_HEIGHT = 110;

    private final int ZOOM_WIDTH = 94;
    private final int ZOOM_HEIGHT = 135;
    private final double ZOOM_PIN_OFFSET = 0.0002;

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
        // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // fetchLocation();
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
                    updateToDefaultPins();
                } else {
                    tabOn = "Zoom";
                    zoomFiltersButton.setVisibility(View.VISIBLE);
                    updateToZoomPins();
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
        System.out.println("Something pressed" + v.getId());
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

//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
//                    assert supportMapFragment != null;
//                    supportMapFragment.getMapAsync(MainActivity.this);
//                }
//            }
//        });
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap; // Set global map
        markers = new ArrayList<>();
        InputStream inputStream = getResources().openRawResource(R.raw.locations);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        Gson gson = new Gson();

        BitmapDescriptor defaultPin = getBitmapIcon(R.drawable.pin_default, DEFAULT_WIDTH, DEFAULT_HEIGHT);

        locations = convertJSON(bufferedReader.lines().collect(Collectors.joining()), Location[].class);
        for(Location location : locations) {
            markers.add(googleMap.addMarker(new MarkerOptions().icon(defaultPin).position(location.getCoords()).title(location.getName())));
        }

//        LatLng[] places = new LatLng[100];
//        String filePath = "../../res/values/locations/locations.txt";
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(filePath));
//            String st = "";
//            while ((st = br.readLine()) != null) {
//                System.out.println(st);
//            }
//        } catch (Exception e) {
//
//        }

//        for (int i = 0; i <)

        LatLng cur_position = new LatLng(40.108014, -88.227265);
        MarkerOptions markerOptions = new MarkerOptions().position(cur_position).title("You")
                .draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        googleMap.animateCamera(CameraUpdateFactory.newLatLng(cur_position));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(cur_position, 15));
        googleMap.addMarker(markerOptions);

        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Grainger, 18), 4000, null);
    }

    public static <T> List<T> convertJSON(String s, Class<T[]> type) {
        return Arrays.asList(new Gson().fromJson(s, type));
    }

    private void updateToDefaultPins() {
        // Remove a couple example markers
        for (Marker m: markers) {
            if (m.getTitle().equals("Grainger Library")) {
                m.setVisible(false);
            } else if (m.getTitle().equals("UGL")) {
                m.setVisible(false);
            }
        }
        // Add 2 example zoom markers
        Location location = locations.get(0); // Grainger
        BitmapDescriptor defaultPin = getBitmapIcon(R.drawable.pin_default, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        markers.add(map.addMarker(new MarkerOptions().icon(defaultPin).position(location.getCoords()).title(location.getName())));
        location = locations.get(2); // UGL
        markers.add(map.addMarker(new MarkerOptions().icon(defaultPin).position(location.getCoords()).title(location.getName())));
    }

    private void updateToZoomPins() {
        // Remove a couple example markers
        for (Marker m: markers) {
            if (m.getTitle().equals("Grainger Library")) {
                m.setVisible(false);
            } else if (m.getTitle().equals("UGL")) {
                m.setVisible(false);
            }
        }
        // Add 2 example zoom markers
        Location location = locations.get(0); // Grainger
        BitmapDescriptor zoomPin = getBitmapIcon(R.drawable.pin_6, ZOOM_WIDTH, ZOOM_HEIGHT);
        markers.add(map.addMarker(new MarkerOptions().icon(zoomPin).position(new LatLng(location.getLat(), location.getLon() + ZOOM_PIN_OFFSET)).title(location.getName())));
        location = locations.get(2); // UGL
        zoomPin = getBitmapIcon(R.drawable.pin_2, ZOOM_WIDTH, ZOOM_HEIGHT);
        markers.add(map.addMarker(new MarkerOptions().icon(zoomPin).position(new LatLng(location.getLat(), location.getLon() + ZOOM_PIN_OFFSET)).title(location.getName())));
    }

    private BitmapDescriptor getBitmapIcon(int resourceId, int width, int height) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), resourceId);
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        return BitmapDescriptorFactory.fromBitmap(smallMarker);
    }
}