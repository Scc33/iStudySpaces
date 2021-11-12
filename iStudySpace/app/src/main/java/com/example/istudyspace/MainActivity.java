package com.example.istudyspace;

import androidx.appcompat.app.AppCompatActivity;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

//    Location currentLocation;
//    FusedLocationProviderClient fusedLocationProviderClient;
//    private static final int REQUEST_CODE = 101;
    private Button filtersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // fetchLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);

        mapFragment.getMapAsync(this);

        filtersButton = (Button) findViewById(R.id.filters);

        filtersButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.filters) {
            Intent intent = new Intent(this, FilterActivity.class);
            startActivity(intent);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng Grainger = new LatLng(40.11270, -88.22692);
        googleMap.addMarker(new MarkerOptions()
                .position(Grainger)
                .title("Grainger Library"));

        LatLng[] places = {new LatLng(40.10954764345154, -88.22724722308422),
                new LatLng(40.10485852066087, -88.22622205875562), new LatLng(40.102880814557395, -88.22425868195258),
                new LatLng(40.10600026564582, -88.21903799312537), new LatLng(40.11682456862754, -88.22881269128017),
                new LatLng(40.1084316354503, -88.22968738268322), new LatLng(40.11101728663284, -88.22391634018292)};

        googleMap.addMarker(new MarkerOptions()
                .position(places[0])
                .title("Illini Union")
                .snippet("Zoom 6"));

        googleMap.addMarker(new MarkerOptions()
                .position(places[1])
                .title("UGL"));

        googleMap.addMarker(new MarkerOptions()
                .position(places[2])
                .title("Funk Library"));

        googleMap.addMarker(new MarkerOptions()
                .position(places[3])
                .title("Caffe Paradiso"));

        googleMap.addMarker(new MarkerOptions()
                .position(places[4])
                .title("Bearology"));

        googleMap.addMarker(new MarkerOptions()
                .position(places[5])
                .title("Illini Union Bookstore"));

        googleMap.addMarker(new MarkerOptions()
                .position(places[6])
                .title("Loomis Laboratory"));

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
}