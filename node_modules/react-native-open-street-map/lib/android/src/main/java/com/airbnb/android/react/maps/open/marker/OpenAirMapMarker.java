package com.airbnb.android.react.maps.open.marker;

import android.content.Context;

import com.airbnb.android.react.maps.open.OpenAirMapFeature;

import org.osmdroid.views.MapView;

class OpenAirMapMarker extends OpenAirMapFeature {

    public OpenAirMapMarker(Context context) {
        super(context);
    }

    @Override
    public void addToMap(MapView map) {

    }

    @Override
    public void removeFromMap(MapView map) {

    }

    @Override
    public Object getFeature() {
        return null;
    }
}
