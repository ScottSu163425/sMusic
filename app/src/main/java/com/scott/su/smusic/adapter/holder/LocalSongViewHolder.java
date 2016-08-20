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
public class LocalSongViewHolder extends RecyclerView.ViewHolder {
    private FrameLayout coverAreaLayout;
    private View selectionIndicatorView;
    private TextView numberTextView, titleTextView, artistTextView, albumTextView;
    private ImageView coverImageView, moreImageView;
    private View dividerView;

    public LocalSongViewHolder(View itemView) {
        super(itemView);

        coverAreaLayout = (FrameLayout) itemView.findViewById(R.id.fl_cover_area_view_holder_local_song);
        selectionIndicatorView = itemView.findViewById(R.id.view_indicator_view_holder_local_song);
        numberTextView = (TextView) itemView.findViewById(R.id.tv_number_view_holder_local_song);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_song);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_local_song);
        albumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_local_song);
        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_local_song);
        moreImageView = (ImageView) itemView.findViewById(R.id.iv_more_view_holder_local_song);
        dividerView = itemView.findViewById(R.id.view_divider_view_holder_local_song);
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

    public ImageView getCoverImageView() {
        return coverImageView;
    }

    public ImageView getMoreImageView() {
        return moreImageView;
    }

    public FrameLayout getCoverAreaLayout() {
        return coverAreaLayout;
    }

    public View getSelectionIndicatorView() {
        return selectionIndicatorView;
    }

    public View getDividerView() {
        return dividerView;
    }
}
