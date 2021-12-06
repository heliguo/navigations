package com.library.rnbanner;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.infer.annotation.Assertions;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.library.rnbanner.util.BannerUtils;

import java.util.Map;

public class RNBannerManager extends ViewGroupManager<Banner> {

    public static final String NAME = "RNBannerManager";

    public static final int COMMAND_BANNER_START = 1;
    public static final int COMMAND_BANNER_STOP = 2;

    private BannerDelegate bannerDelegate;

    @NonNull
    @Override
    public String getName() {
        return NAME;
    }

    @NonNull
    @Override
    protected Banner createViewInstance(@NonNull ThemedReactContext reactContext) {
        Banner banner = new Banner(reactContext);
        bannerDelegate = new BannerDelegate(banner);
        return banner;
    }

    @Override
    public void addView(Banner parent, View child, int index) {
        Assertions.assertCondition(child instanceof Banner, "Views attached to RecyclerViewBackedScrollView must be RecyclerViewItemView views.");
        bannerDelegate.addView(child);
    }

    @Override
    public int getChildCount(Banner parent) {
        return parent.getItemCount();
    }

    @Override
    public View getChildAt(Banner parent, int index) {
        return parent.getChildAt(index);
    }

    @Override
    public void removeViewAt(Banner parent, int index) {
        parent.removeViewAt(index);
    }

    /**
     * 注册数据
     */
    @ReactProp(name = "bannerDataSource")
    public void setBannerDataSource(Banner parent, ReadableArray array) {
        bannerDelegate.setDataSource(array);
    }

    /**
     * 圆角
     */
    @ReactProp(name = "bannerRound")
    public void setBannerRound(Banner parent, int radius) {
        bannerDelegate.setBannerRound(radius);
    }

    /**
     * 设置当前条目
     */
    @ReactProp(name = "currentItem")
    public void setCurrentItem(Banner parent, int index) {
        bannerDelegate.setCurrentItem(index);
    }

    /**
     * 设置当前条目
     */
    @ReactProp(name = "autoLoop")
    public void setAutoLoop(Banner parent, boolean autoLoop) {
        bannerDelegate.setAutoLoop(autoLoop);
    }

    /**
     * 设置轮播间隔时间
     *
     * @param loopTime 时间（毫秒）
     */
    @ReactProp(name = "loopTime")
    public void setLoopTime(Banner parent, int loopTime) {
        bannerDelegate.setLoopTime(loopTime);
    }

    /**
     * 设置轮播滑动过程的时间
     */
    @ReactProp(name = "scrollTime")
    public void setScrollTime(Banner parent, int scrollTime) {
        bannerDelegate.setScrollTime(scrollTime);
    }

    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of(
                "start", COMMAND_BANNER_START,
                "stop", COMMAND_BANNER_STOP
        );
    }

    @Override
    public void receiveCommand(@NonNull Banner root, String commandId, @Nullable ReadableArray args) {
        int command = Integer.parseInt(commandId);
        switch (command) {
            case COMMAND_BANNER_START:
                bannerDelegate.start();
                break;
            case COMMAND_BANNER_STOP:
                bannerDelegate.stop();
                break;
            default:
                break;
        }
    }

}
