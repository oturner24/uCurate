package com.cmsc436.ucurate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class AddPin extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imageView;
    private FusedLocationProviderClient mFusedLocationClient;
    private double lat;
    private double lng;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pin);

        imageView = findViewById(R.id.imageView);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            getLastLoc();
        }

        Button takePhoto = (Button) findViewById(R.id.photo);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
            }
        });

        Button drop = findViewById(R.id.drop);
        drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ((EditText) findViewById(R.id.title)).getText().toString();
                Intent dropIntent = new Intent(getApplicationContext(), DropPins.class);
                //TODO probably want to actually store this in Stop Object
                dropIntent.putExtra("title", title);
                dropIntent.putExtra("lat", lat);
                dropIntent.putExtra("lng", lng);
                dropIntent.putExtra("photo", bitmap);
                startActivity(dropIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED && permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION) {
                    getLastLoc();
                }
            }
        }
    }

    private void getLastLoc() {
        mFusedLocationClient.getLastLocation() //disregard this red line - asking for permissions in onCreate
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            ((EditText) findViewById(R.id.address)).setText(lat + ", " + lng);
                        }
                    }
                });
    }
}
