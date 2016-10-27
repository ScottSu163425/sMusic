package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.PlayListViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/10/24.
 */
public abstract class PlayListDisplayAdapter extends BaseDisplayAdapter<PlayListViewHolder, LocalSongEntity> {

    public abstract void onItemRemoveClick(View view, int position, LocalSongEntity entity);


    public PlayListDisplayAdapter(Activity context) {
        super(context);
    }

    public PlayListDisplayAdapter(Activity context, List<LocalSongEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected PlayListViewHolder createVH(ViewGroup parent, int viewType) {
        return new PlayListViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_play_list, parent, false));
    }

    @Override
    protected void bindVH(final PlayListViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        if (getSelectedPosition() == position) {
            //Set current position indicator as playing state;
            ViewUtil.setViewVisiable(viewHolder.getIndicatorImageView());
            ViewUtil.setViewGone(viewHolder.getNumberTextView());
            AnimUtil.rotate2D(viewHolder.getIndicatorImageView(), AnimUtil.ROTATION_DEGREE_ROUND_QUARTER, -AnimUtil.ROTATION_DEGREE_ROUND, AnimUtil.DURATION_NORMAL, new OvershootInterpolator(), null);
        } else {
            ViewUtil.setViewVisiable(viewHolder.getNumberTextView());
            ViewUtil.setViewGone(viewHolder.getIndicatorImageView());
            ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        }

        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSelectedPosition() != -1) {
                    notifyItemChanged(getSelectedPosition());
                }
                setSelectedPosition(position);
                notifyItemChanged(position);
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position, null, null, null);
                }
            }
        });

        viewHolder.getRemoveBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemRemoveClick(view, position, entity);
            }
        });

    }

    @Override
    protected void onDataListChanged() {

    }


}
