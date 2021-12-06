package com.library.rnbanner;

import android.net.Uri;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.library.rnbanner.adapter.BannerAdapter;
import com.library.rnbanner.holder.ImageHolder;
import com.library.rnbanner.holder.RNViewHolder;
import com.library.rnbanner.holder.RNViewHolderGroup;
import com.library.rnbanner.holder.VideoHolder;
import com.library.rnbanner.util.BannerUtils;

import java.util.List;

/**
 * 自定义布局,多个不同UI切换
 */
public class MultipleTypesAdapter extends BannerAdapter<BannerDataSource, RecyclerView.ViewHolder> {


    private final BannerDelegate delegate;

    public MultipleTypesAdapter(List<BannerDataSource> mData, BannerDelegate delegate) {
        super(mData);
        this.delegate = delegate;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 2:
                return new VideoHolder(BannerUtils.getView(parent, R.layout.banner_video));
            case 3:
                return new RNViewHolder(new RNViewHolderGroup(parent.getContext()));
            case 1:
            default:
                return new ImageHolder(BannerUtils.getView(parent, R.layout.banner_image));
        }
    }

    @Override
    public int getItemViewType(int position) {
        //先取得真实的position,在获取实体
//        return getData(getRealPosition(position)).viewType;
        //直接获取真实的实体
        return getRealData(position).viewType;
        //或者自己直接去操作集合
//        return mData.get(getRealPosition(position)).viewType;
    }

    @Override
    public void onBindView(RecyclerView.ViewHolder holder, BannerDataSource data, int position, int size) {
        int viewType = holder.getItemViewType();
        switch (viewType) {
            case 1://网络图片
                ImageHolder imageHolder = (ImageHolder) holder;
                Glide.with(imageHolder.imageView).load(data.imageUrl).into(imageHolder.imageView);
                break;
            case 3://video
                VideoHolder videoHolder = (VideoHolder) holder;
                Uri uri = Uri.parse(data.imageUrl);
                videoHolder.player.setVideoURI(uri);
                break;
            case 2://RN View
                RNViewHolder rnViewHolder = (RNViewHolder) holder;
                ((RNViewHolderGroup) rnViewHolder.itemView).addView(delegate.getViewByIndex(position));
                break;
        }
    }

}
