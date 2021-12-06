package com.library.rnbanner;


import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.library.rnbanner.indicator.CircleIndicator;
import com.library.rnbanner.util.GsonUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 管理banner view 的添加
 */
public class BannerDelegate {

    private static final String TAG = "BannerDelegate";

    private final Banner banner;

    private final List<View> viewList = new ArrayList<>();

    private final SparseArray<View> viewSparseArray = new SparseArray<>();
    private MultipleTypesAdapter multipleTypesAdapter;

    public BannerDelegate(Banner banner) {
        this.banner = banner;
    }

    public void setDataSource(ReadableArray array) {
        if (array == null || array.size() == 0) {
            return;
        }
        List<BannerDataSource> dataSources = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            HashMap<String, Object> hashMap = array.getMap(i).toHashMap();
            JSONObject jsonObject = new JSONObject(hashMap);
            BannerDataSource dataSource = GsonUtils.gson().fromJson(jsonObject.toString(), BannerDataSource.class);
            if (dataSource.viewType == 2) {//需要RN 传递view
                calculateView(dataSource.rnIndex);
            }
            dataSources.add(dataSource);
        }
        multipleTypesAdapter = new MultipleTypesAdapter(dataSources, this);
        banner.setAdapter(multipleTypesAdapter);
        setAdapterListener();
    }

    public void setDataSource(List<BannerDataSource> dataSource) {
        if (dataSource == null || dataSource.isEmpty()) {
            return;
        }
        for (BannerDataSource source : dataSource) {
            if (source.viewType == 2) {//需要RN 传递view
                calculateView(source.rnIndex);
            }
        }
        multipleTypesAdapter = new MultipleTypesAdapter(dataSource, this);
        banner.setAdapter(multipleTypesAdapter);
        setAdapterListener();
    }

    private void setAdapterListener() {
        if (multipleTypesAdapter == null) {
            return;
        }
        multipleTypesAdapter.setOnBannerListener((data, position) ->
                getReactContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit("itemClick", position));
    }

    private ReactContext getReactContext() {
        return (ReactContext) (ThemedReactContext) banner.getContext();
    }

    public void addView(View view) {
        viewList.add(view);
    }

    /**
     * 根据数据中的index 保存 RN view 真实位置
     *
     * @param index {@link BannerDataSource#rnIndex}
     */
    public void calculateView(int index) {
        if (viewList.isEmpty()) {
            return;
        }
        viewSparseArray.put(index, viewList.remove(0));
    }

    public View getViewByIndex(int index) {
        return viewSparseArray.get(index);
    }


    public void setCurrentItem(int index) {
        banner.setCurrentItem(index);
    }

    public void setAutoLoop(boolean autoLoop) {
        Log.e(TAG, "setAutoLoop: " + autoLoop);
        banner.isAutoLoop(autoLoop);
    }

    public void setScrollTime(int scrollTime) {
        banner.setScrollTime(scrollTime);
    }

    public void setLoopTime(long loopTime) {
        banner.setLoopTime(loopTime);
    }

    public void start() {
        banner.start();
    }

    public void stop() {
        banner.stop();
    }

    public void setBannerRound(int radius) {
        banner.setBannerRound(radius);
        banner.setIndicator(new CircleIndicator(getReactContext()));
        banner.setIndicatorSelectedColor(Color.parseColor("#ff0000"));
    }
}
