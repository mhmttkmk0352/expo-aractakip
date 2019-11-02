package com.airbnb.android.react.maps.open;

import org.osmdroid.util.GeoPoint;

public class Bounds {
    private GeoPoint latitude;
    private GeoPoint longitude;

    public Bounds(GeoPoint latitude, GeoPoint longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
