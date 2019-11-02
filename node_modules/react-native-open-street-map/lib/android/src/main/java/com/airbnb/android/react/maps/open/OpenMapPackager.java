package com.airbnb.android.react.maps.open;

import android.app.Activity;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OpenMapPackager  implements ReactPackage {
    public OpenMapPackager(Activity activity) {
    } // backwards compatibility

    public OpenMapPackager() {
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
        OpenAirMapManager mapManager = new OpenAirMapManager(reactContext);

        return Arrays.<ViewManager>asList(mapManager);
    }
}
