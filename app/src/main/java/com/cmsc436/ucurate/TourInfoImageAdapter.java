package com.cmsc436.ucurate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ceylonlabs.imageviewpopup.ImagePopup;

public class TourInfoImageAdapter extends RecyclerView.Adapter<TourInfoImageAdapter.MyViewHolder> {

    private Stop[] stops;
    private Context mContext;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView riv;

        public MyViewHolder(View view) {
            super(view);

            riv = (ImageView) view.findViewById(R.id.horizontal_item_view_image);

        }

    }

    public TourInfoImageAdapter(Context context, Stop[] stops) {
        this.mContext = context;
        this.stops = stops;
    }

    @Override
    public TourInfoImageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item, parent, false);

        if (itemView.getLayoutParams().width == RecyclerView.LayoutParams.MATCH_PARENT) {
            itemView.getLayoutParams().width = RecyclerView.LayoutParams.WRAP_CONTENT;
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.riv.setImageBitmap(stops[position].getImage());

    }

    @Override
    public int getItemCount() {
        return stops.length;
    }
}