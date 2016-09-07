package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public abstract class LocalSongDisplayAdapter extends BaseDisplayAdapter<LocalSongViewHolder, LocalSongEntity> {

    private DISPLAY_TYPE displayType = DISPLAY_TYPE.NumberDivider; //RecyclerView item layout type

    private LocalSongModelImpl localSongModel;

    public enum DISPLAY_TYPE {
        NumberDivider, //Type that item with number text and bottom divider line.
        CoverDivider, //Type that item with album cover imageview and bottom divider line.
        OnlyNumber, //Type that item only with number textview.
        OnlyCover, //Type that item only with album cover imageview.
        OnlyDivider, //Type that item only with bottom divider line.
        None //Type that item without all above views.
    }

    public abstract void onItemMoreClick(View view, int position, LocalSongEntity entity);


    public LocalSongDisplayAdapter(Activity context, DISPLAY_TYPE displayType) {
        super(context);
        this.displayType = displayType;
        localSongModel = new LocalSongModelImpl();
    }

    public LocalSongDisplayAdapter(Activity context, List<LocalSongEntity> dataList, DISPLAY_TYPE displayType) {
        super(context, dataList);
        this.displayType = displayType;
        localSongModel = new LocalSongModelImpl();
    }

    @Override
    protected LocalSongViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalSongViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_song, parent, false));
    }

    @Override
    protected void bindVH(final LocalSongViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        if (displayType == DISPLAY_TYPE.NumberDivider || displayType == DISPLAY_TYPE.OnlyNumber) {
            if (displayType == DISPLAY_TYPE.NumberDivider) {
                ViewUtil.setViewVisiable(viewHolder.getDividerView());
            } else {
                //OnlyNumber
                ViewUtil.setViewGone(viewHolder.getDividerView());
            }
            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getNumberTextView());
            ViewUtil.setViewGone(viewHolder.getCoverImageView());
            ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        } else if (displayType == DISPLAY_TYPE.CoverDivider || displayType == DISPLAY_TYPE.OnlyCover) {
            if (displayType == DISPLAY_TYPE.CoverDivider) {
                ViewUtil.setViewVisiable(viewHolder.getDividerView());
            } else {
                ViewUtil.setViewGone(viewHolder.getDividerView());
            }
            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getCoverImageView());
            ViewUtil.setViewGone(viewHolder.getNumberTextView());
            //展示图片
            Glide.with(context)
                    .load(new LocalAlbumModelImpl().getAlbumCoverPath(context, entity.getAlbumId()))
                    .placeholder(R.color.place_holder_loading)
                    .error(R.drawable.ic_cover_default_song_bill)
                    .into(viewHolder.getCoverImageView());
        } else if (displayType == DISPLAY_TYPE.OnlyDivider) {
            ViewUtil.setViewGone(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getDividerView());
        } else if (displayType == DISPLAY_TYPE.None) {
            ViewUtil.setViewGone(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewGone(viewHolder.getDividerView());
        }

//        if (getSelectedPosition() == position) {
//            ViewUtil.setViewVisiable(viewHolder.getSelectionIndicatorView());
//        } else {
//            ViewUtil.setViewGone(viewHolder.getSelectionIndicatorView());
//        }

        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectedPosition(position);

                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position,
                            new View[]{viewHolder.getCoverImageView()},
                            new String[]{context.getString(R.string.transition_name_cover)},
                            null);
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
