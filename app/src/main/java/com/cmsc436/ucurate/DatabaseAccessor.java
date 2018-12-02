package com.cmsc436.ucurate;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DatabaseAccessor {
    private static DatabaseReference mDatabase;

    public DatabaseAccessor(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //Log.i("database", "This is the database key:" + mDatabase.getKey());
    }

    public void insertUser(String username){
        mDatabase.child("users").push().child("username").setValue(username);
    }

    public void insertPin(Stop stop, String UserID){
        DatabaseReference newPin = mDatabase.child("pins").push();
        newPin.child("title").setValue(stop.getTitle());
        newPin.child("description").setValue(stop.getDescription());
        newPin.child("latitude").setValue(stop.getCoordinate().latitude);
        newPin.child("longitude").setValue(stop.getCoordinate().longitude);
        newPin.child("owner").setValue(UserID);
        newPin.child("image").setValue(stop.getImage());

        mDatabase.child("users").child(UserID).child("associatedPins").push().setValue(newPin.getKey());

        stop.setID(newPin.getKey());
    }

    public void insertTour(Tour tour, String UserID){
        DatabaseReference newTour = mDatabase.child("tours").push();
        newTour.child("title").setValue(tour.getTitle());
        newTour.child("description").setValue(tour.getDescription());
        newTour.child("numStops").setValue(tour.getNumStops());
        newTour.child("distance").setValue(tour.getDistance());

        for (Stop stop : tour.getStops()){
            newTour.child("tourPins").push().setValue(stop.getID());
            //Add tour to pin
            mDatabase.child("pins").child(stop.getID()).child("associatedTours").push().setValue(newTour.getKey());
        }

        tour.setID(newTour.getKey());
    }


    public Stop getPinByID(final String stopID){
        final Stop stop = new Stop();
        mDatabase.child("pins").child(stopID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                stop.setID(stopID);
                stop.setTitle((String) dataSnapshot.child("title").getValue());
                stop.setDescription((String) dataSnapshot.child("description").getValue());
                long lat = (long) dataSnapshot.child("latitude").getValue();
                long lng = (long) dataSnapshot.child("longitude").getValue();
                stop.setCoordinate(new LatLng(lat,lng));
                stop.setImage((Bitmap) dataSnapshot.child("image").getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        return stop;
    }

    public Tour getTourByID(final String tourID){
        final Tour tour = new Tour();
        mDatabase.child("tours").child(tourID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tour.setTitle((String) dataSnapshot.child("title").getValue());
                tour.setDescription((String) dataSnapshot.child("description").getValue());
                tour.setNumStops((int) dataSnapshot.child("numStops").getValue());
                tour.setDistance((double) dataSnapshot.child("distance").getValue());

                ArrayList<Stop> stops = new ArrayList<Stop>();
                for (DataSnapshot snapshot : dataSnapshot.child("tourPins").getChildren()){
                    stops.add(getPinByID(snapshot.getKey()));
                }
                tour.setStops((Stop[])stops.toArray());

                tour.setID(tourID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        return tour;
    }

    public Stop[] getPinsByUser(String UserID){
        final ArrayList<Stop> stops = new ArrayList<Stop>();
        DatabaseReference ref = mDatabase.child("users").child(UserID).child("associatedPins");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    stops.add(getPinByID((String) child.getValue()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });

        return (Stop[]) stops.toArray(new Stop[0]);
    }

    public Tour[] getToursByUser(String UserID){
        final ArrayList<Tour> tours = new ArrayList<Tour>();
        DatabaseReference ref = mDatabase.child("users").child(UserID).child("associatedTours");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    tours.add(getTourByID((String) child.getValue()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });

        return (Tour[]) tours.toArray(new Tour[0]);
    }

    public Stop[] getAllPins(){
        final ArrayList<Stop> stops = new ArrayList<Stop>();
        mDatabase.child("pins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("database", ("Snapshot Children:" + String.valueOf(dataSnapshot.getChildrenCount())));
                for (DataSnapshot ref : dataSnapshot.getChildren()){
                    stops.add(getPinByID(ref.getKey()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        //Log.i("database", ("Array Length:" + String.valueOf(stops.size())));
        return stops.toArray(new Stop[stops.size()]);
    }

    public Tour[] getAllTours(){
        final ArrayList<Tour> tours = new ArrayList<Tour>();
        mDatabase.child("tours").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ref : dataSnapshot.getChildren()){
                    tours.add(getTourByID(ref.getKey()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        return (Tour[]) tours.toArray(new Tour[0]);
    }




}
