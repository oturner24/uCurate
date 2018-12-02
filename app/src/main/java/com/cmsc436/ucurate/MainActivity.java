package com.cmsc436.ucurate;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dbTest();

        /*
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        */

        Button launchTourList = findViewById(R.id.button3);
        launchTourList.setOnClickListener(new OnClickListener() {
             @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(MainActivity.this, TourListActivity.class);
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

                /*
                Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent3);
                */
            }
        });

    }

    public void dbTest(){
        DatabaseAccessor db = new DatabaseAccessor();

        /*
        Stop stop = new Stop();
        stop.setTitle("Test Pin");
        stop.setDescription("This is a test pin");
        stop.setCoordinate(new LatLng(1,1));

        db.insertPin(stop,"12345");

        Tour tour = new Tour();
        tour.setTitle("Test Pin");
        tour.setDescription("This is a test pin");
        tour.setStops(new Stop[]{stop});

        db.insertTour(tour, "12345");
        */

        Stop[] stops = db.getAllPins();

        Log.i("database", String.valueOf(stops.length));
        for (Stop s : stops){
            Log.i("database", s.getTitle());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //TODO implement
    }
}
