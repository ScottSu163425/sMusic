package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.PlayListSecondViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.List;

/**
 * Created by asus on 2016/12/3.
 */

public class PlayListSecondDisplayAdapter extends BaseDisplayAdapter<PlayListSecondViewHolder, LocalSongEntity> {


    public PlayListSecondDisplayAdapter(Activity context) {
        super(context);
    }

    public PlayListSecondDisplayAdapter(Activity context, List<LocalSongEntity> dataList) {
        super(context, dataList);
    }

    @Override
    protected PlayListSecondViewHolder createVH(ViewGroup parent, int viewType) {
        return new PlayListSecondViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_play_list_second, parent, false));
    }

    @Override
    protected void bindVH(PlayListSecondViewHolder viewHolder, LocalSongEntity entity, int position) {
        ImageLoader.load(context,
                entity.getCoverPath(),
                viewHolder.getCoverImageView(),
                R.drawable.ic_cover_default_song_bill,
                R.drawable.ic_cover_default_song_bill);

        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
    }

    @Override
    protected void onDataListChanged() {

    }
}
