package com.navigations.homepage;


import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.HashMap;
import java.util.Map;


public class HomePageModule extends ReactContextBaseJavaModule {

    private static final String MESSAGE = "MESSAGE";


    public HomePageModule(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
        assert reactContext != null;
        reactContext.addActivityEventListener(new ActivityEventListener() {
            @Override
            public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

            }

            @Override
            public void onNewIntent(Intent intent) {

            }
        });
        reactContext.registerComponentCallbacks(new ComponentCallbacks2() {
            @Override
            public void onConfigurationChanged(@NonNull Configuration newConfig) {

            }

            @Override
            public void onLowMemory() {

            }

            @Override
            public void onTrimMemory(int level) {

            }
        });
        reactContext.removeLifecycleEventListener(new LifecycleEventListener() {
            @Override
            public void onHostResume() {

            }

            @Override
            public void onHostPause() {

            }

            @Override
            public void onHostDestroy() {

            }
        });
    }

    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, "message");
        return map;
    }

    @NonNull
    @Override
    public String getName() {
        return HomePageManager.HOME_PAGE_FRAGMENT;
    }


    @ReactMethod
    public void log(String msg) {
        Log.e("TAG", "log: " + msg);
    }

    @ReactMethod
    public void show(String msg) {
        Toast.makeText(getReactApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * callback 机制
     */
    @ReactMethod
    public void testAndroidCallback(String msg, Callback callback) {
        Log.e("TAG", "testAndroidCallback: " + msg);
        if ("1".equals(msg))
            callback.invoke("是的，登陆成功了");
    }

    /**
     * promise 机制
     */
    @ReactMethod
    public void testAndroidPromise(String msg, Promise promise) {
        if ("2".equals(msg))
            promise.resolve("Promise");//正常执行
    }

    /**
     * 发送事件,js注册监听
     */
    @ReactMethod
    public void sendEvent() {
        WritableMap params = Arguments.createMap();
        params.putString("key", "value");
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("EventName", params);
    }
}
