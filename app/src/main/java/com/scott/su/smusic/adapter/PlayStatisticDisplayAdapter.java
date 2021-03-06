package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.PlayStatisticNormalViewHolder;
import com.scott.su.smusic.adapter.holder.PlayStatisticTop3ViewHolder;
import com.scott.su.smusic.entity.PlayStatisticEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticDisplayAdapter extends BaseDisplayAdapter<RecyclerView.ViewHolder, PlayStatisticEntity> {
    public static final int VIEW_TYPE_NORMAL = 123;
    public static final int VIEW_TYPE_TOP_3 = 321;

    public PlayStatisticDisplayAdapter(Activity context) {
        super(context);
    }

    public PlayStatisticDisplayAdapter(Activity context, List<PlayStatisticEntity> dataList) {
        super(context, dataList);
    }

    @Override
    public int getItemViewType(int position) {
//        if (getItemCount() < 3) {
//            return VIEW_TYPE_NORMAL;
//        }

        if (position < 3) {
            return VIEW_TYPE_TOP_3;
        }

        return VIEW_TYPE_NORMAL;
    }

    @Override
    protected RecyclerView.ViewHolder createVH(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            return new PlayStatisticNormalViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_play_statistic_normal, parent, false));
        } else {
            return new PlayStatisticTop3ViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_play_statistic_top_3, parent, false));
        }
    }

    @Override
    protected void bindVH(RecyclerView.ViewHolder viewHolder, final PlayStatisticEntity entity, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_NORMAL) {
            PlayStatisticNormalViewHolder holder = (PlayStatisticNormalViewHolder) viewHolder;
            ViewUtil.setText(holder.getNumberTextView(), (position + 1) + "", "");
            ViewUtil.setText(holder.getTitleTextView(), entity.getTitle(), "");
            ViewUtil.setText(holder.getArtistTextView(), entity.getArtist(), "");
            ViewUtil.setText(holder.getAlbumTextView(), entity.getAlbum(), "");
            ViewUtil.setText(holder.getCountTextView(), entity.getPlayCount() + " " + context.getString(R.string.unit_play_count), "");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemClickCallback() != null) {
                        getItemClickCallback().onItemClick(v, entity, position, null, null, null);
                    }
                }
            });

        } else {
            final PlayStatisticTop3ViewHolder holder = (PlayStatisticTop3ViewHolder) viewHolder;
            holder.getNumberTextView().setText(String.valueOf(position + 1));

            ViewUtil.setText(holder.getTitleTextView(), entity.getTitle(), "");
            ViewUtil.setText(holder.getArtistTextView(), entity.getArtist(), "");
            ViewUtil.setText(holder.getAlbumTextView(), entity.getAlbum(), "");
            ViewUtil.setText(holder.getCountTextView(), entity.getPlayCount() + " " + context.getString(R.string.unit_play_count), "");

            if (position == 0) {
                holder.getNumberTextView().setSlantedBackgroundColor(ContextCompat.getColor(context,R.color.md_yellow_600));
            } else if (position == 1) {
                holder.getNumberTextView().setSlantedBackgroundColor(ContextCompat.getColor(context,R.color.md_grey_400));
            } else if (position == 2) {
                holder.getNumberTextView().setSlantedBackgroundColor(ContextCompat.getColor(context,R.color.md_green_600));
            }

            ImageLoader.load(context, entity.getCoverPath(), holder.getCoverImageView(),
                    R.drawable.ic_cover_default_song_bill, R.drawable.ic_cover_default_song_bill);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getItemClickCallback() != null) {
                        getItemClickCallback().onItemClick(v, entity, position,
                                new View[]{holder.getCoverImageView()},
                                new String[]{context.getString(R.string.transition_name_cover)},
                                null);
                    }
                }
            });
        }


    }

    @Override
    protected void onDataListChanged() {

    }
}
