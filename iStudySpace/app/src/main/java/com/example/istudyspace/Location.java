package com.example.istudyspace;

import com.google.android.gms.maps.model.LatLng;

public class Location {
    private String name;
    private double lat;
    private double lon;
    private boolean coffee;
    private boolean groupWork;
    private boolean food;
    private String noiseLevel;
    private String zoom;
    private String imageFile;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public LatLng getCoords() {
        return new LatLng(lat, lon);
    }

    public void setGroupWork() { this.groupWork = groupWork; }

    public boolean getGroupWork() { return groupWork; }

    public void setCoffee() { this.coffee = coffee; }

    public boolean getCoffee() { return coffee; }

    public void setFood() { this.food = food; }

    public boolean getFood() { return food; }

    public void setNoiseLevel() { this.noiseLevel = noiseLevel; }

    public String getNoiseLevel() { return noiseLevel; }

    public void setZoom() { this.zoom = zoom; }

    public String getZoom() { return zoom; }

    public void setImageFile() { this.imageFile = imageFile; }

    public String getImageFile() {return imageFile; }
}
