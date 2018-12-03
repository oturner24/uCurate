package com.cmsc436.ucurate;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
/*
public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button launchAddPin = findViewById(R.id.button9);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileActivity.this, AddPin.class);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button10);
        launchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(intent3);

            }
        });

        Button launchAddTour = findViewById(R.id.button11);
        launchAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileActivity.this, CreateTour.class);
                startActivity(intent2);

            }
        });

        Button launchHome = findViewById(R.id.button12);
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
                RecyclerView mRecyclerView;
                RecyclerView.Adapter mAdapter;
                RecyclerView.LayoutManager mLayoutManager;
                //tourlist activity?
                //Intent intent1 = new Intent(ProfileActivity.this, TourListActivity.class);
                //startActivity(intent1);
                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ProfileActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                Stop [] mDataSet = {};
                //TODO: database call to put the correct string array parameter in to the TourListAdapter constructor
                StopListAdapter mStopAdapter = new StopListAdapter(ProfileActivity.this, mDataSet);
                mAdapter = new TourListAdapter(myDataset);
                mRecyclerView.setAdapter(mAdapter);

            }
        });

        Button tourList = findViewById(R.id.button8);
        tourList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tourlist activity?
                //Intent intent2 = new Intent(ProfileActivity.this, TourListActivity.class);
                //startActivity(intent2);

                RecyclerView mRecyclerView;
                RecyclerView.Adapter mAdapter;
                RecyclerView.LayoutManager mLayoutManager;
                mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(ProfileActivity.this);
                mRecyclerView.setLayoutManager(mLayoutManager);


                //TODO: database call to put the correct string array parameter in to the TourListAdapter constructor
                mAdapter = new TourListAdapter(_);
                mRecyclerView.setAdapter(mAdapter);
                String [] mDataSet = {"item 0", "item 1", "item 2"};
                //TODO: database call to put the correct string array parameter in to the TourListAdapter constructor
                TourListAdapter mTourAdapter = new TourListAdapter(mDataSet);

            }
        });


    }


}
*/