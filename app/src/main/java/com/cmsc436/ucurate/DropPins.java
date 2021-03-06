package com.cmsc436.ucurate;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DropPins extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Stop stop;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_pins);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //retrieve the stop object passed by add pin
        stop = getIntent().getParcelableExtra("stop");
        userID = getIntent().getStringExtra("userID");

        //populate screen with stop info
        ((TextView) findViewById(R.id.drop_title)).setText(stop.getTitle());
        ((TextView) findViewById(R.id.drop_des)).setText(stop.getDescription());
        ((ImageView) findViewById(R.id.drop_img)).setImageBitmap(stop.getImage());

        findViewById(R.id.drop_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseAccessor db = new DatabaseAccessor();
                db.insertPin(stop, userID);  //null error here
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

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
