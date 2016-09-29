package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalAlbumViewHolder;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/20.
 */
public class LocalAlbumDisplayAdapter extends BaseDisplayAdapter<LocalAlbumViewHolder, LocalAlbumEntity> {


    public LocalAlbumDisplayAdapter(Activity context) {
        super(context);
    }

    public LocalAlbumDisplayAdapter(Activity context, List<LocalAlbumEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected LocalAlbumViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalAlbumViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_album, parent, false));
    }

    @Override
    protected void bindVH(LocalAlbumViewHolder viewHolder, final LocalAlbumEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getAlbumTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getSongCountTextView(), entity.getAlbumSongs().size() + " " + context.getString(R.string.unit_song), "");

        ImageLoader.load(context,
                new LocalAlbumModelImpl().getAlbumCoverPath(context, entity.getAlbumId()),
                viewHolder.getCoverImageView(),
                R.color.place_holder_loading,
                R.drawable.ic_cover_default_song_bill
        );

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(view, entity, position,
                            new View[]{view},
                            new String[]{context.getString(R.string.transition_name_card)},
                            null);
                }
            }
        });
    }

    @Override
    protected void onDataListChanged() {

    }

}
