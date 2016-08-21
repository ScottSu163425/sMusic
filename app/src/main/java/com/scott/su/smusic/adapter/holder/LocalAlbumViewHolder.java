package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalAlbumViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView, artistTextView, songCountTextView;
    private ImageView coverImageView;

    public LocalAlbumViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_album);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_local_album);
        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_local_album);
        songCountTextView = (TextView) itemView.findViewById(R.id.tv_count_view_holder_local_album);
    }

    public TextView getArtistTextView() {
        return artistTextView;
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }

    public TextView getSongCountTextView() {
        return songCountTextView;
    }
}
