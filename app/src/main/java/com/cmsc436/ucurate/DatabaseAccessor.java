package com.cmsc436.ucurate;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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

    public void insertUser(final String userID, final String email){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                /*
                for (DataSnapshot child : dataSnapshot.child("users").getChildren()){
                    if (child.hasChild(userID))
                        return;
                }
                */
                if (!dataSnapshot.child("users").hasChild(userID)) {
                    mDatabase.child("users").child(userID).child("email").setValue(email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void insertPin(Stop stop, String UserID){
        DatabaseReference newPin = mDatabase.child("pins").push();
        newPin.child("title").setValue(stop.getTitle().toString());
        newPin.child("description").setValue(stop.getDescription().toString());
        newPin.child("latitude").setValue(stop.getCoordinate().latitude);
        newPin.child("longitude").setValue(stop.getCoordinate().longitude);
        newPin.child("owner").setValue(UserID);
        newPin.child("image").setValue(stop.getImage());

        mDatabase.child("users").child(UserID).child("associatedPins").push().setValue(newPin.getKey());

        Log.i("database", ("Inserted new pin with ID: " + newPin.getKey()));
        stop.setID(newPin.getKey());
    }

    public void insertTour(Tour tour, String UserID){
        DatabaseReference newTour = mDatabase.child("tours").push();
        newTour.child("title").setValue(tour.getTitle());
        newTour.child("description").setValue(tour.getDescription());
        newTour.child("numStops").setValue(tour.getNumStops());

        for (Stop stop : tour.getStops()){
            newTour.child("tourPins").push().setValue(stop.getID());
            //Add tour to pin
            mDatabase.child("pins").child(stop.getID()).child("associatedTours").push().setValue(newTour.getKey());
        }
        mDatabase.child("users").child(UserID).child("associatedTours").push().setValue(newTour.getKey());
        Log.i("database", ("Inserted new tour with ID: " + newTour.getKey()));
        tour.setID(newTour.getKey());

    }

    static public void createPinFromSnapshot(Stop empty, DataSnapshot pinSnapshot){
        Stop stop = empty;
        stop.setTitle((String) pinSnapshot.child("title").getValue());
        stop.setDescription((String) pinSnapshot.child("description").getValue());
        double lat = ((Number) pinSnapshot.child("latitude").getValue()).doubleValue();
        double lng = ((Number) pinSnapshot.child("longitude").getValue()).doubleValue();

        stop.setCoordinate(new LatLng(lat,lng));
        stop.setImage((Bitmap) pinSnapshot.child("image").getValue());
        stop.setID(pinSnapshot.getKey());
    }


    public Stop getPinByID(final String stopID){
        final Stop stop = new Stop();
        mDatabase.child("pins").child(stopID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                createPinFromSnapshot(stop, dataSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        return stop;
    }

    static public void createTourFromSnapshot(Tour empty, DataSnapshot tourSnapshot, DataSnapshot pinsSnapshot){
        Tour tour = empty;
        tour.setTitle((String) tourSnapshot.child("title").getValue());
        tour.setDescription((String) tourSnapshot.child("description").getValue());
        tour.setNumStops(((Number) tourSnapshot.child("numStops").getValue()).intValue());

        ArrayList<Stop> stops = new ArrayList<Stop>();
        for (DataSnapshot snapshot : pinsSnapshot.getChildren()){
            Stop temp = new Stop();
            createPinFromSnapshot(temp, snapshot);
            stops.add(temp);
        }
        tour.setStops(stops.toArray(new Stop[0]));

        tour.setID(tourSnapshot.getKey());
    }

    public Tour getTourByID(final String tourID){
        final Tour tour = new Tour();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot tourSnapshot = dataSnapshot.child("tours").child(tourID);
                DataSnapshot pinsSnapshot = dataSnapshot.child("pins");
                createTourFromSnapshot(tour,tourSnapshot,pinsSnapshot);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        return tour;
    }

    public Stop[] getPinsByUser(final String UserID){
        final ArrayList<Stop> stops = new ArrayList<Stop>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot assocPins = dataSnapshot.child("users").child(UserID).child("associatedPins");
                for (DataSnapshot child : assocPins.getChildren()) {
                    Stop temp = new Stop();
                    String pinID = (String) child.getValue();
                    DataSnapshot pinSnapshot = dataSnapshot.child("pins").child(pinID);
                    createPinFromSnapshot(temp, pinSnapshot);
                    stops.add(temp);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });

        return stops.toArray(new Stop[0]);
    }

    public Tour[] getToursByUser(final String UserID){
        final ArrayList<Tour> tours = new ArrayList<Tour>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot assocTours = dataSnapshot.child("users").child(UserID).child("associatedTours");
                DataSnapshot pinsSnapshot = dataSnapshot.child("pins");
                for (DataSnapshot child : assocTours.getChildren()) {
                    Tour temp = new Tour();
                    String tourID = (String) child.getValue();
                    DataSnapshot tourSnapshot = dataSnapshot.child("tours").child(tourID);
                    createTourFromSnapshot(temp,tourSnapshot,pinsSnapshot);
                    tours.add(temp);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });

        return tours.toArray(new Tour[tours.size()]);
    }

    public Stop[] getAllPins(){
        final ArrayList<Stop> stops = new ArrayList<Stop>();
        mDatabase.child("pins").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.i("database", ("Snapshot Children:" + String.valueOf(dataSnapshot.getChildrenCount())));
                for (DataSnapshot pinSnapshot : dataSnapshot.getChildren()){
                    Stop temp = new Stop();
                    createPinFromSnapshot(temp, pinSnapshot);
                    stops.add(temp);
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
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot toursSnapshot = dataSnapshot.child("tours");
                DataSnapshot pinsSnapshot = dataSnapshot.child("pins");
                for (DataSnapshot tourSnapshot: toursSnapshot.getChildren()){
                    Tour temp = new Tour();
                    createTourFromSnapshot(temp, tourSnapshot, pinsSnapshot);
                    tours.add(temp);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //not implemented
            }
        });
        return tours.toArray(new Tour[0]);
    }




}
