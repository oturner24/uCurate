package com.cmsc436.ucurate;

import android.app.Activity;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class StopListAdapter extends RecyclerView.Adapter<StopListAdapter.MyViewHolder> {
    private Stop[] mDataset;
    private ArrayList<Marker> selected;
    private ArrayList<Stop> selectedStops;
    private ArrayList<Polyline> lines;
    private GoogleMap mMap;
    private Activity mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CheckedTextView mItemName;
        public ImageButton mInfoButton;
        public int index;
        public Marker mMarker;
        public MyViewHolder(ConstraintLayout v) {
            super(v);
            mItemName = (CheckedTextView) v.getViewById(R.id.stop);
            mInfoButton = (ImageButton) v.getViewById(R.id.info_button);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public StopListAdapter(Activity context, Stop[] myDataset) {
        mDataset = myDataset;
        selected = new ArrayList<>();
        selectedStops = new ArrayList<>();
        lines = new ArrayList<>();
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StopListAdapter.MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        // create a new view
        ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_list_item, parent, false);

        final MyViewHolder vh = new MyViewHolder(v);
        vh.mItemName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                CheckedTextView item = ((CheckedTextView) view);
                item.setChecked(!item.isChecked());
                if (item.isChecked()) {
                    item.setTextColor(Color.RED);

                    Stop stop = mDataset[vh.index];
                    vh.mMarker = mMap.addMarker(new MarkerOptions().position(stop.getCoordinate()).title(stop.getTitle()));
                    selected.add(vh.mMarker);
                    selectedStops.add(mDataset[vh.index]);
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(stop.getCoordinate()));
                } else {
                    item.setTextColor(Color.BLACK);
                    vh.mMarker.remove();
                    selected.remove(vh.mMarker);
                    selectedStops.remove(mDataset[vh.index]);
                }
                //move view to see all selected markers
                if (selected.size() > 0) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    for (Marker marker : selected) {
                        builder.include(marker.getPosition());
                        latLngs.add(marker.getPosition());
                    }
                    LatLngBounds bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                    mMap.animateCamera(cu);

                    //draw lines between markers
                    for (Polyline p : lines) {
                        p.remove();
                    }
                    PolylineOptions line = new PolylineOptions().addAll(latLngs);
                    lines.add(mMap.addPolyline(line));
                }

            }
        });

        vh.mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View popupView = mContext.getLayoutInflater().inflate(R.layout.pin_info_popup, null);
                popupView.setBackgroundColor(Color.WHITE);

                Stop currPin = mDataset[vh.index];
                ((TextView) popupView.findViewById(R.id.pin_name)).setText(currPin.getTitle());
                ((TextView) popupView.findViewById(R.id.pin_des)).setText(currPin.getDescription());
                ((ImageView) popupView.findViewById(R.id.pin_img)).setImageBitmap(currPin.getImage());

                final PopupWindow popup = new PopupWindow(popupView, WRAP_CONTENT, WRAP_CONTENT, false);
                popup.showAtLocation(mContext.findViewById(R.id.main_layout), Gravity.CENTER,0,0);

                popupView.findViewById(R.id.pin_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popup.dismiss();
                    }
                });
            }
        });
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mItemName.setText(mDataset[position].getTitle());
        holder.index = position;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }

    public ArrayList getSelected() {
        return selectedStops;
    }
}