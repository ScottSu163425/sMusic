package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongViewHolder extends RecyclerView.ViewHolder {
    private TextView mNumberTextView, mTitleTextView, mArtistTextView, mAlbumTextView;
    private ImageView mCoverImageView;

    public LocalSongViewHolder(View itemView) {
        super(itemView);

        mNumberTextView = (TextView) itemView.findViewById(R.id.tv_number_view_holder_local_song);
        mTitleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_song);
        mArtistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_local_song);
        mAlbumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_local_song);
        mCoverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_local_song);
    }

    public TextView getNumberTextView() {
        return mNumberTextView;
    }

    public TextView getArtistTextView() {
        return mArtistTextView;
    }

    public TextView getTitleTextView() {
        return mTitleTextView;
    }

    public TextView getAlbumTextView() {
        return mAlbumTextView;
    }

    public ImageView getCoverImageView() {
        return mCoverImageView;
    }
}
