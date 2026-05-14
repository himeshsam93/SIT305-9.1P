package com.example.lostandfoundapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailActivity extends AppCompatActivity {

    ImageView detailImage;

    TextView txtDetails;

    Button btnRemove;

    DBHelper dbHelper;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_detail);

        detailImage = findViewById(R.id.detailImage);

        txtDetails = findViewById(R.id.txtDetails);

        btnRemove = findViewById(R.id.btnRemove);

        dbHelper = new DBHelper(this);

        id = getIntent().getIntExtra("id", -1);

        Item item = dbHelper.getSingleItem(id);

        if(item != null) {

            if(item.getImagePath() != null &&
                    !item.getImagePath().isEmpty()) {

                detailImage.setImageURI(
                        Uri.parse(item.getImagePath())
                );
            }

            String details =
                    "Type: " + item.getType() + "\\n\\n" +
                            "Name: " + item.getName() + "\\n\\n" +
                            "Phone: " + item.getPhone() + "\\n\\n" +
                            "Description: " + item.getDescription() + "\\n\\n" +
                            "Date: " + item.getDate() + "\\n\\n" +
                            "Location: " + item.getLocation() + "\\n\\n" +
                            "Category: " + item.getCategory() + "\\n\\n" +
                            "Posted: " + item.getTimestamp();

            txtDetails.setText(details);
        }

        btnRemove.setOnClickListener(v -> {

            dbHelper.deleteItem(id);

            Toast.makeText(this,
                    "Item Removed",
                    Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}
