package com.cmsc436.ucurate;

import android.content.Intent;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ProfileActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private Stop[] myPins;
    private static final String STOPS = "STOPS";
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userID = getIntent().getStringExtra("userID");
        //Bundle bundle = getIntent().getExtras();
        //myPins = bundle.getParcelable("myPins");
        Parcelable[] parcels = getIntent().getParcelableArrayExtra("myPins");
        ArrayList<Stop> tempStops = new ArrayList<>();

        for (Parcelable p : parcels){
            tempStops.add((Stop) p);
        }

        myPins = tempStops.toArray(new Stop[0]);

        Button launchAddPin = findViewById(R.id.button10);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileActivity.this, AddPin.class);
                intent2.putExtra("userID",userID);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button12);
        launchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot assocPins = dataSnapshot.child("users").child(userID).child("associatedPins");
                        ArrayList<Stop> stops = new ArrayList<Stop>();
                        for (DataSnapshot child : assocPins.getChildren()) {
                            Stop temp = new Stop();
                            String pinID = (String) child.getValue();
                            DataSnapshot pinSnapshot = dataSnapshot.child("pins").child(pinID);
                            DatabaseAccessor.createPinFromSnapshot(temp, pinSnapshot);
                            stops.add(temp);
                        }
                        Intent intent3 = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent3.putExtra("userID", userID);
                        intent3.putExtra("myPins",stops.toArray(new Stop[0]));
                        startActivity(intent3);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //not implemented
                    }
                });
            }
        });

        Button launchAddTour = findViewById(R.id.button9);
        launchAddTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        Intent intent4 = new Intent(ProfileActivity.this, CreateTour.class);
                        intent4.putExtra("userID", userID);
                        intent4.putExtra("stops", stops);
                        startActivity(intent4);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //not implemented
                    }
                });

            }
        });

        Button launchHome = findViewById(R.id.button11);
        launchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(ProfileActivity.this, MainActivity.class);
                intent5.putExtra("userID",userID);
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


                //First set an empty adapter
                //mAdapter = new StopListAdapter(ProfileActivity.this, new Stop[0]);
                //mRecyclerView.setAdapter(mAdapter);

                if (myPins.length  > 0) {
                    mAdapter = new StopListAdapter(ProfileActivity.this, myPins);
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
                intent4.putExtra("userID",userID);
                startActivity(intent4);
            }
        });
    }


    //Broken, also pretty sure its unnecessary
    public Stop[] getPinNames(Stop[] stops) {
        HashMap<Stop, String> hash = new HashMap<>();

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