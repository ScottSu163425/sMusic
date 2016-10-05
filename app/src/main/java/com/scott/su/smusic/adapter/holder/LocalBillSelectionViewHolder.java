package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/9/2.
 */
public class LocalBillSelectionViewHolder extends RecyclerView.ViewHolder {
    private ImageView coverImageView;
    private TextView titleTextView;

    public LocalBillSelectionViewHolder(View itemView) {
        super(itemView);

        coverImageView = (ImageView) itemView.findViewById(R.id.iv_cover_view_holder_local_bill_selection);
        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_local_bill_selection);
    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

    public ImageView getCoverImageView() {
        return coverImageView;
    }
}
