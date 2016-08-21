package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalAlbumViewHolder;
import com.scott.su.smusic.adapter.holder.LocalSongBillViewHolder;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/20.
 */
public class LocalSongBillDisplayAdapter extends BaseDisplayAdapter<LocalSongBillViewHolder, LocalSongBillEntity> {

    public LocalSongBillDisplayAdapter(Activity context) {
        super(context);
    }

    public LocalSongBillDisplayAdapter(Activity context, List<LocalSongBillEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected LocalSongBillViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalSongBillViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_song_bill, parent, false));
    }

    @Override
    protected void bindVH(LocalSongBillViewHolder viewHolder, final LocalSongBillEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getBillTitle(), "");
        ViewUtil.setText(viewHolder.getCountTextView(),
                (entity.getBillSongs() == null ? 0 : entity.getBillSongs().size()) + "首",
                "0首");

        String billCoverPath = "";
        if (entity.getBillSongs() != null && entity.getBillSongs().size() > 0) {
            billCoverPath = new LocalSongModelImpl().getAlbumCoverPath(context,
                    entity.getBillSongs().get(entity.getBillSongs().size()-1).getAlbumId());
        }

        Glide.with(context)
                .load(billCoverPath)
                .placeholder(R.color.place_holder_loading)
                .error(R.drawable.ic_cover_default_song_bill_)
                .into(viewHolder.getCoverImageView());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(view, entity, position, null, null, null);
                }
            }
        });
    }

}
