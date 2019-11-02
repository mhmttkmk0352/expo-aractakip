package com.airbnb.android.react.maps.open;

import android.content.Context;

import com.facebook.react.views.view.ReactViewGroup;

import org.osmdroid.views.MapView;

public abstract class OpenAirMapFeature extends ReactViewGroup {
  public OpenAirMapFeature(Context context) {
    super(context);
  }

  public abstract void addToMap(MapView map);

  public abstract void removeFromMap(MapView map);

  public abstract Object getFeature();
}
