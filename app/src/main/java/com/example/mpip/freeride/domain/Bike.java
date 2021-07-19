package com.example.mpip.freeride.domain;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.InputStream;

public class Bike {
    private String id;
    private String name;
    private int price;
    private boolean rented;
    private Location location;
    private String renter;
    private String category;
    private Bitmap image;

    public Bike() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getRented() {
        return rented;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }


   /* public boolean isRented() {
        return rented;
    }*/

    public void setRented(boolean rented) {
        this.rented = rented;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Bike(String id, String name, int price, Bitmap image, boolean rented, Location location, String renter, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.rented = rented;
        this.location = location;
        this.renter = renter;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRenter() {
        return renter;
    }

    public void setRenter(String renter) {
        this.renter = renter;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
