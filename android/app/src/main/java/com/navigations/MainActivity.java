package com.navigations;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.example.fragmentlib.liveBus.LiveDataBus;
import com.example.fragmentlib.liveBus.LiveDataBusKey;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.reactnativenavigation.NavigationActivity;

public class MainActivity extends NavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LiveDataBus.get().with(LiveDataBusKey.KEY, Integer.class).observe(this, integer -> {

            ReactInstanceManager reactInstanceManager = MainApplication.get().getReactNativeHost().getReactInstanceManager();
            ReactContext reactContext = reactInstanceManager.getCurrentReactContext();
            if (reactContext != null) {
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("ItemClick", integer);
            }

        });
    }
}
