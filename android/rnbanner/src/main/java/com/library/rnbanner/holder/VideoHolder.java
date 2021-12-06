package com.library.rnbanner.holder;

import android.view.View;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.library.rnbanner.R;

public class VideoHolder extends RecyclerView.ViewHolder {

    public final VideoView player;

    public VideoHolder(@NonNull View itemView) {
        super(itemView);
        player = itemView.findViewById(R.id.player);
    }
}
