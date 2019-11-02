package com.airbnb.android.react.maps.open;


import android.app.Activity;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenAirMapModule extends ReactContextBaseJavaModule {

    private static final String SNAPSHOT_RESULT_FILE = "file";
    private static final String SNAPSHOT_RESULT_BASE64 = "base64";
    private static final String SNAPSHOT_FORMAT_PNG = "png";
    private static final String SNAPSHOT_FORMAT_JPG = "jpg";

    public OpenAirMapModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "OpenAirMapModule";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put("legalNotice", "This license information is displayed in Settings > Google > Open Source on any device running Google Play services.");
        return constants;
    }

    public Activity getActivity() {
        return getCurrentActivity();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }
}