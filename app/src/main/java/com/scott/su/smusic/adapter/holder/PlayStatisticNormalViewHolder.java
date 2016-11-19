package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/11/19.
 */
public class PlayStatisticNormalViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView, artistTextView, albumTextView, numberTextView, countTextView;

    public PlayStatisticNormalViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_play_statistic);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_play_statistic);
        albumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_play_statistic);
        numberTextView = (TextView) itemView.findViewById(R.id.tv_number_view_holder_play_statistic);
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

    public TextView getNumberTextView() {
        return numberTextView;
    }

    public TextView getCountTextView() {
        return countTextView;
    }
}
