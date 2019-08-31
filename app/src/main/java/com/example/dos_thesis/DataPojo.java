package com.example.dos_thesis;

public class DataPojo {
    private String datetime, location, magnitude;

    public DataPojo() {

    }

    public DataPojo(String datetime, String location, String magnitude) {
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
