package com.cmsc436.ucurate;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TourListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button mButton;
    private String[] myDataset;
    private static final String TOURS = "TOURS";
    private HashMap<String, String> hash;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        Button launchAddPin = findViewById(R.id.button14);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(TourListActivity.this, AddPin.class);
                intent2.putExtra("userID",userID);
                startActivity(intent2);

            }
        });

        Button launchProfile = findViewById(R.id.button16);
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
                        Intent intent3 = new Intent(TourListActivity.this, ProfileActivity.class);
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

        Button launchAddTour = findViewById(R.id.button17);
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

                        Intent intent4 = new Intent(TourListActivity.this, CreateTour.class);
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

        Button launchHome = findViewById(R.id.button15);
        launchHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(TourListActivity.this, MainActivity.class);
                intent5.putExtra("userID",userID);
                startActivity(intent5);

            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Will need to get tour names from database

        userID = getIntent().getStringExtra("userID");
        Parcelable[] tours = getIntent().getParcelableArrayExtra(TOURS);
        myDataset = getTourNames(tours);


        //myDataset = new String[]{"Street Art", "Abstract Art", "Sculpture", "Testudos", "Multicultural", "Kids Tour", "Hidden Gems", "Student Work"};

        mAdapter = new TourListAdapter(myDataset, hash, userID);
        mRecyclerView.setAdapter(mAdapter);

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }


        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search);
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);


    }

    private void doMySearch(String query) {
        //do the search

        String[] newDataSet = new String[myDataset.length];
        int j = 0;

        for(int i = 0; i < newDataSet.length; i++){
            if(myDataset[i].toLowerCase().contains(query.toLowerCase())){
                newDataSet[j] = myDataset[i];
                j++;
            }
        }

        mAdapter = new TourListAdapter(newDataSet, hash, userID);
        mRecyclerView.setAdapter(mAdapter);

    }

    public String[] getTourNames(Parcelable[] tours){
        hash = new HashMap<>();
        for(int i = 0; i < tours.length; i++){
            Tour tour = (Tour) tours[i];
            String tourName = tour.getTitle();
            String tourID = tour.getID();

            hash.put(tourName, tourID);
        }

        Set<String> keys = hash.keySet();
        String[] tourNames = (String[]) keys.toArray(new String[keys.size()]);
        return tourNames;
    }
}
