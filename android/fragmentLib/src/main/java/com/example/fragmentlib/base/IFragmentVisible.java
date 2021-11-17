package com.example.fragmentlib.base;

public interface IFragmentVisible {
    /**
     * fragment可见的时候
     */
    void onVisible();

    /**
     * fragment不可见的时候
     */
    void onInVisible();

    /**
     * fragment第一次可见的时候
     */
    void onVisibleFirst();

    /**
     * fragment第一次可见 除了第一次 onVisibleFirst
     */
    void onVisibleExceptFirst();

    /**
     * 返回 true 表示 fragment 是当前可见的 fragment
     * @return
     */
    boolean isVisibleToUser();
}
