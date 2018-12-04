package com.cmsc436.ucurate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/*This is an adapter for TourListActivity. This class includes the onClickListener that starts
 * the TourInfoActivity
 * */

public class TourListProfileAdapter extends RecyclerView.Adapter<TourListProfileAdapter.MyViewHolder> {
    private String[] mDataset;
    private Context mContext;
    private static String TAG = "TLA";
    private static final String TOUR = "TOUR";
    private Tour mTour;
    private String userID;
    private String tourTitle;
    //private Tour[] tours;

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
    public TourListProfileAdapter(String[] myDataset, String userID) {
        mDataset = myDataset;
        this.userID = userID;
    }

    public TourListProfileAdapter(String[] myDataset, Tour[] tours, String userID) {
        mDataset = myDataset;
        //mHash = hash;
        //this.tours = tours;
        this.userID = userID;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TourListProfileAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mContext = parent.getContext();
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);

        // Clicking on tour will get tour object from database and start TourInfoActivity
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                TextView textView = view.findViewById(R.id.tour);
                tourTitle = (String) textView.getText();

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
                        for(Tour tour : tours){
                            if(tour.getTitle().equals(tourTitle)){
                                mTour = tour;
                            }
                        }

                        Log.d(TAG,"I've been clicked");
                        Intent intent = new Intent(mContext, TourInfoActivity.class);
                        intent.putExtra("userID",userID);
                        intent.putExtra(TOUR, mTour);
                        mContext.startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //not implemented
                    }
                });

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

    public Bitmap scaleDownBitmap(Bitmap photo) {
        float densityMultiplier = mContext.getResources().getDisplayMetrics().density;

        int h = (int) (50 * densityMultiplier);
        int w = (int) (h * photo.getWidth()/((double) photo.getHeight()));

        photo = Bitmap.createScaledBitmap(photo, w, h, true);

        return photo;
    }

}