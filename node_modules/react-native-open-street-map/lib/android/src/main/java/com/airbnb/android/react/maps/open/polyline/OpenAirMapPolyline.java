package com.airbnb.android.react.maps.open.polyline;

import android.content.Context;

import com.airbnb.android.react.maps.open.OpenAirMapFeature;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class OpenAirMapPolyline extends OpenAirMapFeature {
    private Polyline polyline;
    private MapView map;
    private List<GeoPoint> coordinates = new ArrayList<>();
    private int color;
    private float width;
    private boolean geodesic;
    private float zIndex;

    public OpenAirMapPolyline(Context context) {
        super(context);
    }

    @Override
    public void addToMap(MapView map) {
        this.map = map;
    }

    @Override
    public void removeFromMap(MapView map) {

    }

    public void setCoordinates(ReadableArray coordinates) {
        this.coordinates = new ArrayList<>(coordinates.size());
        for (int i = 0; i < coordinates.size(); i++) {
            ReadableMap coordinate = coordinates.getMap(i);
            this.coordinates.add(i,
                    new GeoPoint(coordinate.getDouble("latitude"), coordinate.getDouble("longitude")));
        }
        if (polyline != null) {
            polyline.setPoints(this.coordinates);
        }
    }

    public void setColor(int color) {
        this.color = color;
        if (polyline != null) {
            polyline.setColor(color);
        }
    }

    public void setWidth(float width) {
        this.width = width;
        if (polyline != null) {
            polyline.setWidth(width);
        }
    }

    public void setGeodesic(boolean geodesic) {
        this.geodesic = geodesic;
        if (polyline != null) {
            polyline.setGeodesic(geodesic);
        }
    }

    @Override
    public Object getFeature() {
        return polyline;
    }

}
