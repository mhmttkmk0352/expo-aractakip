package com.airbnb.android.react.maps.open.polyline;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

public class    OpenAirMapPolylineManager extends ViewGroupManager<OpenAirMapPolyline> {
    private final DisplayMetrics metrics;

    public OpenAirMapPolylineManager(ReactApplicationContext reactContext) {
        super();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            metrics = new DisplayMetrics();
            ((WindowManager) reactContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getRealMetrics(metrics);
        } else {
            metrics = reactContext.getResources().getDisplayMetrics();
        }
    }

    @Override
    public String getName() {
        return "AIRMapPolyline";
    }

    @Override
    public OpenAirMapPolyline createViewInstance(ThemedReactContext context) {
        return new OpenAirMapPolyline(context);
    }

    @ReactProp(name = "coordinates")
    public void setCoordinate(OpenAirMapPolyline view, ReadableArray coordinates) {
        view.setCoordinates(coordinates);
    }

    @ReactProp(name = "strokeWidth", defaultFloat = 1f)
    public void setStrokeWidth(OpenAirMapPolyline view, float widthInPoints) {
        float widthInScreenPx = metrics.density * widthInPoints; // done for parity with iOS
        view.setWidth(widthInScreenPx);
    }

    @ReactProp(name = "strokeColor", defaultInt = Color.RED, customType = "Color")
    public void setStrokeColor(OpenAirMapPolyline view, int color) {
        view.setColor(color);
    }

    @ReactProp(name = "geodesic", defaultBoolean = false)
    public void setGeodesic(OpenAirMapPolyline view, boolean geodesic) {
        view.setGeodesic(geodesic);
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.of(
                "onPress", MapBuilder.of("registrationName", "onPress")
        );
    }
}
