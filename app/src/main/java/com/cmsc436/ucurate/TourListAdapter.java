package com.cmsc436.ucurate;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/*This is an adapter for TourListActivity. This class includes the onClickListener that starts
* the TourInfoActivity
* */

public class TourListAdapter extends RecyclerView.Adapter<TourListAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private static String TAG = "TLA";
    private static final String TOUR = "TOUR";
    private Tour mTour;
    private HashMap<String, String> mHash;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public MyViewHolder(TextView v) {
            super(v);
            mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TourListAdapter(String[] myDataset, HashMap hash) {
        mDataset = myDataset;
        mHash = hash;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TourListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        // Clicking on tour will get tour object from database and start TourInfoActivity
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TextView textView = view.findViewById(R.id.tour);
                // Display a Toast message indicting the selected item
                //Toast.makeText(mContext, textView.getText(), Toast.LENGTH_SHORT).show();

                DatabaseAccessor db = new DatabaseAccessor();

                //TODO: get tour from database
                String tourTitle = (String) textView.getText();
                String tourID = mHash.get(tourTitle);
                mTour = db.getTourByID(tourID);

                /*mTour = new Tour("Street Art");
                Stop stop1 = new Stop("stop1", new LatLng(38.9810, -76.9386));
                Stop stop2 = new Stop("stop2", new LatLng(38.9911, -76.9375));
                Stop stop3 = new Stop("stop3", new LatLng(38.9879, -76.9442));

                Stop[] stops = new Stop[]{stop1, stop2, stop3};

                mTour.setStops(stops);*/

                Log.d(TAG,"I've been clicked");
                Intent intent = new Intent(mContext, TourInfoActivity.class);

                intent.putExtra(TOUR, mTour);
                mContext.startActivity(intent);
            }
        });
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }


}