package com.cmsc436.ucurate;

public class Tour {
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

}
