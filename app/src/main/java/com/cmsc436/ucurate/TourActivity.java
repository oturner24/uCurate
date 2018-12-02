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

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;

import com.google.android.gms.maps.model.Marker;


public class TourActivity extends AppCompatActivity implements OnMapReadyCallback{

    private Tour mTour;
    private Stop[] stops;
    private static final String TOUR = "TOUR";
    private GoogleMap mMap;
    private Button mButton, mBackButton;
    private int curr;
    private PolylineOptions complete;
    private LatLng currLoc;
    private Polyline polylineC;
    private Marker[] markers;
    private static String TAG = "TA";



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
        mBackButton = findViewById(R.id.button3);
        mBackButton.setEnabled(false);
        curr = 0;

        editDescription();

        complete = new PolylineOptions();
        currLoc = stops[curr].getCoordinate();
        complete.add(currLoc);



        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update line color/dotted
                //change cameraview to next loc
                curr++;
                if(mButton.getText().equals("End")){
                    Intent intent = new Intent(TourActivity.this, TourListActivity.class);

                    startActivity(intent);
                    finish();
                } else {
                    updateMapNext();
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr--;
                updateMapBack();
            }
        });

    }

    public void onMapReady(GoogleMap map){
        mMap = map;

        PolylineOptions route = new PolylineOptions();

        markers = new Marker[stops.length];

        for(int i = 0; i < stops.length; i++){
            LatLng loc = stops[i].getCoordinate();
            String title = stops[i].getTitle();
            route.add(loc);
            markers[i] = mMap.addMarker(new MarkerOptions().position(loc).title(title));
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

        final ImagePopup imagePopup = new ImagePopup(this);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG,"I've been clicked");

                Marker currMarker = markers[curr];
                if (marker.equals(currMarker)){
                    Log.d(TAG,"right marker");
                    Bitmap img1 = stops[curr].getImage();
                    /*
                    Bitmap preimg = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                            R.drawable.img1);
                    Bitmap img1 = scaleDownBitmap(preimg);
                    */

                    Drawable img = new BitmapDrawable(getResources(), img1);
                    imagePopup.initiatePopup(img);
                    imagePopup.viewPopup();


                }

                return true;
            }
        });

    }

    private void updateMapNext(){
        currLoc = stops[curr].getCoordinate();
        complete.add(currLoc);
        polylineC = mMap.addPolyline(complete);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stops[curr].getCoordinate(), 20));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

        if(curr > 0){
            mBackButton.setEnabled(true);
        }

        if(curr == stops.length - 1) {
            mButton.setText("End");
        }

        editDescription();


    }

    private void updateMapBack(){
        complete = new PolylineOptions();
        polylineC.remove();


        for(int i = 0; i <= curr; i++){
            LatLng loc = stops[i].getCoordinate();
            complete.add(loc);
        }

        polylineC = mMap.addPolyline(complete);



        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stops[curr].getCoordinate(), 20));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(20), 2000, null);

        if(curr == 0){
            mBackButton.setEnabled(false);
        }

        if(curr < stops.length - 1){
            mButton.setText("Next");
        }

        editDescription();
    }


    public void editDescription(){
        TextView tv = findViewById(R.id.textView5);
        tv.setText(stops[curr].getDescription());
    }
}