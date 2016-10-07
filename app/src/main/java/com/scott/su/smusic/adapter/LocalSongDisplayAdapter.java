package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.constant.LocalSongDisplayStyle;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalSongModelImpl;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public abstract class LocalSongDisplayAdapter extends BaseDisplayAdapter<LocalSongViewHolder, LocalSongEntity> {

    private LocalSongDisplayStyle localSongDisplayStyle = LocalSongDisplayStyle.NumberDivider; //RecyclerView item layout type

    private LocalSongModelImpl localSongModel;


    public abstract void onItemMoreClick(View view, int position, LocalSongEntity entity);


    public LocalSongDisplayAdapter(Activity context, LocalSongDisplayStyle localSongDisplayStyle) {
        super(context);
        this.localSongDisplayStyle = localSongDisplayStyle;
        localSongModel = new LocalSongModelImpl();
    }

    public LocalSongDisplayAdapter(Activity context, List<LocalSongEntity> dataList, LocalSongDisplayStyle localSongDisplayStyle) {
        super(context, dataList);
        this.localSongDisplayStyle = localSongDisplayStyle;
        localSongModel = new LocalSongModelImpl();
    }

    @Override
    protected LocalSongViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalSongViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_song, parent, false));
    }

    @Override
    protected void bindVH(final LocalSongViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        if (localSongDisplayStyle == LocalSongDisplayStyle.NumberDivider || localSongDisplayStyle == LocalSongDisplayStyle.OnlyNumber) {
            if (localSongDisplayStyle == LocalSongDisplayStyle.NumberDivider) {
                ViewUtil.setViewVisiable(viewHolder.getDividerView());
            } else {
                //OnlyNumber
                ViewUtil.setViewGone(viewHolder.getDividerView());
            }
            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getNumberTextView());
            ViewUtil.setViewGone(viewHolder.getCoverImageView());
            ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        } else if (localSongDisplayStyle == LocalSongDisplayStyle.CoverDivider || localSongDisplayStyle == LocalSongDisplayStyle.OnlyCover) {
            if (localSongDisplayStyle == LocalSongDisplayStyle.CoverDivider) {
                ViewUtil.setViewVisiable(viewHolder.getDividerView());
            } else {
                ViewUtil.setViewGone(viewHolder.getDividerView());
            }
            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getCoverImageView());
            ViewUtil.setViewGone(viewHolder.getNumberTextView());
            ImageLoader.load(context,
                    new LocalAlbumModelImpl().getAlbumCoverPathByAlbumId(context, entity.getAlbumId()),
                    viewHolder.getCoverImageView(),
                    R.color.place_holder_loading,
                    R.drawable.ic_cover_default_song_bill
            );
        } else if (localSongDisplayStyle == LocalSongDisplayStyle.OnlyDivider) {
            ViewUtil.setViewGone(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getDividerView());
        } else if (localSongDisplayStyle == LocalSongDisplayStyle.None) {
            ViewUtil.setViewGone(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewGone(viewHolder.getDividerView());
        }

        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
