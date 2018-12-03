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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Will need to get tour names from database

        Tour[] tours = (Tour[]) getIntent().getParcelableArrayExtra(TOURS);
        myDataset = getTourNames(tours);

        //myDataset = new String[]{"Street Art", "Abstract Art", "Sculpture", "Testudos", "Multicultural", "Kids Tour", "Hidden Gems", "Student Work"};

        mAdapter = new TourListAdapter(myDataset, hash);
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

        mAdapter = new TourListAdapter(newDataSet, hash);
        mRecyclerView.setAdapter(mAdapter);

    }

    public String[] getTourNames(Tour[] tours){

        for(int i = 0; i < tours.length; i++){
            String tourName = tours[i].getTitle();
            String tourID = tours[i].getID();

            hash.put(tourName, tourID);
        }

        Set<String> keys = hash.keySet();
        String[] tourNames = (String[]) keys.toArray();
        return tourNames;
    }
}
