package com.navigations.homepage;

import android.os.Bundle;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.fragmentlib.HomeFragment;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;

import java.util.Map;

public class HomePageManager extends ViewGroupManager<FrameLayout> {

    public static final String HOME_PAGE_FRAGMENT = "HomePageManager";

    private final int COMMAND_CREATE = 1;
    private final ReactApplicationContext reactContext;

    public HomePageManager(ReactApplicationContext reactContext) {
        this.reactContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return HOME_PAGE_FRAGMENT;
    }

    @NonNull
    @Override
    protected FrameLayout createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new FrameLayout(reactContext);
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("create", COMMAND_CREATE);
    }

    @ReactProp(name = "bundle")
    public void setBundle(@NonNull FrameLayout root, String bundle) {
        Log.e("TAG", "setUrl1: " + bundle);
    }

    @Override
    public void receiveCommand(@NonNull FrameLayout root, String commandId, @Nullable ReadableArray args) {
        super.receiveCommand(root, commandId, args);

        if (args == null) {
            return;
        }
        int reactNativeViewId = args.getInt(0);
        String value = args.getString(1);
        Bundle bundle = new Bundle();
        bundle.putString(HomeFragment.KEY_TITLE, value);
        int commandIdInt = Integer.parseInt(commandId);
        if (commandIdInt == COMMAND_CREATE) {
            createNativeFragment(root, reactNativeViewId, bundle);
        }
        Log.e("TAG", "setUrl2: " + args.getString(1));

    }

    private void createNativeFragment(FrameLayout root, int reactNativeViewId, Bundle bundle) {
        ViewGroup parent = (ViewGroup) root.findViewById(reactNativeViewId).getParent();
        if (parent == null) {
            return;
        }
        setupLayout(parent);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        FragmentActivity activity = (FragmentActivity) reactContext.getCurrentActivity();
        assert activity != null;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(reactNativeViewId, homeFragment, String.valueOf(reactNativeViewId));
        transaction.commit();
    }

    private void setupLayout(View view) {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren(view);
                Choreographer.getInstance().removeFrameCallback(this);
            }
        });
    }

    private void manuallyLayoutChildren(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.EXACTLY));
    }
}
