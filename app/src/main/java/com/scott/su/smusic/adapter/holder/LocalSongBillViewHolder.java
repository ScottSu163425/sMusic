package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongBillViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView, countTextView;
    private ImageView coverImageView;

    public LocalSongBillViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_song_bill);
        countTextView = (TextView) itemView.findViewById(R.id.tv_count_view_holder_local_song_bill);
        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_local_song_bill);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getCountTextView() {
        return countTextView;
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }
}
