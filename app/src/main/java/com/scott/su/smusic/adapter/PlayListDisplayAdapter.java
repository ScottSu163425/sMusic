package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.PlayListViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
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
        ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position, null , null, null);
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
