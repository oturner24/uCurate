package com.cmsc436.ucurate;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Stop implements Parcelable {

    private String title;
    private String description;
    private LatLng coordinate;
    private Bitmap image;

    public Stop(String title, LatLng coordinate){
        this.title = title;
        this.coordinate = coordinate;
    }

    public Stop(String title, LatLng coordinate, Bitmap image) {
        this.title = title;
        this.coordinate = coordinate;
        this.image = image;
    }

    public Stop() {}

    public String getTitle(){
        return title;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }



    ////// Code required for parcelable interface //////

    // "De-parcel object
    public Stop(Parcel in) {
        this.title = in.readString();
        this.description = in.readString();
        Bundle b = in.readBundle(getClass().getClassLoader());
        this.image = b.getParcelable("image");
        this.coordinate = b.getParcelable("loc");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.description);
        Bundle b = new Bundle();
        b.putParcelable("image", this.image); //TODO check this works correctly
        b.putParcelable("loc", this.coordinate);
        dest.writeBundle(b);
    }

    // Creator
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Stop createFromParcel(Parcel in) {
            return new Stop(in);
        }

        public Stop[] newArray(int size) {
            return new Stop[size];
        }
    };
}
