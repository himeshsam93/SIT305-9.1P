package com.example.lostandfoundapp;

public class Item {

    int id;
    String type;
    String name;
    String phone;
    String description;
    String date;
    String location;

    double latitude;
    double longitude;

    String category;
    String imagePath;
    String timestamp;

    public Item(int id,
                String type,
                String name,
                String phone,
                String description,
                String date,
                String location,
                double latitude,
                double longitude,
                String category,
                String imagePath,
                String timestamp) {

        this.id = id;
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.imagePath = imagePath;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCategory() {
        return category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getTimestamp() {
        return timestamp;
    }
}