package com.example.fragmentlib.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 重写requestLayout 解决react native调用 notify_xxx系列方法 无法刷新UI问题
 */
public class RnRecyclerView extends RecyclerView {

    private boolean isRequestLayout = false;

    public RnRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public RnRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RnRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setItemAnimator(null);
    }

    @SuppressLint("WrongCall")
    @Override
    public void requestLayout() {
        super.requestLayout();
        if (isRequestLayout) {
            return;
        }
        isRequestLayout = true;
        this.post(() -> {
            onLayout(false, getLeft(), getTop(), getRight(), getBottom());
            isRequestLayout = false;
        });
    }

    public void setMoreListener(int count, LoadMoreListener moreListener) {
        if (getAdapter() == null || moreListener == null) {
            return;
        }
        this.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                int lastPosition = 0;
                int lastPositionMax = 0;
                int itemCount = getAdapter().getItemCount();
                if (layoutManager instanceof LinearLayoutManager) {
                    lastPositionMax = lastPosition = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();//完全到底
                } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                    int[] positions = ((StaggeredGridLayoutManager) layoutManager).findLastCompletelyVisibleItemPositions(null);//完全到底
                    lastPosition = lastPosition(positions);
                    lastPositionMax = maxLastPosition(positions);
                }
                if (lastPositionMax == -1 || lastPosition == -1) {
                    return;
                }
                if (dy > 0 && lastPosition >= itemCount - 1 - count && count > 0 && lastPositionMax != itemCount - 1) {
                    moreListener.loadMore(false);
                }
                if (lastPositionMax == itemCount - 1 && dy > 0) {
                    moreListener.loadMore(true);
                }

            }
        });
    }

    private int maxLastPosition(int[] pos) {
        if (pos == null) {
            return -1;
        }
        if (pos.length == 1) {
            return pos[0];
        }
        int max = pos[0];
        for (int i = 1; i < pos.length; i++) {
            max = Math.max(max, pos[i]);
        }
        return max;
    }

    private int lastPosition(int[] pos) {
        if (pos == null) {
            return -1;
        }
        if (pos.length == 1) {
            return pos[0];
        }
        int min = pos[0];
        for (int i = 1; i < pos.length; i++) {
            min = Math.min(min, pos[i]);
        }
        return min;
    }

    public interface LoadMoreListener {
        void loadMore(boolean bottom);
    }

}
