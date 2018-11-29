package com.cmsc436.ucurate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

public class TourActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Tour mTour;
    private Stop[] stops;
    private static final String TOUR = "TOUR";
    private GoogleMap mMap;
    private Button mButton;
    private int curr;
    private PolylineOptions complete;
    private LatLng currLoc;
    private Polyline polylineC;



    private static final int PATTERN_DASH_LENGTH_PX = 20;
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DASH = new Dash(PATTERN_DASH_LENGTH_PX);
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DASH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        mTour = getIntent().getParcelableExtra(TOUR);
        stops = mTour.getStops();

        mButton = findViewById(R.id.button2);
        curr = 0;

        complete = new PolylineOptions();
        currLoc = stops[curr].getCoordinate();
        complete.add(currLoc);


        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update line color/dotted
                //change cameraview to next loc
                curr++;
                if(curr == stops.length){
                    Intent intent = new Intent(TourActivity.this, TourListActivity.class);

                    startActivity(intent);
                    finish();
                } else if(curr == stops.length - 1){
                    mButton.setText("End");
                }

                updateMap();
            }
        });
    }

    public void onMapReady(GoogleMap map){
        mMap = map;

        PolylineOptions route = new PolylineOptions();

        for(int i = 0; i < stops.length; i++){
            LatLng loc = stops[i].getCoordinate();
            String title = stops[i].getTitle();
            route.add(loc);
            mMap.addMarker(new MarkerOptions().position(loc).title(title));
        }

        route.add(stops[0].getCoordinate());
        Polyline polyline = mMap.addPolyline(route);
        polyline.setPattern(PATTERN_POLYGON_ALPHA);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stops[0].getCoordinate(), 20));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

    }

    private void updateMap(){
        currLoc = stops[curr].getCoordinate();
        complete.add(currLoc);
        polylineC = mMap.addPolyline(complete);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stops[curr].getCoordinate(), 20));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);


    }
}
