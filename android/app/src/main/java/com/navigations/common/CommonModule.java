package com.navigations.common;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.fragmentlib.liveBus.LiveDataBus;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的Module 提供通用方法调用
 */
public class CommonModule extends ReactContextBaseJavaModule {

    private static final String MESSAGE = "MESSAGE";

    private final ReactApplicationContext reactContext;

    public CommonModule(@Nullable ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
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

    @NonNull
    @Override
    public String getName() {
        return "CommonModule";
    }

    /**
     * 示例
     * RN 可以直接获取MESSAGE 对应的value
     *
     * @return map
     */
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        Map<String, Object> map = new HashMap<>();
        map.put(MESSAGE, "message");
        return map;
    }

    /**
     * 调用Android端log
     */
    @ReactMethod
    public void log(String msg) {
        Log.e("CommonModule", "native log : " + msg);
    }

    /**
     * 调用Android端Toast
     */
    @ReactMethod
    public void show(String msg) {
        Toast.makeText(getReactApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 示例
     * callback 机制
     */
    @ReactMethod
    public void testAndroidCallback(String msg, Callback callback) {
        if ("1".equals(msg))
            callback.invoke("是的，登陆成功了");
    }

    /**
     * 示例
     * promise 机制
     */
    @ReactMethod
    public void testAndroidPromise(String msg, Promise promise) {
        if ("2".equals(msg))
            promise.resolve("Promise");//正常执行
        else
            promise.reject("errorCode", "errorMsg");//错误
    }

    /**
     * 使用LiveDataBus向Native发送事件
     *
     * @param key 事件key（唯一标示）
     * @param obj 发送的数据
     */
    @ReactMethod
    public void postEvent(String key, Object obj) {
        LiveDataBus.get().with(key).postValue(obj);
    }

}
