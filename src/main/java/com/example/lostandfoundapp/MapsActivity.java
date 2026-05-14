package com.example.lostandfoundapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback {

    GoogleMap mMap;

    EditText etRadius;

    Button btnSearch;

    DBHelper dbHelper;

    double userLat = -37.8136;
    double userLng = 144.9631;

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        etRadius = findViewById(R.id.etRadius);

        btnSearch = findViewById(R.id.btnSearch);

        dbHelper = new DBHelper(this);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment =
                (SupportMapFragment)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.map);

        if(mapFragment != null) {

            mapFragment.getMapAsync(this);
        }

        btnSearch.setOnClickListener(v -> {

            loadMarkers();
        });

        getCurrentLocation();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        LatLng melbourne =
                new LatLng(userLat, userLng);

        mMap.moveCamera(
                CameraUpdateFactory
                        .newLatLngZoom(melbourne, 10)
        );

        loadMarkers();
    }

    private void getCurrentLocation() {

        if(ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1
            );

            return;
        }

        fusedLocationProviderClient
                .getLastLocation()
                .addOnSuccessListener(location -> {

                    if(location != null) {

                        userLat = location.getLatitude();

                        userLng = location.getLongitude();

                        loadMarkers();
                    }
                });
    }

    private void loadMarkers() {

        if(mMap == null) return;

        mMap.clear();

        ArrayList<Item> items =
                dbHelper.getAllItems();

        double radius = 999999;

        if(!etRadius.getText()
                .toString()
                .isEmpty()) {

            radius = Double.parseDouble(
                    etRadius.getText()
                            .toString()
            );
        }

        for(Item item : items) {

            if(item.getLatitude() == 0 &&
                    item.getLongitude() == 0) {

                continue;
            }

            float[] results = new float[1];

            Location.distanceBetween(
                    userLat,
                    userLng,
                    item.getLatitude(),
                    item.getLongitude(),
                    results
            );

            float distanceKm =
                    results[0] / 1000;

            if(distanceKm <= radius) {

                LatLng latLng =
                        new LatLng(
                                item.getLatitude(),
                                item.getLongitude()
                        );

                mMap.addMarker(
                        new MarkerOptions()
                                .position(latLng)
                                .title(item.getName())
                                .snippet(item.getDescription())
                );
            }
        }

        LatLng current =
                new LatLng(userLat, userLng);

        mMap.moveCamera(
                CameraUpdateFactory
                        .newLatLngZoom(current, 10)
        );
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults);

        if(requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }
        else {

            Toast.makeText(
                    this,
                    "Location Permission Denied",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }
}