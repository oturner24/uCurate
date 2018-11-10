package com.cmsc436.ucurate;

import android.os.Parcel;
import android.os.Parcelable;

public class Tour implements Parcelable{
    private String description;
    private int numStops;
    private double distance;
    private Stop[] stops;

    public String getDescription() {
        return description;
    }

    public double getDistance() {
        return distance;
    }

    public int getNumStops() {
        return numStops;
    }

    public Stop[] getStops() {
        return stops;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setNumStops(int numStops) {
        this.numStops = numStops;
    }

    public void setStops(Stop[] stops) {
        this.stops = stops;
    }

    public Tour(Parcel in){
        this.description = in.readString();
        this.numStops = in.readInt();
        this.distance = in.readDouble();
        this.stops = (Stop[]) in.createTypedArray(Stop.CREATOR);
    }
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeString(this.description);
       dest.writeInt(this.numStops);
       dest.writeDouble(this.distance);
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
