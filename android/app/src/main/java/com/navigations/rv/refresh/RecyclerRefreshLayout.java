package com.navigations.rv.refresh;

import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.react.uimanager.events.NativeGestureUtil;

public class RecyclerRefreshLayout extends SwipeRefreshLayout {

    private static final float DEFAULT_CIRCLE_TARGET = 64;

    private boolean mDidLayout = false;
    private boolean mRefreshing = false;
    private float mProgressViewOffset = 0;
    private int mTouchSlop;
    private float mPrevTouchX;
    private boolean mIntercepted;

    public RecyclerRefreshLayout(Context reactContext) {
        super(reactContext);
        mTouchSlop = ViewConfiguration.get(reactContext).getScaledTouchSlop();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mRefreshing = refreshing;

        // `setRefreshing` must be called after the initial layout otherwise it
        // doesn't work when mounting the component with `refreshing = true`.
        // Known Android issue: https://code.google.com/p/android/issues/detail?id=77712
        if (mDidLayout) {
            super.setRefreshing(refreshing);
        }
    }

    @Override
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (!mDidLayout) {
            mDidLayout = true;

            // Update values that must be set after initial layout.
            setRefreshing(mRefreshing);
        }
    }

    /**
     * {@link SwipeRefreshLayout} overrides {@link ViewGroup#requestDisallowInterceptTouchEvent} and
     * swallows it. This means that any component underneath SwipeRefreshLayout will now interact
     * incorrectly with Views that are above SwipeRefreshLayout. We fix that by transmitting the call
     * to this View's parents.
     */
    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (shouldInterceptTouchEvent(ev) && super.onInterceptTouchEvent(ev)) {
            NativeGestureUtil.notifyNativeGestureStarted(this, ev);

            // If the pull-to-refresh gesture is interrupted by a parent with its own
            // onInterceptTouchEvent then the refresh indicator gets stuck on-screen
            // so we ask the parent to not intercept this touch event after it started
            if (getParent() != null) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }

            return true;
        }
        return false;
    }

    /**
     * {@link SwipeRefreshLayout} completely bypasses ViewGroup's "disallowIntercept" by overriding
     * {@link ViewGroup#onInterceptTouchEvent} and never calling super.onInterceptTouchEvent().
     * This means that horizontal scrolls will always be intercepted, even though they shouldn't, so
     * we have to check for that manually here.
     */
    private boolean shouldInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPrevTouchX = ev.getX();
                mIntercepted = false;
                break;

            case MotionEvent.ACTION_MOVE:
                final float eventX = ev.getX();
                final float xDiff = Math.abs(eventX - mPrevTouchX);

                if (mIntercepted || xDiff > mTouchSlop) {
                    mIntercepted = true;
                    return false;
                }
        }
        return true;
    }
}
