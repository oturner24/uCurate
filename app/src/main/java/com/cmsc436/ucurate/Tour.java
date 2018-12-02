package com.cmsc436.ucurate;

import android.os.Parcel;
import android.os.Parcelable;

public class Tour implements Parcelable{
    private String title;
    private String description;
    private int numStops;
    private Stop[] stops;
    private double distance;

    private String ID;

    public Tour(String title){
        this.title = title;
    }

    public Tour(){}

    public String getDescription() {
        return description;
    }

    public double getDistance() { return this.distance; }

    public int getNumStops() {
        return numStops;
    }

    public Stop[] getStops() {
        return stops;
    }

    public String getTitle() { return title; }

    public String getID() { return ID;}

    public void setDistance(double distance) {this.distance = distance; }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNumStops(int numStops) {
        this.numStops = numStops;
    }

    public void setTitle(String title) { this.title = title; }

    public void setStops(Stop[] stops) {
        this.stops = stops;
    }

    public void setID(String ID){this.ID = ID;}


    public Tour(Parcel in){
        this.title = in.readString();
        this.description = in.readString();
        this.numStops = in.readInt();
        this.stops = (Stop[]) in.createTypedArray(Stop.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
       dest.writeString(this.description);
       dest.writeInt(this.numStops);
       dest.writeTypedArray(this.stops,0);

    }

    public static final Parcelable.Creator<Tour> CREATOR =
            new Parcelable.Creator<Tour>() {

                public Tour createFromParcel(Parcel in) {
                    return new Tour(in);
                }

                public Tour[] newArray(int size) {
                    return new Tour[size];
                }
            };

}
