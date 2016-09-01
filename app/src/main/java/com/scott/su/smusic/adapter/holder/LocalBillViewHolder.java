package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalBillViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView, countTextView;
    private ImageView coverImageView;

    public LocalBillViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_bill);
        countTextView = (TextView) itemView.findViewById(R.id.tv_count_view_holder_local_bill);
        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_local_bill);
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
