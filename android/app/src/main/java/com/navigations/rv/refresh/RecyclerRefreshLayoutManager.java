package com.navigations.rv.refresh;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewProps;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

import javax.annotation.Nullable;

/**
 * 下拉刷新控件
 */
public class RecyclerRefreshLayoutManager extends ViewGroupManager<RecyclerRefreshLayout> {

    protected static final String REACT_CLASS = "RecyclerRefreshLayout";

    @NonNull
    @Override
    protected RecyclerRefreshLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new RecyclerRefreshLayout(reactContext);
    }

    @NonNull
    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @ReactProp(name = ViewProps.ENABLED, defaultBoolean = true)
    public void setEnabled(RecyclerRefreshLayout view, boolean enabled) {
        view.setEnabled(enabled);
    }

    @ReactProp(name = "colors", customType = "ColorArray")
    public void setColors(RecyclerRefreshLayout view, @Nullable ReadableArray colors) {
        if (colors != null) {
            int[] colorValues = new int[colors.size()];
            for (int i = 0; i < colors.size(); i++) {
                colorValues[i] = colors.getInt(i);
            }
            view.setColorSchemeColors(colorValues);
        } else {
            view.setColorSchemeColors();
        }
    }

    @ReactProp(name = "progressBackgroundColor", defaultInt = Color.TRANSPARENT, customType = "Color")
    public void setProgressBackgroundColor(RecyclerRefreshLayout view, int color) {
        try {
            Log.i("ReactNative", "the color to be used is " + color);
            view.setProgressBackgroundColor(color);
        } catch (Exception e) {
            Log.i("ReactNative", e.getMessage());
        }
    }

    @ReactProp(name = "size", defaultInt = SwipeRefreshLayout.DEFAULT)
    public void setSize(RecyclerRefreshLayout view, int size) {
        view.setSize(size);
    }


    @ReactProp(name = "refreshing")
    public void setRefreshing(RecyclerRefreshLayout view, boolean refreshing) {
        view.setRefreshing(refreshing);
    }

    @Override
    protected void addEventEmitters(@NonNull ThemedReactContext reactContext, @NonNull RecyclerRefreshLayout view) {
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedViewConstants() {
        return MapBuilder.of(
                "SIZE",
                MapBuilder.of("DEFAULT", SwipeRefreshLayout.DEFAULT, "LARGE", SwipeRefreshLayout.LARGE));
    }

    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("refreshRecyclerView", MapBuilder.of("registrationName", "onRefresh"))
                .build();
    }
}
