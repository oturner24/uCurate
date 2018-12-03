package com.cmsc436.ucurate;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Set;

//setContentView(R.layout.activity_profile_tours);
public class ProfileToursActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset;
    private static final String TOURS = "TOURS";
    private HashMap<String, String> hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button launchAddPin = findViewById(R.id.button23);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileToursActivity.this, AddPin.class);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button22);
        launchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ProfileToursActivity.this, ProfileActivity.class);
                startActivity(intent3);

            }
        });

        Button launchAddTour = findViewById(R.id.button20);
        launchAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileToursActivity.this, CreateTour.class);
                startActivity(intent2);

            }
        });

        Button launchHome = findViewById(R.id.button21);
        launchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(ProfileToursActivity.this, MainActivity.class);
                startActivity(intent5);

            }
        });

        Button tourList = findViewById(R.id.button24);
        tourList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView = (RecyclerView) findViewById(R.id.recycle_tours);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                mLayoutManager = new LinearLayoutManager(ProfileToursActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                // Will need to get tour names from database

                Tour[] tours = (Tour[]) getIntent().getParcelableArrayExtra(TOURS);
                myDataset = getTourNames(tours);

                mAdapter = new TourListAdapter(myDataset, hash);
                mRecyclerView.setAdapter(mAdapter);

            }
        });

        Button pinList = findViewById(R.id.button25);
        pinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ProfileToursActivity.this, ProfileActivity.class);
                startActivity(intent4);
            }
        });
    }



    public String[] getTourNames(Tour[] tours) {

        for (int i = 0; i < tours.length; i++) {
            String tourName = tours[i].getTitle();
            String tourID = tours[i].getID();

            hash.put(tourName, tourID);
        }

        Set<String> keys = hash.keySet();
        String[] tourNames = (String[]) keys.toArray();
        return tourNames;
    }

}

