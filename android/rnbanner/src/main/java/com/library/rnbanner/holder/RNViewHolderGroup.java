package com.library.rnbanner.holder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * View that is going to be used as a cell in {@link androidx.recyclerview.widget.RecyclerView}. It's going to be reusable and
 * we will remove/attach views for a certain positions based on the {@code mViews} array stored
 * in the adapter class.
 * <p>
 * This method overrides {@link #onMeasure} and delegates measurements to the child view that has
 * been attached to. This is because instances of {@link RNViewHolderGroup} are created
 * outside of {@link } and their layout is not managed by that manager
 * as opposed to all the other react-native views. Instead we use dimensions of the child view
 * (dimensions has been set in layouting process) so that size of this view match the size of
 * the view it wraps.
 */
public class RNViewHolderGroup extends ViewGroup {

    private int mLastMeasuredWidth;
    private int mLastMeasuredHeight;

    public RNViewHolderGroup(Context context) {
        super(context);
        mLastMeasuredHeight = 10;
        mLastMeasuredWidth = 10;
        setClickable(true);
    }

    private final OnLayoutChangeListener mChildLayoutChangeListener = (v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
        int oldHeight = (oldBottom - oldTop);
        int newHeight = (bottom - top);

        if (oldHeight != newHeight) {
            if (getParent() != null) {
                requestLayout();
                getParent().requestLayout();
            }
        }
    };

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // This view will only have one child that is managed by the `NativeViewHierarchyManager` and
        // its position and dimensions are set separately. We don't need to handle its layouting here
    }

    @Override
    public void onViewAdded(View child) {
        super.onViewAdded(child);
        child.addOnLayoutChangeListener(mChildLayoutChangeListener);
    }

    @Override
    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        child.removeOnLayoutChangeListener(mChildLayoutChangeListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // We override measure spec and use dimensions of the children. Children is a view added
        // from the adapter and always have a correct dimensions specified as they are calculated
        // and set with NativeViewHierarchyManager.
        // In case there is no view attached, we use the last measured dimensions.

        if (getChildCount() > 0) {
            View child = getChildAt(0);
            mLastMeasuredWidth = child.getMeasuredWidth();
            mLastMeasuredHeight = child.getMeasuredHeight();
        }
        setMeasuredDimension(mLastMeasuredWidth, mLastMeasuredHeight);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Similarly to ReactViewGroup, we return true.
        // In this case it is necessary in order to force the RecyclerView to intercept the touch events,
        // in this way we can exactly know when the drag starts because "onInterceptTouchEvent"
        // of the RecyclerView will return true.
        return true;
    }
}
