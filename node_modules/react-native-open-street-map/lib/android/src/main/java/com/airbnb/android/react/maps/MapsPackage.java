package com.airbnb.android.react.maps;

import android.app.Activity;

import com.airbnb.android.react.maps.open.OpenAirMapManager;
import com.airbnb.android.react.maps.open.OpenAirMapModule;
import com.airbnb.android.react.maps.open.collout.OpenAirMapCalloutManager;
import com.airbnb.android.react.maps.open.marker.OpenAirMapMarkerManager;
import com.airbnb.android.react.maps.open.polyline.OpenAirMapPolylineManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapsPackage implements ReactPackage {
  public MapsPackage(Activity activity) {
  } // backwards compatibility

  public MapsPackage() {
  }

  @Override
  public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
    return Arrays.<NativeModule>asList(new OpenAirMapModule(reactContext));
  }

  // Deprecated RN 0.47
  public List<Class<? extends JavaScriptModule>> createJSModules() {
    return Collections.emptyList();
  }

  @Override
  public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
    OpenAirMapCalloutManager calloutManager = new OpenAirMapCalloutManager();
    OpenAirMapMarkerManager annotationManager = new OpenAirMapMarkerManager();
    OpenAirMapPolylineManager polylineManager = new OpenAirMapPolylineManager(reactContext);
    OpenAirMapManager mapManager = new OpenAirMapManager(reactContext);

    return Arrays.<ViewManager>asList(
        calloutManager,
        annotationManager,
        polylineManager,
        mapManager);
  }
}
