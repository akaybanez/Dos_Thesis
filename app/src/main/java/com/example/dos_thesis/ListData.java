package com.example.dos_thesis;

public class ListData {
    private String datetime;
    private String location;
    private String magnitude;

    public ListData() {

    }

    public ListData(String datetime, String location, String magnitude) {
        this.datetime = datetime;
        this.location = location;
        this.magnitude = magnitude;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(String magnitude) {
        this.magnitude = magnitude;
    }
}
