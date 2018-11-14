package com.cmsc436.ucurate;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

/*
This Activity will display a list of available tours.

Will need to be modified once database is implemented.


*/

public class TourListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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

        // specify an adapter (see also next example)
        // Will need to get tour names from database
        String[] myDataset = {"Street Art", "Abstract Art", "Sculpture", "Testudos", "Multicultural", "Kids Tour", "Hidden Gems", "Student Work"};
        mAdapter = new TourListAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

    }
}
