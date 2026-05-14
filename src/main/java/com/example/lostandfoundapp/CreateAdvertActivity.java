package com.example.lostandfoundapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateAdvertActivity extends AppCompatActivity {

    EditText etName, etPhone,
            etDescription,
            etDate,
            etLocation;

    RadioButton rbLost;

    Spinner spinnerCategory;

    Button btnSave,
            btnImage,
            btnCurrentLocation;

    ImageView imageView;

    DBHelper dbHelper;

    Uri imageUri;

    double selectedLat = 0;
    double selectedLng = 0;

    FusedLocationProviderClient fusedLocationProviderClient;

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_advert);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);

        rbLost = findViewById(R.id.rbLost);

        spinnerCategory = findViewById(R.id.spinnerCategory);

        btnSave = findViewById(R.id.btnSave);
        btnImage = findViewById(R.id.btnImage);
        btnCurrentLocation =
                findViewById(R.id.btnCurrentLocation);

        imageView = findViewById(R.id.imageView);

        dbHelper = new DBHelper(this);

        fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(this);

        if (!Places.isInitialized()) {

            Places.initialize(
                    getApplicationContext(),
                    "YOUR_API_KEY"
            );
        }

        String[] categories = {
                "Electronics",
                "Pets",
                "Wallets",
                "Keys",
                "Documents"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        categories
                );

        spinnerCategory.setAdapter(adapter);

        etLocation.setFocusable(false);

        etLocation.setOnClickListener(v -> {

            List<Place.Field> fields = Arrays.asList(
                    Place.Field.NAME,
                    Place.Field.ADDRESS,
                    Place.Field.LAT_LNG
            );

            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.OVERLAY,
                    fields
            ).build(this);

            startActivityForResult(intent, 100);
        });

        btnCurrentLocation.setOnClickListener(v -> {
            getCurrentLocation();
        });

        btnImage.setOnClickListener(v -> {

            Intent gallery = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );

            startActivityForResult(gallery, PICK_IMAGE);
        });

        btnSave.setOnClickListener(v -> {

            String type =
                    rbLost.isChecked() ? "Lost" : "Found";

            String timestamp =
                    new SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                    ).format(new Date());

            String imagePath = "";

            if(imageUri != null) {

                imagePath = imageUri.toString();
            }

            boolean inserted = dbHelper.insertItem(
                    type,
                    etName.getText().toString(),
                    etPhone.getText().toString(),
                    etDescription.getText().toString(),
                    etDate.getText().toString(),
                    etLocation.getText().toString(),
                    selectedLat,
                    selectedLng,
                    spinnerCategory.getSelectedItem().toString(),
                    imagePath,
                    timestamp
            );

            if(inserted) {

                Toast.makeText(this,
                        "Advert Saved",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
            else {

                Toast.makeText(this,
                        "Failed",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
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

                    if (location != null) {

                        selectedLat =
                                location.getLatitude();

                        selectedLng =
                                location.getLongitude();

                        Geocoder geocoder =
                                new Geocoder(
                                        this,
                                        Locale.getDefault()
                                );

                        try {

                            List<Address> addresses =
                                    geocoder.getFromLocation(
                                            selectedLat,
                                            selectedLng,
                                            1
                                    );

                            if(addresses != null &&
                                    addresses.size() > 0) {

                                etLocation.setText(
                                        addresses.get(0)
                                                .getAddressLine(0)
                                );
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        if(requestCode == PICK_IMAGE &&
                resultCode == RESULT_OK &&
                data != null) {

            imageUri = data.getData();

            imageView.setImageURI(imageUri);
        }

        if(requestCode == 100 &&
                resultCode == RESULT_OK &&
                data != null) {

            Place place =
                    Autocomplete.getPlaceFromIntent(data);

            etLocation.setText(place.getAddress());

            selectedLat =
                    place.getLatLng().latitude;

            selectedLng =
                    place.getLatLng().longitude;
        }
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
                grantResults
        );

        if(requestCode == 1 &&
                grantResults.length > 0 &&
                grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {

            getCurrentLocation();
        }
    }
}