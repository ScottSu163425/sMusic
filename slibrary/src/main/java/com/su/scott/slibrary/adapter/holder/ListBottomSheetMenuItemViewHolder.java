package com.su.scott.slibrary.adapter.holder;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.su.scott.slibrary.R;

/**
 * Created by Administrator on 2016/8/30.
 */
public class ListBottomSheetMenuItemViewHolder extends RecyclerView.ViewHolder {
    private ImageView iconImageView;
    private TextView itemNameTextView;

    public ListBottomSheetMenuItemViewHolder(View itemView) {
        super(itemView);
        iconImageView = (ImageView) itemView.findViewById(R.id.iv_icon_view_holder_menu_item_base_list_bottom_sheet);
        itemNameTextView = (TextView) itemView.findViewById(R.id.tv_item_name_view_holder_menu_item_base_list_bottom_sheet);
    }

    public ImageView getIconImageView() {
        return iconImageView;
    }

    public TextView getItemNameTextView() {
        return itemNameTextView;
    }
}
