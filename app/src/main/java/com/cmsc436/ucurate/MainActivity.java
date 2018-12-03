package com.cmsc436.ucurate;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Stop stop;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button launchTourList = findViewById(R.id.button3);
        launchTourList.setOnClickListener(new OnClickListener() {
             @Override
                public void onClick(View v) {
                    //TODO: Get all tours from database
                    DatabaseAccessor db = new DatabaseAccessor();
                    Tour[] tours = db.getAllTours();
                    Intent intent1 = new Intent(MainActivity.this, TourListActivity.class);
                    intent1.putExtra("TOURS", tours);
                    startActivity(intent1);

                }
        });

        Button launchAddPin = findViewById(R.id.button4);
        launchAddPin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, AddPin.class);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button2);
        launchProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent3);
            }
        });

    }

    public void dbTest(){
        DatabaseAccessor db = new DatabaseAccessor();

        /*
        Stop test1 = new Stop("Pin", new LatLng(38.9869,-76.9426));
        test1.setDescription("This is the description.");
        Stop test2 = new Stop("Pin 2", new LatLng(38, -76.8));
        Stop test3 = new Stop("Pin 3", new LatLng(38.2, -76.3));

        db.insertPin(test1,"12345");
        db.insertPin(test2,"12345");
        db.insertPin(test3,"12345");

        Tour tour = new Tour();
        tour.setTitle("Test Pin2");
        tour.setDescription("This is a test pin");
        tour.setStops(new Stop[]{test1});

        db.insertTour(tour, "12345");

        */

        Stop[] stops = db.getAllPins();

        Log.i("database", String.valueOf(stops.length));
        for (Stop s : stops){
            Log.i("database", s.getTitle());
        }

        Button launchAddTour = findViewById(R.id.button4);
        launchAddTour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, CreateTour.class);
                startActivity(intent4);

            }
        });

        Button launchHome = findViewById(R.id.button5);
        launchHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent5);

            }
        });

        Button launchAddPin = findViewById(R.id.button6);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, AddPin.class);
                startActivity(intent2);

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //set marker at current location
        LatLng loc = stop.getCoordinate();
        mMap.addMarker(new MarkerOptions().position(loc).title(stop.getTitle())).setDraggable(true);
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                //do nothing
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                //do nothing
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                stop.setCoordinate(marker.getPosition());
            }
        });
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}
