package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/19.
 */
public class PlayListViewHolder extends RecyclerView.ViewHolder {
    private TextView numberTextView, titleTextView, artistTextView, albumTextView;
    private ImageView indicatorImageView;
    private View removeBtn;

    public PlayListViewHolder(View itemView) {
        super(itemView);

        numberTextView = (TextView) itemView.findViewById(R.id.tv_number_view_holder_play_list);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_play_list);
        artistTextView = (TextView) itemView.findViewById(R.id.tv_artist_view_holder_play_list);
        albumTextView = (TextView) itemView.findViewById(R.id.tv_ablum_view_holder_play_list);
        indicatorImageView = (ImageView) itemView.findViewById(R.id.iv_indicator_view_holder_play_list);
        removeBtn = itemView.findViewById(R.id.iv_remove_view_holder_play_list);
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

    public View getRemoveBtn() {
        return removeBtn;
    }

    public ImageView getIndicatorImageView() {
        return indicatorImageView;
    }
}
