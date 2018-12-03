package com.cmsc436.ucurate;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Stop[] myDataset;
    private static final String STOPS = "STOPS";
    private HashMap<Stop, String> hash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button launchAddPin = findViewById(R.id.button10);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileActivity.this, AddPin.class);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button12);
        launchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent3);

            }
        });

        Button launchAddTour = findViewById(R.id.button9);
        launchAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileActivity.this, CreateTour.class);
                startActivity(intent2);

            }
        });

        Button launchHome = findViewById(R.id.button11);
        launchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent5);

            }
        });

        Button pinList = findViewById(R.id.button7);
        pinList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView = (RecyclerView) findViewById(R.id.recycle_pins);

                // use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                //mLayoutManager = new LinearLayoutManager(ProfileActivity.this);
                //mRecyclerView.setLayoutManager(mLayoutManager);

                // Will need to get pin titles from database, unclear if this is done correctly
                //TODO: see comment and below code?

                Stop[] stops = (Stop[]) getIntent().getParcelableArrayExtra(STOPS);
                if (stops != null) {
                    myDataset = getPinNames(stops);

                    mAdapter = new StopListAdapter(ProfileActivity.this, myDataset);
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(getApplicationContext(), "No pins dropped.", Toast.LENGTH_LONG).show();
                }

            }
        });

        Button tourList = findViewById(R.id.button8);
        tourList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(ProfileActivity.this, ProfileToursActivity.class);
                startActivity(intent4);
            }
        });
    }



    public Stop[] getPinNames(Stop[] stops) {

            for (int i = 0; i < stops.length; i++) {
                String stopName = stops[i].getTitle();
                //String stopID = stops[i].getID();
                Stop curr = stops[i];

                hash.put(curr, stopName);
            }

            Set<Stop> keys = hash.keySet();
            Stop[] stopNames = (Stop[]) keys.toArray();
            return stopNames;
    }

    }