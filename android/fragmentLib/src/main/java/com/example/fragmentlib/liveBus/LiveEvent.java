package com.example.fragmentlib.liveBus;


import static androidx.lifecycle.Lifecycle.State.CREATED;
import static androidx.lifecycle.Lifecycle.State.DESTROYED;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;


import java.util.Iterator;
import java.util.Map;


/**
 * 事件
 */
public class LiveEvent<T> {

    private static final String TAG = "LiveEvent";

    private static final Object NOT_SET = new Object();
    private final SafeIterableMap<Observer<? super T>, ObserverWrapper> mObservers = new SafeIterableMap<>();
    private volatile Object mData = NOT_SET;
    private boolean mDispatchingValue;
    @SuppressWarnings("FieldCanBeLocal")
    private boolean mDispatchInvalidated;

    public T getValue() {
        if (mData == NOT_SET) {
            return null;
        }
        return (T) mData;
    }

    private void considerNotify(ObserverWrapper observer) {
        if (!observer.mActive) {
            return;
        }
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false);
            return;
        }
        if (mData == NOT_SET) {
            // 从没发送过事件直接忽视分发事件
            return;
        }
        //noinspection unchecked
        observer.mObserver.onChanged((T) mData);
    }

    @SuppressWarnings("WeakerAccess")
    private void dispatchingValue(@Nullable ObserverWrapper initiator) {
        if (mDispatchingValue) {
            mDispatchInvalidated = true;
            return;
        }
        mDispatchingValue = true;
        do {
            mDispatchInvalidated = false;
            if (initiator != null) {
                considerNotify(initiator);
                initiator = null;
            } else {
                for (Iterator<Map.Entry<Observer<? super T>, ObserverWrapper>> iterator =
                     mObservers.iteratorWithAdditions(); iterator.hasNext(); ) {
                    considerNotify(iterator.next().getValue());
                    if (mDispatchInvalidated) {
                        break;
                    }
                }
            }
        } while (mDispatchInvalidated);
        mDispatchingValue = false;
    }

    /**
     * 感知生命周期 能收到注册之后发送的消息
     *
     * @param owner
     * @param observer
     */
    @MainThread
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        assertMainThread("observe");
        realObserve(owner, observer, false);
    }

    /**
     * 自动取消订阅 能收到注册之前发送的消息
     *
     * @param owner
     * @param observer
     */
    @MainThread
    public void observeSticky(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        assertMainThread("observeSticky");
        realObserve(owner, observer, true);
    }

    @MainThread
    private void realObserve(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer,
                             boolean isStickyMode) {
        if (owner.getLifecycle().getCurrentState() == DESTROYED) {
            return;
        }
        LifecycleBoundObserver wrapper = new LifecycleBoundObserver(owner, observer, isStickyMode);
        ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
        if (existing != null && !existing.isAttachedTo(owner)) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        owner.getLifecycle().addObserver(wrapper);
    }

    /**
     * 不感知生命周期 只能收到注册之后发送的消息
     *
     * @param observer
     */
    @MainThread
    public void observeForever(@NonNull Observer<? super T> observer) {
        assertMainThread("observeForever");
        realObserveForever(observer, false);
    }

    /**
     * 不感知生命周期 并且能收到 注册之前发送出来的消息
     *
     * @param observer
     */
    @MainThread
    public void observeForeverSticky(@NonNull Observer<? super T> observer) {
        assertMainThread("observeForeverSticky");
        realObserveForever(observer, true);
    }

    @MainThread
    private void realObserveForever(@NonNull Observer<? super T> observer, boolean isStickyMode) {
        AlwaysActiveObserver wrapper = new AlwaysActiveObserver(observer, isStickyMode);
        ObserverWrapper existing = mObservers.putIfAbsent(observer, wrapper);
        if (existing != null && (existing instanceof LiveEvent.LifecycleBoundObserver)) {
            throw new IllegalArgumentException("Cannot add the same observer"
                    + " with different lifecycles");
        }
        if (existing != null) {
            return;
        }
        wrapper.activeStateChanged(true);
    }

    @MainThread
    public void removeObserver(@NonNull final Observer<? super T> observer) {
        assertMainThread("removeObserver");
        ObserverWrapper removed = mObservers.remove(observer);
        if (removed == null) {
            return;
        }
        removed.detachObserver();
        removed.activeStateChanged(false);
    }

    @SuppressWarnings("WeakerAccess")
    @MainThread
    public void removeObservers(@NonNull final LifecycleOwner owner) {
        assertMainThread("removeObservers");
        for (Map.Entry<Observer<? super T>, ObserverWrapper> entry : mObservers) {
            if (entry.getValue().isAttachedTo(owner)) {
                removeObserver(entry.getKey());
            }
        }
    }

    public void postValue(final T value) {
        if (DefaultTaskExecutor.getInstance().isMainThread())
            setValue(value);
        else
            DefaultTaskExecutor.getInstance().postToMainThread(() -> setValue(value));
    }

    @MainThread
    private void setValue(T value) {
        assertMainThread("setValue");
        mData = value;
        dispatchingValue(null);
    }

    @SuppressLint("RestrictedApi")
    class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver {
        @NonNull
        final LifecycleOwner mOwner;

        LifecycleBoundObserver(@NonNull LifecycleOwner owner, Observer<? super T> observer,
                               boolean isStickyMode) {
            super(observer, isStickyMode);
            mOwner = owner;
        }

        @Override
        boolean shouldBeActive() {
            return mOwner.getLifecycle().getCurrentState().isAtLeast(CREATED);
        }

        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

            if (mOwner.getLifecycle().getCurrentState() == DESTROYED) {
                removeObserver(mObserver);
                return;
            }
            Log.e(TAG, "onStateChanged: " + mOwner.getLifecycle().getCurrentState()+"   "+mOwner);
            activeStateChanged(shouldBeActive());
        }

        @Override
        boolean isAttachedTo(LifecycleOwner owner) {
            return mOwner == owner;
        }

        @Override
        void detachObserver() {
            mOwner.getLifecycle().removeObserver(this);
        }
    }

    private abstract class ObserverWrapper {
        final Observer<? super T> mObserver;
        boolean mActive;
        final boolean isStickyMode;

        ObserverWrapper(Observer<? super T> observer, final boolean isStickyMode) {
            mObserver = observer;
            this.isStickyMode = isStickyMode;
        }

        abstract boolean shouldBeActive();

        boolean isAttachedTo(LifecycleOwner owner) {
            return false;
        }

        void detachObserver() {
        }

        void activeStateChanged(boolean newActive) {
            if (newActive == mActive) {
                return;
            }
            mActive = newActive;
            if (mActive && isStickyMode) {
                dispatchingValue(this);
            }
        }
    }

    private class AlwaysActiveObserver extends ObserverWrapper {

        AlwaysActiveObserver(Observer<? super T> observer, boolean isStickyMode) {
            super(observer, isStickyMode);
        }

        @Override
        boolean shouldBeActive() {
            return true;
        }
    }

    private static void assertMainThread(String methodName) {
        if (!DefaultTaskExecutor.getInstance().isMainThread()) {
            throw new IllegalStateException("Cannot invoke " + methodName + " on a background"
                    + " thread");
        }
    }
}