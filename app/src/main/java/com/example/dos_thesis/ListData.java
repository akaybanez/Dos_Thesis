package com.example.dos_thesis;

public class ListData {
    private String datetime, latitude, location, longitude, magnitude;

    public ListData () {

    }

    public ListData(String datetime, String latitude, String location, String longitude, String magnitude) {
        this.datetime = datetime;
        this.latitude = latitude;
        this.location = location;
        this.longitude = longitude;
        this.magnitude = magnitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLocation() {
        return location;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMagnitude() {
        return magnitude;
    }
}
