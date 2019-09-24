package com.example.dos_thesis;

public class ListData {
    private String datetime, location, mag;

    public ListData() {

    }

    public ListData(String datetime, String location, String mag) {
        this.datetime = datetime;
        this.location = location;
        this.mag = mag;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getLocation() {
        return location;
    }

    public String getMag() {
        return mag;
    }
}
