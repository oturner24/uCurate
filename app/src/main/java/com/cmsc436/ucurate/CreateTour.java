package com.cmsc436.ucurate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CreateTour extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private StopListAdapter mAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng mCoords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tour);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mRecyclerView = findViewById(R.id.pin_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("pins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Stop> stops = new ArrayList<Stop>();
                for (DataSnapshot pinSnapshot : dataSnapshot.getChildren()){
                    Stop temp = new Stop();
                    DatabaseAccessor.createPinFromSnapshot(temp, pinSnapshot);
                    stops.add(temp);
                }

                mAdapter = new StopListAdapter(CreateTour.this, stops.toArray(new Stop[0]));
                mRecyclerView.setAdapter(mAdapter);

                findViewById(R.id.create_tour_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = ((TextView) findViewById(R.id.tour_name)).getText().toString().trim();
                        ArrayList<Stop> selected = mAdapter.getSelected();
                        if (title.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Must enter a title.", Toast.LENGTH_LONG).show();
                        } else if (selected.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Must select at least one pin.", Toast.LENGTH_LONG).show();
                        } else {
                            Tour tour = new Tour(title);
                            //TODO set description
                            tour.setStops(selected.toArray(new Stop[selected.size()]));
                            DatabaseAccessor db = new DatabaseAccessor();
                            //TODO replace with real userID
                            db.insertTour(tour, "12345");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });



        //Start callback

        //End callback
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mAdapter.setMap(mMap);

        //ask for location permissions if necessary
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            moveToLastLoc();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED && permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    moveToLastLoc();
                }
            }
        }
    }

    //sets the stop's coordinates to the current location
    private void moveToLastLoc() {
        mFusedLocationClient.getLastLocation() //disregard this red line - asking for permissions earlier
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            mCoords = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoords, 50));
                            // Zoom in, animating the camera.
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());
                            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        }
                    }
                });
    }

}
