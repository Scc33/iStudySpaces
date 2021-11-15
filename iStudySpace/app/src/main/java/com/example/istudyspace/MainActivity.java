package com.example.istudyspace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {


    private Button filtersButton;
    private SearchView searchView;
    private androidx.appcompat.widget.SearchView.SearchAutoComplete searchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                .title("Illini Union"));

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

}