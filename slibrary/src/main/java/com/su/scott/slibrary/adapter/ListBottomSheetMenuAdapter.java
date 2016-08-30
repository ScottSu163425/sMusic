package com.su.scott.slibrary.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.su.scott.slibrary.R;
import com.su.scott.slibrary.adapter.holder.ListBottomSheetMenuItemViewHolder;
import com.su.scott.slibrary.entity.BottomSheetMenuItemEntity;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ListBottomSheetMenuAdapter extends BaseDisplayAdapter<ListBottomSheetMenuItemViewHolder, BottomSheetMenuItemEntity> {

    public ListBottomSheetMenuAdapter(Activity context) {
        super(context);
    }

    public ListBottomSheetMenuAdapter(Activity context, List<BottomSheetMenuItemEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected ListBottomSheetMenuItemViewHolder createVH(ViewGroup parent, int viewType) {
        return new ListBottomSheetMenuItemViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_menu_item_base_list_bottom_sheet, parent, false));
    }

    @Override
    protected void bindVH(final ListBottomSheetMenuItemViewHolder viewHolder, final BottomSheetMenuItemEntity entity, final int position) {
        viewHolder.getIconImageView().setImageResource(entity.getIconId());
        viewHolder.getItemNameTextView().setText(entity.getItemName());
        if (getItemClickCallback() != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getItemClickCallback().onItemClick(viewHolder.itemView, entity, position, null, null, null);
                }
            });
        }
    }

    @Override
    protected void onDataListChanged() {

    }

}
