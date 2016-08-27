package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/27.
 */
public class LocalSongSelectionViewHolder extends RecyclerView.ViewHolder {
    private TextView numberTextView, titleTextView, artistTextView, albumTextView;
    private AppCompatCheckBox checkBox;

    public LocalSongSelectionViewHolder(View itemView) {
        super(itemView);

        numberTextView = (TextView) itemView.findViewById(R.id.tv_number_view_holder_local_song_selection);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_song_selection);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_local_song_selection);
        albumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_local_song_selection);
        checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox_view_holder_local_song_selection);
    }

    public TextView getNumberTextView() {
        return numberTextView;
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

    public AppCompatCheckBox getCheckBox() {
        return checkBox;
    }
}
