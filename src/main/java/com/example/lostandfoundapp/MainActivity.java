package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnCreate, btnShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCreate = findViewById(R.id.btnCreate);

        btnShow = findViewById(R.id.btnShow);

        Button btnMap =
                findViewById(R.id.btnMap);

        btnMap.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            MainActivity.this,
                            MapsActivity.class
                    );

            startActivity(intent);
        });

        btnCreate.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    CreateAdvertActivity.class
            );

            startActivity(intent);
        });

        btnShow.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    ItemListActivity.class
            );

            startActivity(intent);
        });
    }
}