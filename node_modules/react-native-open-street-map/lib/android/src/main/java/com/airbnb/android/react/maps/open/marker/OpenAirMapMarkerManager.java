package com.airbnb.android.react.maps.open.marker;

import com.airbnb.android.react.maps.open.SizeReportingShadowNode;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.LayoutShadowNode;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class OpenAirMapMarkerManager  extends ViewGroupManager<OpenAirMapMarker> {

    private static final int SHOW_INFO_WINDOW = 1;
    private static final int HIDE_INFO_WINDOW = 2;

    public OpenAirMapMarkerManager() {
    }

    @Override
    public String getName() {
        return "AIRMapMarker";
    }

    @Override
    public OpenAirMapMarker createViewInstance(ThemedReactContext context) {
        return new OpenAirMapMarker(context);
    }

    @ReactProp(name = "anchor")
    public void setAnchor(OpenAirMapMarker view, ReadableMap map) {
        // should default to (0.5, 1) (bottom middle)
        double x = map != null && map.hasKey("x") ? map.getDouble("x") : 0.5;
        double y = map != null && map.hasKey("y") ? map.getDouble("y") : 1.0;
        float xFloat = (float)x;
        float yFloat = (float)y;

        view.setY(yFloat);
        view.setX(xFloat);
    }

    @ReactProp(name = "calloutAnchor")
    public void setCalloutAnchor(OpenAirMapMarker view, ReadableMap map) {
        // should default to (0.5, 0) (top middle)
        double x = map != null && map.hasKey("x") ? map.getDouble("x") : 0.5;
        double y = map != null && map.hasKey("y") ? map.getDouble("y") : 0.0;
        float xFloat = (float)x;
        float yFloat = (float)y;

        view.setY(yFloat);
        view.setX(xFloat);
    }

    @ReactProp(name = "rotation", defaultFloat = 0.0f)
    public void setMarkerRotation(OpenAirMapMarker view, float rotation) {
        view.setRotation(rotation);
    }

    @ReactProp(name = "draggable", defaultBoolean = false)
    public void setDraggable(OpenAirMapMarker view, boolean draggable) {
        view.setEnabled(draggable);
    }

    @Override
    @Nullable
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "showCallout", SHOW_INFO_WINDOW,
                "hideCallout", HIDE_INFO_WINDOW
        );
    }

    @Override
    public void receiveCommand(OpenAirMapMarker view, int commandId, @Nullable ReadableArray args) {
        switch (commandId) {
            case SHOW_INFO_WINDOW:
                ((Marker) view.getFeature()).showInfoWindow();
                break;

            case HIDE_INFO_WINDOW:
                ((Marker) view.getFeature()).hideInfoWindow();
                break;
        }
    }

    @Override
    @Nullable
    public Map getExportedCustomDirectEventTypeConstants() {
        Map<String, Map<String, String>> map = MapBuilder.of(
                "onPress", MapBuilder.of("registrationName", "onPress"),
                "onCalloutPress", MapBuilder.of("registrationName", "onCalloutPress"),
                "onDragStart", MapBuilder.of("registrationName", "onDragStart"),
                "onDrag", MapBuilder.of("registrationName", "onDrag"),
                "onDragEnd", MapBuilder.of("registrationName", "onDragEnd")
        );

        map.putAll(MapBuilder.of(
                "onDragStart", MapBuilder.of("registrationName", "onDragStart"),
                "onDrag", MapBuilder.of("registrationName", "onDrag"),
                "onDragEnd", MapBuilder.of("registrationName", "onDragEnd")
        ));

        return map;
    }

    @Override
    public LayoutShadowNode createShadowNodeInstance() {
        // we use a custom shadow node that emits the width/height of the view
        // after layout with the updateExtraData method. Without this, we can't generate
        // a bitmap of the appropriate width/height of the rendered view.
        return new SizeReportingShadowNode();
    }

    @Override
    public void updateExtraData(OpenAirMapMarker view, Object extraData) {
        // This method is called from the shadow node with the width/height of the rendered
        // marker view.
        HashMap<String, Float> data = (HashMap<String, Float>) extraData;
        float width = data.get("width");
        float height = data.get("height");

//        view.update((int) width, (int) height);
    }
}
