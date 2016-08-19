package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayAdapter extends BaseDisplayAdapter<LocalSongViewHolder,LocalSongEntity> {

    public LocalSongDisplayAdapter(Activity context) {
        super(context);
    }

    public LocalSongDisplayAdapter(Activity context, List<LocalSongEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected LocalSongViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalSongViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_local_song,parent,false));
    }

    @Override
    protected void bindVH(LocalSongViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getNumberTextView(),position+"","");
        ViewUtil.setText(viewHolder.getTitleTextView(),entity.getTitle(),"");
        ViewUtil.setText(viewHolder.getArtistTextView(),entity.getArtist(),"");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position, null, null, null);
                }
            }
        });
    }


}
