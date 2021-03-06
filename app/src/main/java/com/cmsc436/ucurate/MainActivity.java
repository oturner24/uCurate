package com.cmsc436.ucurate;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.app.Fragment;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends FragmentActivity  {
    //private GoogleMap mMap;
    //private FusedLocationProviderClient mFusedLocationClient;
    //implements OnMapReadyCallback


    private static final int RC_SIGN_IN = 123;
    private static final int LOCATION_PERM = 1;

    private String userID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
          //      .findFragmentById(R.id.mapView);
        //mapFragment.getMapAsync(this);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            createSignInIntent();
        } else {
            //Uncomment if you want to force login every single time
            //createSignInIntent();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            userID = user.getUid();
        }

        ImageView mImage = findViewById(R.id.imageView2);
        mImage.setImageResource(R.drawable.fasika_08);

        Button launchTourList = findViewById(R.id.button3);
        launchTourList.setOnClickListener(new OnClickListener() {
             @Override
                public void onClick(View v) {
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    final ArrayList<Tour> tours = new ArrayList<Tour>();
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(DataSnapshot dataSnapshot) {
                             DataSnapshot toursSnapshot = dataSnapshot.child("tours");
                             DataSnapshot pinsSnapshot = dataSnapshot.child("pins");
                             for (DataSnapshot tourSnapshot: toursSnapshot.getChildren()){
                                 Tour temp = new Tour();
                                 DatabaseAccessor.createTourFromSnapshot(temp, tourSnapshot, pinsSnapshot);
                                 tours.add(temp);
                             }

                             Intent intent1 = new Intent(MainActivity.this, TourListActivity.class);
                             intent1.putExtra("userID", userID);
                             intent1.putExtra("TOURS", tours.toArray(new Tour[0]));
                             startActivity(intent1);

                         }
                         @Override
                         public void onCancelled(DatabaseError databaseError) {
                             //not implemented
                         }
                     });

                }
        });

        Button launchProfile = findViewById(R.id.button2);
        launchProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot assocPins = dataSnapshot.child("users").child(userID).child("associatedPins");
                        ArrayList<Stop> stops = new ArrayList<Stop>();
                        for (DataSnapshot child : assocPins.getChildren()) {
                            Stop temp = new Stop();
                            String pinID = (String) child.getValue();
                            DataSnapshot pinSnapshot = dataSnapshot.child("pins").child(pinID);
                            DatabaseAccessor.createPinFromSnapshot(temp, pinSnapshot);
                            stops.add(temp);
                        }
                        Intent intent3 = new Intent(MainActivity.this, ProfileActivity.class);
                        //Bundle bundle = new Bundle();
                        //bundle.putParcelableArray("myPins", stops.toArray(new Stop[0]));
                        //intent3.putExtras(bundle);
                        intent3.putExtra("userID", userID);
                        intent3.putExtra("myPins",stops.toArray(new Stop[0]));
                        startActivity(intent3);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //not implemented
                    }
                });



            }
        });

        Button launchAddTour = findViewById(R.id.button4);
        launchAddTour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("pins").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<Stop> stops = new ArrayList<Stop>();
                        for (DataSnapshot pinSnapshot : dataSnapshot.getChildren()){
                            Stop temp = new Stop();
                            DatabaseAccessor.createPinFromSnapshot(temp, pinSnapshot);
                            stops.add(temp);
                        }

                        Intent intent4 = new Intent(MainActivity.this, CreateTour.class);
                        intent4.putExtra("userID", userID);
                        intent4.putExtra("stops", stops);
                        startActivity(intent4);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //not implemented
                    }
                });


            }
        });
        /*
        Button launchHome = findViewById(R.id.button5);
        launchHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent5 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent5);

            }
        });*/

        Button launchAddPin = findViewById(R.id.button6);
        launchAddPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, AddPin.class);
                intent2.putExtra("userID",userID);
                startActivity(intent2);
            }
        });


    }


    public void createSignInIntent() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    // [START auth_fui_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                userID = user.getUid();
                DatabaseAccessor db = new DatabaseAccessor();
                db.insertUser(userID, user.getEmail());
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    // [END auth_fui_result]

    public void signOut() {
        // [START auth_fui_signout]
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_signout]
    }

    public void delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
                .delete(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        // [END auth_fui_delete]
    }

    public void privacyAndTerms() {
        List<AuthUI.IdpConfig> providers = Collections.emptyList();
        // [START auth_fui_pp_tos]
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTosAndPrivacyPolicyUrls(
                                "https://example.com/terms.html",
                                "https://example.com/privacy.html")
                        .build(),
                RC_SIGN_IN);
        // [END auth_fui_pp_tos]
    }

    public void dbTest(){
        DatabaseAccessor db = new DatabaseAccessor();

        /*
        Stop test1 = new Stop("Pin", new LatLng(38.9869,-76.9426));
        test1.setDescription("This is the description.");
        Stop test2 = new Stop("Pin 2", new LatLng(38, -76.8));
        Stop test3 = new Stop("Pin 3", new LatLng(38.2, -76.3));

        db.insertPin(test1,"12345");
        db.insertPin(test2,"12345");
        db.insertPin(test3,"12345");

        Tour tour = new Tour();
        tour.setTitle("Test Pin2");
        tour.setDescription("This is a test pin");
        tour.setStops(new Stop[]{test1});

        db.insertTour(tour, "12345");

        */

        Stop[] stops = db.getAllPins();

        Log.i("database", String.valueOf(stops.length));
        for (Stop s : stops){
            Log.i("database", s.getTitle());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /*@Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //ask for location permissions if necessary
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERM);
        } else {
            moveToLastLoc();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == LOCATION_PERM) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED && permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    moveToLastLoc();
                }
            }
        }
    }

    //sets the stop's coordinates to the current location
    private void moveToLastLoc() {
        mFusedLocationClient.getLastLocation() //disregard this red line - asking for permissions earlier
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng mCoords = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoords, 50));
                            // Zoom in, animating the camera.
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());
                            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                        }
                    }
                });
    }*/
}
