package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.T;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongDisplayAdapter extends BaseDisplayAdapter<LocalSongViewHolder, LocalSongEntity> {

    private DISPLAY_TYPE displayType = DISPLAY_TYPE.WithNumber; //列表类型（有无专辑封面图片）

    private LocalSongModelImpl localSongModel;

    public enum DISPLAY_TYPE {
        WithNumber,
        WithCover,
        None
    }


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
    protected void bindVH(LocalSongViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        if (this.displayType == DISPLAY_TYPE.WithNumber) {
            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewGone(viewHolder.getCoverImageView());
            ViewUtil.setViewVisiable(viewHolder.getNumberTextView());
            ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        } else if (this.displayType == DISPLAY_TYPE.WithCover) {
            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewGone(viewHolder.getNumberTextView());
            ViewUtil.setViewVisiable(viewHolder.getCoverImageView());
            //展示图片
            Glide.with(context)
                    .load(localSongModel.getAlbumCoverPath(context, entity.getAlbumId()))
                    .placeholder(R.color.place_holder_loading)
                    .error(R.color.place_holder_error)
                    .into(viewHolder.getCoverImageView());
        } else if (this.displayType == DISPLAY_TYPE.None) {
            ViewUtil.setViewGone(viewHolder.getCoverAreaLayout());
        }

        if (getSelectedPosition() == position) {
            ViewUtil.setViewVisiable(viewHolder.getSelectionIndicatorView());
        } else {
            ViewUtil.setViewGone(viewHolder.getSelectionIndicatorView());
        }

        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setSelectedPosition(position);

                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position, null, null, null);
                }
            }
        });

        viewHolder.getMoreImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                T.showShort(context, entity.getTitle());
            }
        });

    }


}
