package com.library.rnbanner.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.library.rnbanner.R;
import com.library.rnbanner.config.BannerConfig;
import com.library.rnbanner.holder.IViewHolder;
import com.library.rnbanner.listener.OnBannerListener;
import com.library.rnbanner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class BannerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements IViewHolder<T, VH> {
    protected List<T> mData = new ArrayList<>();
    private OnBannerListener<T> mOnBannerListener;
    private VH mViewHolder;
    private int mIncreaseCount = BannerConfig.INCREASE_COUNT;

    public BannerAdapter(List<T> data) {
        setDatas(data);
    }

    /**
     * 设置实体集合（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param data
     */
    public void setDatas(List<T> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        mData = data;
        notifyDataSetChanged();
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 真实的position
     * @return
     */
    public T getData(int position) {
        return mData.get(position);
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 这里传的position不是真实的，获取时转换了一次
     * @return
     */
    public T getRealData(int position) {
        return mData.get(getRealPosition(position));
    }


    @Override
    public final void onBindViewHolder(@NonNull VH holder, int position) {
        mViewHolder = holder;
        int real = getRealPosition(position);
        T data = mData.get(real);
        holder.itemView.setTag(R.id.banner_data_key, data);
        holder.itemView.setTag(R.id.banner_pos_key, real);
        onBindView(holder, mData.get(real), real, getRealCount());
        if (mOnBannerListener != null) {
            holder.itemView.setOnClickListener(view -> mOnBannerListener.OnBannerClick(data, real));
        }
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH vh = onCreateHolder(parent, viewType);
        vh.itemView.setOnClickListener(v -> {
            if (mOnBannerListener != null) {
                T data = (T) vh.itemView.getTag(R.id.banner_data_key);
                int real = (int) vh.itemView.getTag(R.id.banner_pos_key);
                mOnBannerListener.OnBannerClick(data, real);
            }
        });
        return vh;
    }

    @Override
    public int getItemCount() {
        return getRealCount() > 1 ? getRealCount() + mIncreaseCount : getRealCount();
    }

    public int getRealCount() {
        return mData == null ? 0 : mData.size();
    }

    public int getRealPosition(int position) {
        return BannerUtils.getRealPosition(mIncreaseCount == BannerConfig.INCREASE_COUNT, position, getRealCount());
    }

    public void setOnBannerListener(OnBannerListener<T> listener) {
        this.mOnBannerListener = listener;
    }

    public VH getViewHolder() {
        return mViewHolder;
    }

    public void setIncreaseCount(int increaseCount) {
        this.mIncreaseCount = increaseCount;
    }
}