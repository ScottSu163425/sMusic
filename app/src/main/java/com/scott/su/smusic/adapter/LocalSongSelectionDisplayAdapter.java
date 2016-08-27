package com.scott.su.smusic.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalSongSelectionViewHolder;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.su.scott.slibrary.adapter.BaseDisplayAdapter;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2016/8/27.
 */
public class LocalSongSelectionDisplayAdapter extends BaseDisplayAdapter<LocalSongSelectionViewHolder, LocalSongEntity> {
    private Map<Integer, Boolean> checkStates;


    public LocalSongSelectionDisplayAdapter(Activity context) {
        super(context);
        checkStates = new HashMap<>();
    }


    public LocalSongSelectionDisplayAdapter(Activity context, List<LocalSongEntity> dataList) {
        super(context, dataList);
        checkStates = new HashMap<>();
    }

    private void initCheckStates() {
        for (int i = 0; i < getDataList().size(); i++) {
            checkStates.put(i, false);
        }
    }

    @Override
    protected LocalSongSelectionViewHolder createVH(ViewGroup parent, int viewType) {
        return new LocalSongSelectionViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.view_holder_local_song_selection, parent, false));
    }

    @Override
    protected void bindVH(final LocalSongSelectionViewHolder viewHolder, final LocalSongEntity entity, final int position) {
        ViewUtil.setText(viewHolder.getNumberTextView(), (position + 1) + "", "");
        ViewUtil.setText(viewHolder.getTitleTextView(), entity.getTitle(), "");
        ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
        ViewUtil.setText(viewHolder.getAlbumTextView(), entity.getAlbum(), "");

        viewHolder.getCheckBox().setChecked(checkStates.get(position));

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.getCheckBox().setChecked(!viewHolder.getCheckBox().isChecked());
                checkStates.put(position, viewHolder.getCheckBox().isChecked());
                if (getItemClickCallback() != null) {
                    getItemClickCallback().onItemClick(v, entity, position, null, null, null);
                }
            }
        });


    }

    @Override
    protected void onDataListChanged() {
        initCheckStates();
    }

    /**
     * select or unselect all,it depends on current check state;
     */
    public void selectAll() {
        boolean isSelected;
        int selectedSongsCount = getSelectedSongsCount();

        if (selectedSongsCount == 0) {
            //No one item has bean selected,then select all;
            isSelected = true;
        } else if (selectedSongsCount < getDataList().size()) {
            //More than one (but less than the total count) item has bean selected,then select all;
            isSelected = true;
        } else {
            //All items have bean selected,then unselect all;
            isSelected = false;
        }

        for (int i = 0; i < checkStates.size(); i++) {
            checkStates.put(i, isSelected);
        }

        this.notifyDataSetChanged();

    }

    public int getSelectedSongsCount() {
        int count = 0;

        for (int i = 0; i < getDataList().size(); i++) {
            if (checkStates.get(i)) {
                count++;
            }
        }

        return count;
    }

    public List<LocalSongEntity> getSelectedSongs() {
        List selectedSongs = new ArrayList();

        for (int i = 0; i < getDataList().size(); i++) {
            if (checkStates.get(i)) {
                selectedSongs.add(getDataList().get(i));
            }
        }

        return selectedSongs;
    }


}
