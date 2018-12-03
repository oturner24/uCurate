package com.cmsc436.ucurate;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/*This Activity displays the tour information
*
* Still in progress
*
* */
public class TourInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Tour mTour;
    private static final String TOUR = "TOUR";
    private Stop[] stops;
    private float distance = 0;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_info);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mTour = getIntent().getParcelableExtra(TOUR);
        stops = mTour.getStops();

        String tourTitle = mTour.getTitle();
        String description = mTour.getDescription();
        String num_stopsStr = String.valueOf(stops.length) + " stops";
        String distanceStr = String.format("%.2f miles", mTour.getDistance());




        TextView title = findViewById(R.id.tour_name);
        title.setText(tourTitle);

        TextView desc = findViewById(R.id.description);
        desc.setText(description);

        TextView num = findViewById(R.id.num_stops);
        num.setText(num_stopsStr);

        TextView dist = findViewById(R.id.distance);
        dist.setText(distanceStr);

        //was image_recycler?
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mAdapter = new TourInfoImageAdapter(getApplicationContext(), stops);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(horizontalLayoutManagaer);
        mRecyclerView.setAdapter(mAdapter);

        Button mButton = findViewById(R.id.button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TourInfoActivity.this, TourActivity.class);

                intent.putExtra(TOUR, mTour);
                startActivity(intent);

            }
        });


    }

    public void onMapReady(GoogleMap map) {
        //Add markers for each stop
        PolylineOptions route = new PolylineOptions();

        for(int i = 0; i < stops.length; i++){
            LatLng loc = stops[i].getCoordinate();
            String title = stops[i].getTitle();
            route.add(loc);
            map.addMarker(new MarkerOptions().position(loc).title(title));
            float[] results = new float[1];

            if(i > 0){
                LatLng prevLoc = stops[i-1].getCoordinate();
                Location.distanceBetween(prevLoc.latitude, prevLoc.longitude, loc.latitude, loc.longitude, results);
                distance += results[0];
            }

        }

        route.add(stops[0].getCoordinate());
        Polyline poyline = map.addPolyline(route);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(stops[0].getCoordinate(), 13));
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        map.animateCamera(CameraUpdateFactory.zoomTo(13), 2000, null);
    }
}
