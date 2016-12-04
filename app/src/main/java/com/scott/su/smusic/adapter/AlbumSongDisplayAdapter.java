package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.AlbumSongViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/11/05.
 */
public abstract class AlbumSongDisplayAdapter extends BaseDisplayAdapter<AlbumSongViewHolder, LocalSongEntity> {
    public abstract void onItemMoreClick(View view, int position, LocalSongEntity entity);


    public AlbumSongDisplayAdapter(Activity context ) {
        super(context);
    }

    public AlbumSongDisplayAdapter(Activity context, List<LocalSongEntity> dataList ) {
        super(context, dataList);
    }

    @Override
    protected AlbumSongViewHolder createVH(ViewGroup parent, int viewType) {
        return new AlbumSongViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_album_song, parent, false));
    }

    @Override
    protected void bindVH(final AlbumSongViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position, null, null, null);
                }
            }
        });

        viewHolder.getMoreImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemMoreClick(view, position, entity);
            }
        });

    }

    @Override
    protected void onDataListChanged() {

    }


}
