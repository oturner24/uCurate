package com.cmsc436.ucurate;

import com.google.android.gms.maps.model.LatLng;

public class Stop {

    private String title;
    private LatLng coordinate;

    //need data type for an image

    Stop(String title, LatLng coordinate){
        this.title = title;
        this.coordinate = coordinate;
    }

    public String getTitle(Stop stop){
        return title;
    }

    public LatLng getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(LatLng coordinate) {
        this.coordinate = coordinate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
