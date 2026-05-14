package com.example.lostandfoundapp;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    SearchView searchView;

    DBHelper dbHelper;

    ArrayList<Item> list;

    ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_item_list);

        recyclerView = findViewById(R.id.recyclerView);

        searchView = findViewById(R.id.searchView);

        dbHelper = new DBHelper(this);

        list = dbHelper.getAllItems();

        adapter = new ItemAdapter(this, list);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        adapter.filter(newText);

                        return true;
                    }
                });
    }
}
