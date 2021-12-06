package com.library.rnbanner.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageHolder extends RecyclerView.ViewHolder {
    public final ImageView imageView;

    public ImageHolder(@NonNull View itemView) {
        super(itemView);
        this.imageView = ((ImageView) itemView);
    }
}
