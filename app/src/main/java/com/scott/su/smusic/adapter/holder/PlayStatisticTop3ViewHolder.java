package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haozhang.lib.SlantedTextView;
import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/11/19.
 */
public class PlayStatisticTop3ViewHolder extends RecyclerView.ViewHolder {
    private ImageView coverImageView;
    private TextView titleTextView, artistTextView, albumTextView,  countTextView;
    private SlantedTextView numberTextView;

    public PlayStatisticTop3ViewHolder(View itemView) {
        super(itemView);

        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_play_statistic);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_play_statistic);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_play_statistic);
        albumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_play_statistic);
        numberTextView = (SlantedTextView) itemView.findViewById(R.id.tv_number_view_holder_play_statistic);
        countTextView = (TextView) itemView.findViewById(R.id.tv_count_view_holder_play_statistic);
    }

    public TextView getArtistTextView() {
        return artistTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public TextView getAlbumTextView() {
        return albumTextView;
    }

    public SlantedTextView getNumberTextView() {
        return numberTextView;
    }

    public TextView getCountTextView() {
        return countTextView;
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }
}
