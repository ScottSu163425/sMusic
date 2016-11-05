package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/11/05.
 */
public class BillSongViewHolder extends RecyclerView.ViewHolder {
    private TextView numberTextView, titleTextView, artistTextView, albumTextView;
    private ImageView moreImageView;

    public BillSongViewHolder(View itemView) {
        super(itemView);

        numberTextView = (TextView) itemView.findViewById(R.id.tv_number_view_holder_bill_song);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_bill_song);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_bill_song);
        albumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_bill_song);
        moreImageView = (ImageView) itemView.findViewById(R.id.iv_more_view_holder_bill_song);
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

    public ImageView getMoreImageView() {
        return moreImageView;
    }

}
