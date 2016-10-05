package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalBillSelectionViewHolder;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class LocalBillSelectionDisplayAdapter extends BaseDisplayAdapter<LocalBillSelectionViewHolder, LocalBillEntity> {

    public LocalBillSelectionDisplayAdapter(Activity context) {
        super(context);
    }

    public LocalBillSelectionDisplayAdapter(Activity context, List<LocalBillEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected LocalBillSelectionViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalBillSelectionViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_bill_selection, parent, false));
    }

    @Override
    protected void bindVH(LocalBillSelectionViewHolder viewHolder, final LocalBillEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getBillTitle(), "");

        ImageLoader.load(context,
                new LocalAlbumModelImpl().getAlbumCoverPathBySongId(context, entity.getLatestSongId()),
                viewHolder.getCoverImageView(),
                R.color.place_holder_loading,
                R.drawable.ic_menu_item_bill_selection_36dp);

        if (getItemClickCallback() != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getItemClickCallback().onItemClick(view, entity, position, null, null, null);
                }
            });
        }

    }

    @Override
    protected void onDataListChanged() {

    }


}
