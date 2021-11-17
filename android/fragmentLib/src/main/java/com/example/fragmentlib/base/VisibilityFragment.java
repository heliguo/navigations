package com.example.fragmentlib.base;

import android.util.Log;

import androidx.fragment.app.Fragment;

/**
 * 懒加载 fragment
 */
public class VisibilityFragment extends Fragment implements IFragmentVisible {

    /**
     * fragment是否对用户可见
     */
    private boolean mIsFragmentVisible = false;
    /**
     * 是否是第一次对用户可见
     */
    private boolean mIsFragmentVisibleFirst = true;

    @Override
    public void onResume() {
        super.onResume();
        determineFragmentVisible();
    }

    @Override
    public void onPause() {
        super.onPause();
        determineFragmentInVisible();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            determineFragmentInVisible();
        } else {
            determineFragmentVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            determineFragmentVisible();
        } else {
            determineFragmentInVisible();
        }
    }


    private void determineFragmentVisible() {
        Fragment parentFragment = getParentFragment();
        if (parentFragment != null && parentFragment instanceof VisibilityFragment) {
            if (!((VisibilityFragment) parentFragment).isVisibleToUser()) {
                //父fragment可见 子fragment必定可见
                return;
            }
        }
        if (isResumed() && !isHidden() && getUserVisibleHint() && !mIsFragmentVisible) {
            mIsFragmentVisible = true;
            onVisible();
            if (mIsFragmentVisibleFirst) {
                mIsFragmentVisibleFirst = false;
                onVisibleFirst();
            } else {
                onVisibleExceptFirst();
            }
            determineChildFragmentVisible();
        }
    }

    private void determineChildFragmentVisible() {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof VisibilityFragment) {
                ((VisibilityFragment) fragment).determineFragmentVisible();
            }
        }
    }

    private void determineFragmentInVisible() {
        if (mIsFragmentVisible) {
            mIsFragmentVisible = false;
            onInVisible();
            determineChildFragmentInVisible();
        }
    }

    private void determineChildFragmentInVisible() {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof VisibilityFragment) {
                ((VisibilityFragment) fragment).determineFragmentInVisible();
            }
        }
    }

    @Override
    public void onVisible() {
        log("onVisible");
    }

    @Override
    public void onInVisible() {
        log("onInVisible");
    }

    @Override
    public void onVisibleFirst() {
        log("onVisibleFirst");
    }

    @Override
    public void onVisibleExceptFirst() {
        log("onVisibleExceptFirst");
    }

    @Override
    public boolean isVisibleToUser() {
        return mIsFragmentVisible;
    }

    private void log(String msg) {
        Log.e("lazy_load", getClass().getSimpleName() + " " + msg);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
