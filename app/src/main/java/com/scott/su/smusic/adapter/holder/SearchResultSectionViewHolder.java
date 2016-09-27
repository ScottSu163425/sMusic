package com.scott.su.smusic.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;

/**
 * Created by asus on 2016/9/27.
 */
public class SearchResultSectionViewHolder extends RecyclerView.ViewHolder {
    private TextView titleTextView;

    public SearchResultSectionViewHolder(View itemView) {
        super(itemView);

        titleTextView = (TextView) itemView.findViewById(R.id.tv_title_view_holder_search_result_section);

    }

    public TextView getTitleTextView() {
        return titleTextView;
    }

}
