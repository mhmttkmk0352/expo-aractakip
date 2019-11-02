package com.airbnb.android.react.maps.open;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.airbnb.android.react.maps.R;
import com.airbnb.android.react.maps.open.polyline.OpenAirMapPolyline;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.events.EventDispatcher;

import org.osmdroid.api.IMapController;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

public class OpenAirMapView extends MapView implements Marker.OnMarkerDragListener, MapView.OnFirstLayoutListener {
    public MapView map;
    private ProgressBar mapLoadingProgressBar;
    private RelativeLayout mapLoadingLayout;
    private ImageView cacheImageView;
    private Boolean isMapLoaded = true;
    private Integer loadingBackgroundColor = null;
    private Integer loadingIndicatorColor = null;
    private final int baseMapPadding = 50;

    private boolean showUserLocation = false;
    private boolean handlePanDrag = false;
    private boolean moveOnMarkerPress = true;
    private boolean cacheEnabled = false;
    private boolean initialRegionSet = false;
    private int cameraMoveReason = 0;

    private static final String[] PERMISSIONS = new String[] {
            "android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"
    };

    private final List<OpenAirMapFeature> features = new ArrayList<>();
    private final Map<Polyline, OpenAirMapPolyline> polylineMap = new HashMap<>();
    private final GestureDetectorCompat gestureDetector;
    private final OpenAirMapManager manager;
    private LifecycleEventListener lifecycleListener;
    private boolean paused = false;
    private boolean destroyed = false;
    private final ThemedReactContext context;
    private final EventDispatcher eventDispatcher;

    private static boolean contextHasBug(Context context) {
        return context == null ||
                context.getResources() == null ||
                context.getResources().getConfiguration() == null;
    }

    public OpenAirMapView(ThemedReactContext reactContext,
                          ReactApplicationContext appContext,
                          OpenAirMapManager manager) {
        super(reactContext);
        this.manager = manager;
        this.context = reactContext;
        this.map = this;

        gestureDetector = new GestureDetectorCompat(reactContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1,
                                    MotionEvent e2,
                                    float distanceX,
                                    float distanceY) {
                if (handlePanDrag) {
//                onPanDrag(e2);
                }
                return false;
            }
        });

        this.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                                 int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (!paused) {
                    OpenAirMapView.this.cacheView();
                }
            }
        });

        eventDispatcher = reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher();
    }

    private boolean hasPermissions() {
        int permission0 = checkSelfPermission(getContext(), PERMISSIONS[0]);
        int permission1 = checkSelfPermission(getContext(), PERMISSIONS[1]);

        return permission0 == PackageManager.PERMISSION_GRANTED ||
                permission1 == PackageManager.PERMISSION_GRANTED;
    }

    public synchronized void doDestroy() {
        if (destroyed) {
            return;
        }

        destroyed = true;

        if (lifecycleListener != null && context != null) {
            context.removeLifecycleEventListener(lifecycleListener);
            lifecycleListener = null;
        }
        if (!paused) {
//            onPause();
            paused = true;
        }
//        onDestroy();
    }

    public void setInitialRegion(ReadableMap initialRegion) {
        if (!initialRegionSet && initialRegion != null) {
            setRegion(initialRegion);
            initialRegionSet = true;
        }
    }

    public void showZoomControls(boolean hasZoomcontrols) {
        map.setBuiltInZoomControls(hasZoomcontrols);
    }

    public void showMultiTouchControls(boolean hasMultiTouchControls) {
        map.setMultiTouchControls(hasMultiTouchControls);
    }

    public void setRegion(ReadableMap region) {
        if (region == null) return;

        Double lng = region.getDouble("longitude");
        Double lat = region.getDouble("latitude");
        Double lngDelta = region.getDouble("longitudeDelta");
        Double latDelta = region.getDouble("latitudeDelta");
        GeoPoint southwest = new GeoPoint(lat - latDelta / 2, lng - lngDelta / 2);
        GeoPoint northeast = new GeoPoint(lat + latDelta / 2, lng + lngDelta / 2);

        Bounds bounds = new Bounds(southwest, northeast);

        if (super.getHeight() <= 0 || super.getWidth() <= 0) {
            // in this case, our map has not been laid out yet, so we save the bounds in a local
            // variable, and make a guess of zoomLevel 10. Not to worry, though: as soon as layout
            // occurs, we will move the camera to the saved bounds. Note that if we tried to move
            // to the bounds now, it would trigger an exception.
//            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
//            boundsToMove = bounds;
        } else {
//            map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
//            boundsToMove = null;
        }
    }

    public void setShowsUserLocation(boolean showUserLocation) {
        // hold onto this for lifecycle handling
        this.showUserLocation = showUserLocation;
        if (hasPermissions()) {
            //noinspection MissingPermission
            map.setEnabled(showUserLocation);
//            map.setMyLocationEnabled(showUserLocation);
        }
    }

    public void router(ReadableArray array, String titlePointerA, String descritptionA, String titlePointerB, String descritptionB) {
        List<GeoPoint> coordinates = new ArrayList<>();
        if (array.size() > 1) {
            for (int i = 0; i < array.size(); i++) {
                ReadableMap coordinate = array.getMap(i);
                GeoPoint poit = new GeoPoint(coordinate.getDouble("latitude"), coordinate.getDouble("longitude"));
                coordinates.add(i, poit);
            }
            ReadableMap coo = array.getMap(0);
            GeoPoint pt = new GeoPoint(coo.getDouble("latitude"), coo.getDouble("longitude"));    
            IMapController mapController = this.map.getController();
            if (mapController != null) {
                mapController.setZoom(15);
                mapController.setCenter(pt);
            }
            loadingMap(coordinates, titlePointerA, descritptionA, titlePointerB, descritptionB);
        } else {
            ReadableMap coordinate = array.getMap(0);
            GeoPoint poit = new GeoPoint(coordinate.getDouble("latitude"), coordinate.getDouble("longitude"));
            coordinates.add(0, poit);            
            IMapController mapController = this.map.getController();
            if (mapController != null) {
                mapController.setZoom(15);
                mapController.setCenter(poit);
            }
            loadingSingleMap(coordinates, titlePointerA, descritptionA);
        }
    }

    private void loadingSingleMap(List<GeoPoint> pts, String titlePointer, String descritption) {
        Double latitude = pts.get(0).getLatitude();
        Double longitude = pts.get(0).getLongitude();
        GeoPoint geoPoint = new GeoPoint(latitude, longitude);
        OverlayItem myLocation = new OverlayItem(titlePointer, descritption, geoPoint);
        Drawable newMarker = this.getResources().getDrawable(R.drawable.marker_default);
        myLocation.setMarker(newMarker);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(myLocation);

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this.getContext(), items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                return true;
            }
            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }
        });
        mOverlay.setFocusItemsOnTap(true);
        this.map.getOverlays().add(mOverlay);
    }

    private void loadingMap(List<GeoPoint> pts, String titlePointerA, String descritptionA, String titlePointerB,  String descritptionB) {
        Polyline line = new Polyline(this.getContext());
        line.setSubDescription(Polyline.class.getCanonicalName());
        line.setWidth(5);
        line.setColor(this.getResources().getColor(R.color.line_router_color));
        line.setPoints(pts);
        line.setGeodesic(true);
        line.setInfoWindow(new BasicInfoWindow(R.layout.bonuspack_bubble, this.map));

        OverlayItem myLocation = new OverlayItem(titlePointerA, descritptionA, new GeoPoint(pts.get(0).getLatitude(), pts.get(0).getLongitude()));
        Drawable newMarker = this.getResources().getDrawable(R.drawable.person);
        myLocation.setMarker(newMarker);

        OverlayItem locationDestination = new OverlayItem(titlePointerB, descritptionB, new GeoPoint(pts.get(pts.size() - 1).getLatitude(), pts.get(pts.size() - 1).getLongitude()));
        Drawable newMarkerDestinantion = this.getResources().getDrawable(R.drawable.marker_default);
        locationDestination.setMarker(newMarkerDestinantion);

        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(myLocation);
        items.add(locationDestination);

        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(this.getContext(), items, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                //do something
                return true;
            }
            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                return false;
            }
        });
        mOverlay.setFocusItemsOnTap(true);
        this.map.getOverlays().add(mOverlay);
        this.map.getOverlayManager().add(line);
    }

    public void zoom(int zoom) {
        if (map != null) {
            IMapController controller = map.getController();
            if (controller != null) {
                controller.setZoom(zoom);
            }
        }
    }

    public void setCacheEnabled(boolean cacheEnabled) {
        this.cacheEnabled = cacheEnabled;
    }

    public void enableMapLoading(boolean loadingEnabled) {
        if (loadingEnabled && !this.isMapLoaded) {
            this.getMapLoadingLayoutView().setVisibility(View.VISIBLE);
        }
    }

    public void setMoveOnMarkerPress(boolean moveOnPress) {
        this.moveOnMarkerPress = moveOnPress;
    }

    public void setLoadingBackgroundColor(Integer loadingBackgroundColor) {
        Log.v("test", "setLoadingIndicatorColor");
        this.loadingBackgroundColor = loadingBackgroundColor;

        if (this.mapLoadingLayout != null) {
            if (loadingBackgroundColor == null) {
                this.mapLoadingLayout.setBackgroundColor(Color.WHITE);
            } else {
                this.mapLoadingLayout.setBackgroundColor(this.loadingBackgroundColor);
            }
        }
    }

    public void setLoadingIndicatorColor(Integer loadingIndicatorColor) {

        Log.v("test", "setLoadingIndicatorColor");
        this.loadingIndicatorColor = loadingIndicatorColor;
        if (this.mapLoadingProgressBar != null) {
            Integer color = loadingIndicatorColor;
            if (color == null) {
                color = Color.parseColor("#606060");
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList progressTintList = ColorStateList.valueOf(loadingIndicatorColor);
                ColorStateList secondaryProgressTintList = ColorStateList.valueOf(loadingIndicatorColor);
                ColorStateList indeterminateTintList = ColorStateList.valueOf(loadingIndicatorColor);

                this.mapLoadingProgressBar.setProgressTintList(progressTintList);
                this.mapLoadingProgressBar.setSecondaryProgressTintList(secondaryProgressTintList);
                this.mapLoadingProgressBar.setIndeterminateTintList(indeterminateTintList);
            } else {
                PorterDuff.Mode mode = PorterDuff.Mode.SRC_IN;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    mode = PorterDuff.Mode.MULTIPLY;
                }
                if (this.mapLoadingProgressBar.getIndeterminateDrawable() != null)
                    this.mapLoadingProgressBar.getIndeterminateDrawable().setColorFilter(color, mode);
                if (this.mapLoadingProgressBar.getProgressDrawable() != null)
                    this.mapLoadingProgressBar.getProgressDrawable().setColorFilter(color, mode);
            }
        }
    }

    public void setHandlePanDrag(boolean handlePanDrag) {
        this.handlePanDrag = handlePanDrag;
    }

    public int getFeatureCount() {
        return features.size();
    }

    public View getFeatureAt(int index) {
        return features.get(index);
    }

    public void removeFeatureAt(int index) {
        OpenAirMapFeature feature = features.remove(index);
//        if (feature instanceof OpenAirMapMarker) {
//            markerMap.remove(feature.getFeature());
//        }
        feature.removeFromMap(map);
    }

    public WritableMap makeClickEventData(GeoPoint point) {
        WritableMap event = new WritableNativeMap();

        WritableMap coordinate = new WritableNativeMap();
        coordinate.putDouble("latitude", point.getLatitude());
        coordinate.putDouble("longitude", point.getLongitude());
        event.putMap("coordinate", coordinate);

        Projection projection = map.getProjection();
        Point screenPoint = projection.toProjectedPixels(point, null);

        WritableMap position = new WritableNativeMap();
        position.putDouble("x", screenPoint.x);
        position.putDouble("y", screenPoint.y);
        event.putMap("position", position);

        return event;
    }

    public void animateToRegion(GeoPoint bounds, int duration) {
        if (map == null) return;
        map.getController().animateTo(bounds);
    }

    public void animateToCoordinate(GeoPoint coordinate, int duration) {
        if (map == null) return;
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.v("test", "onMarkerDragEnd");
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.v("test", "onMarkerDragStart");
    }

    @Override
    public void onFirstLayout(View v, int left, int top, int right, int bottom) {

    }

    private ProgressBar getMapLoadingProgressBar() {
        if (this.mapLoadingProgressBar == null) {
            this.mapLoadingProgressBar = new ProgressBar(getContext());
            this.mapLoadingProgressBar.setIndeterminate(true);
        }
        if (this.loadingIndicatorColor != null) {
            this.setLoadingIndicatorColor(this.loadingIndicatorColor);
        }
        return this.mapLoadingProgressBar;
    }

    private RelativeLayout getMapLoadingLayoutView() {
        if (this.mapLoadingLayout == null) {
            this.mapLoadingLayout = new RelativeLayout(getContext());
            this.mapLoadingLayout.setBackgroundColor(Color.LTGRAY);
            this.addView(this.mapLoadingLayout,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT);
            this.mapLoadingLayout.addView(this.getMapLoadingProgressBar(), params);

            this.mapLoadingLayout.setVisibility(View.INVISIBLE);
        }
        this.setLoadingBackgroundColor(this.loadingBackgroundColor);
        return this.mapLoadingLayout;
    }

    private ImageView getCacheImageView() {
        if (this.cacheImageView == null) {
            this.cacheImageView = new ImageView(getContext());
            this.addView(this.cacheImageView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            this.cacheImageView.setVisibility(View.INVISIBLE);
        }
        return this.cacheImageView;
    }

    private void removeCacheImageView() {
        if (this.cacheImageView != null) {
            ((ViewGroup) this.cacheImageView.getParent()).removeView(this.cacheImageView);
            this.cacheImageView = null;
        }
    }

    private void removeMapLoadingProgressBar() {
        if (this.mapLoadingProgressBar != null) {
            ((ViewGroup) this.mapLoadingProgressBar.getParent()).removeView(this.mapLoadingProgressBar);
            this.mapLoadingProgressBar = null;
        }
    }

    private void removeMapLoadingLayoutView() {
        this.removeMapLoadingProgressBar();
        if (this.mapLoadingLayout != null) {
            ((ViewGroup) this.mapLoadingLayout.getParent()).removeView(this.mapLoadingLayout);
            this.mapLoadingLayout = null;
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {

        Log.v("test", "onMarkerDragEnd");
    }

    private void cacheView() {
        if (this.cacheEnabled) {
            final ImageView cacheImageView = this.getCacheImageView();
            final RelativeLayout mapLoadingLayout = this.getMapLoadingLayoutView();
            cacheImageView.setVisibility(View.INVISIBLE);
            mapLoadingLayout.setVisibility(View.VISIBLE);
            if (this.isMapLoaded) {
                Log.v("test", "log map");
//                this.map.snapshot(new GoogleMap.SnapshotReadyCallback() {
                //       @Override public void onSnapshotReady(Bitmap bitmap) {
                //         cacheImageView.setImageBitmap(bitmap);
                //         cacheImageView.setVisibility(View.VISIBLE);
                //         mapLoadingLayout.setVisibility(View.INVISIBLE);
                //       }
                //     });
            }
        } else {
            this.removeCacheImageView();
            if (this.isMapLoaded) {
                this.removeMapLoadingLayoutView();
            }
        }
    }
}
