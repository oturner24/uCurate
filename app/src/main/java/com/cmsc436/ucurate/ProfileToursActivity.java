package com.cmsc436.ucurate;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class ProfileToursActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    private String[] myDataset;
    private static final String TOURS = "TOURS";
    private HashMap<String, String> hash;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_tours);

        userID = getIntent().getStringExtra("userID");

        Button launchAddPin = findViewById(R.id.button23);

        if (launchAddPin == null){
            Log.i("database", ("LAUNCH BUTTON IS NULL"));
        }

        launchAddPin.setOnClickListener(new View.OnClickListener() { //null error here
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(ProfileToursActivity.this, AddPin.class);
                intent2.putExtra("userID",userID);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button22);
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
                        Intent intent3 = new Intent(ProfileToursActivity.this, ProfileActivity.class);
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

        Button launchAddTour = findViewById(R.id.button20);
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
                        Intent intent4 = new Intent(ProfileToursActivity.this, CreateTour.class);
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

        Button launchHome = findViewById(R.id.button21);
        launchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(ProfileToursActivity.this, MainActivity.class);
                intent5.putExtra("userID",userID);
                startActivity(intent5);

            }
        });

        mRecyclerView = findViewById(R.id.recycle_tours);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        final ArrayList<Tour> tours = new ArrayList<Tour>();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot assocTours = dataSnapshot.child("users").child(userID).child("associatedTours");
                DataSnapshot pinsSnapshot = dataSnapshot.child("pins");
                for (DataSnapshot child : assocTours.getChildren()) {
                    Tour temp = new Tour();
                    String tourID = (String) child.getValue();
                    DataSnapshot tourSnapshot = dataSnapshot.child("tours").child(tourID);
                    DatabaseAccessor.createTourFromSnapshot(temp,tourSnapshot,pinsSnapshot);
                    tours.add(temp);
                }

                String[] dataSet = new String[tours.size()];
                for (int i = 0; i < tours.size(); i++){
                    dataSet[i] = tours.get(i).getTitle();
                }


                mAdapter = new TourListProfileAdapter(dataSet, userID);
                mRecyclerView.setAdapter(mAdapter);

                if (mAdapter.getItemCount() == 0) {
                    Toast.makeText(getApplicationContext(), "No tours created.", Toast.LENGTH_LONG).show();
                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });


        Button pinList = findViewById(R.id.button24);
        pinList.setOnClickListener(new View.OnClickListener() {
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
                        Intent intent3 = new Intent(ProfileToursActivity.this, ProfileActivity.class);
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

