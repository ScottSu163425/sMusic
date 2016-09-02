package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalBillSelectionViewHolder;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
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
        return new LocalBillSelectionViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_local_bill_selection, parent, false));
    }

    @Override
    protected void bindVH(LocalBillSelectionViewHolder viewHolder, final LocalBillEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getBillTitle(), "");

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
