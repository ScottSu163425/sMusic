package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalBillViewHolder;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/20.
 */
public class LocalBillDisplayAdapter extends BaseDisplayAdapter<LocalBillViewHolder, LocalBillEntity> {

    public LocalBillDisplayAdapter(Activity context) {
        super(context);
    }

    public LocalBillDisplayAdapter(Activity context, List<LocalBillEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected LocalBillViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalBillViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_bill, parent, false));
    }

    @Override
    protected void bindVH(final LocalBillViewHolder viewHolder, final LocalBillEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getBillTitle(), "");

        ViewUtil.setText(viewHolder.getCountTextView(),
                (entity.isBillEmpty() ? 0 : entity.getBillSongIdsLongArray().length) + " " + context.getString(R.string.unit_song), "");

        String billCoverPath = "";
        if (!entity.isBillEmpty()) {
            billCoverPath = new LocalAlbumModelImpl().getAlbumCoverPathBySongId(context, entity.getLatestSongId());
        }
        ImageLoader.load(context,
                billCoverPath,
                viewHolder.getCoverImageView(),
                R.color.place_holder_loading,
                R.drawable.ic_cover_default_song_bill
        );

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(view, entity, position,
                            new View[]{viewHolder.getCoverImageView()},
                            new String[]{context.getResources().getString(R.string.transition_name_cover)},
                            null);
                }
            }
        });
    }

    @Override
    protected void onDataListChanged() {

    }

}
