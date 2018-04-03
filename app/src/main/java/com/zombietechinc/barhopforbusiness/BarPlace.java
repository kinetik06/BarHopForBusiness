package com.zombietechinc.barhopforbusiness;

import android.net.Uri;

import java.net.URI;

public class BarPlace {

    private String userId;
    private String placeId;
    private String name;
    private String address;
    private Uri webURI;
    private int priceLevel;
    private float rating;

    public BarPlace(){};

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Uri getWebURI() {
        return webURI;
    }

    public void setWebURI(Uri webURI) {
        this.webURI = webURI;
    }

    public int getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(int priceLevel) {
        this.priceLevel = priceLevel;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public BarPlace(String userId, String placeId, String name, String address, int priceLevel, float rating){

        this.userId = userId;
        this.placeId = placeId;
        this.name = name;
        this.address = address;
        this.priceLevel = priceLevel;
        this.rating = rating;


    }

}
