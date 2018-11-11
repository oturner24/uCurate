package com.cmsc436.ucurate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/*This Activity displays the tour information
*
* Still in progress
*
* */
public class TourInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Tour mTour;
    private static final String TOUR = "TOUR";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_info);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mTour = getIntent().getParcelableExtra(TOUR);

        String tourTitle = mTour.getTitle();

        TextView title = findViewById(R.id.tour_name);
        title.setText(tourTitle);

    }

    public void onMapReady(GoogleMap map) {
        //Add markers for each stop
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //create polygon from markers
    }
}
