package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/11/27.
 */

public class PlayListSecondViewHolder extends RecyclerView.ViewHolder{
    private ImageView coverImageView;
    private TextView titleTextView,artistTextView;


    public PlayListSecondViewHolder(View itemView) {
        super(itemView);
        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_play_list_second);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_play_list_second);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_play_list_second);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getArtistTextView() {
        return artistTextView;
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }
}
