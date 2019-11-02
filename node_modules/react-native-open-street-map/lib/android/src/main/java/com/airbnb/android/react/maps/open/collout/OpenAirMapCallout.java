package com.airbnb.android.react.maps.open.collout;


import android.content.Context;

import com.facebook.react.views.view.ReactViewGroup;

public class OpenAirMapCallout extends ReactViewGroup {
    private boolean tooltip = false;
    public int width;
    public int height;

    public OpenAirMapCallout(Context context) {
        super(context);
    }

    public void setTooltip(boolean tooltip) {
        this.tooltip = tooltip;
    }

    public boolean getTooltip() {
        return this.tooltip;
    }
}

