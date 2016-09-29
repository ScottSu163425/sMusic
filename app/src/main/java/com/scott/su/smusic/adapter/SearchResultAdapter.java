package com.scott.su.smusic.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.holder.LocalAlbumViewHolder;
import com.scott.su.smusic.adapter.holder.LocalBillViewHolder;
import com.scott.su.smusic.adapter.holder.LocalSongViewHolder;
import com.scott.su.smusic.adapter.holder.SearchResultSectionViewHolder;
import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.impl.LocalAlbumModelImpl;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.manager.ImageLoader;
import com.su.scott.slibrary.util.DeviceUtil;
import com.su.scott.slibrary.util.ScreenUtil;
import com.su.scott.slibrary.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class SearchResultAdapter extends RecyclerView.Adapter {
    private Context context;
    private List result = new ArrayList();
    private OnSearchResultClickListener onSearchResultClickListener;

    private static final int VIEW_TYPE_SECTION = 0;
    private static final int VIEW_TYPE_LOCAL_SONG = 1;
    private static final int VIEW_TYPE_LOCAL_BILL = 2;
    private static final int VIEW_TYPE_LOCAL_ALBUM = 3;
    private static final int VIEW_TYPE_NONE = -1;


    public SearchResultAdapter(Context context) {
        this.context = context;
    }

    public void setResult(List result) {
        this.result = result;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION) {
            return new SearchResultSectionViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_search_result_section, parent, false));
        }

        if (viewType == VIEW_TYPE_LOCAL_SONG) {
            return new LocalSongViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_local_song, parent, false));
        }

        if (viewType == VIEW_TYPE_LOCAL_BILL) {
            return new LocalBillViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_local_bill, parent, false));
        }

        if (viewType == VIEW_TYPE_LOCAL_ALBUM) {
            return new LocalAlbumViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_local_album, parent, false));
        }

        return new LocalSongViewHolder(LayoutInflater.from(context).inflate(R.layout.view_holder_local_song, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);

        if (viewType == VIEW_TYPE_SECTION) {
            String entity = (String) result.get(position);
            SearchResultSectionViewHolder viewHolder = (SearchResultSectionViewHolder) holder;

            viewHolder.getTitleTextView().setText(entity);
        } else if (viewType == VIEW_TYPE_LOCAL_SONG) {
            final LocalSongViewHolder viewHolder = (LocalSongViewHolder) holder;
            final LocalSongEntity entity = (LocalSongEntity) result.get(position);

            ViewUtil.setViewVisiable(viewHolder.getCoverAreaLayout());
            ViewUtil.setViewVisiable(viewHolder.getCoverImageView());
            ViewUtil.setViewGone(viewHolder.getNumberTextView());

            viewHolder.getTitleTextView().setText(entity.getTitle());
            viewHolder.getArtistTextView().setText(entity.getArtist());
            viewHolder.getAlbumTextView().setText(entity.getAlbum());

            ImageLoader.load(context,
                    new LocalAlbumModelImpl().getAlbumCoverPath(context, entity.getAlbumId()),
                    viewHolder.getCoverImageView(),
                    R.color.place_holder_loading,
                    R.drawable.ic_cover_default_song_bill
            );

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                setSelectedPosition(position);

                    if (onSearchResultClickListener != null) {
                        onSearchResultClickListener.onLocalSongClick(entity, viewHolder.getCoverImageView(), context.getString(R.string.transition_name_cover));
                    }
                }
            });

            viewHolder.getMoreImageView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSearchResultClickListener != null) {
                        onSearchResultClickListener.onLocalSongMoreClick(entity);
                    }
                }
            });
        } else if (viewType == VIEW_TYPE_LOCAL_BILL) {
            final LocalBillViewHolder viewHolder = (LocalBillViewHolder) holder;
            final LocalBillEntity entity = (LocalBillEntity) result.get(position);

            ViewUtil.setText(viewHolder.getTitleTextView(), entity.getBillTitle(), "");
            ViewUtil.setText(viewHolder.getCountTextView(),
                    (entity.isBillEmpty() ? 0 : entity.getBillSongIdsLongArray().length) + " " + context.getString(R.string.unit_song), "");

            String billCoverPath = "";
            if (!entity.isBillEmpty()) {
                billCoverPath = new LocalAlbumModelImpl().getAlbumCoverPath(context,
                        new LocalBillModelImpl().getBillSong(context, entity.getLatestSongId()).getAlbumId());
            }
            ImageLoader.load(context,
                    billCoverPath,
                    viewHolder.getCoverImageView(),
                    R.color.place_holder_loading,
                    R.drawable.ic_cover_default_song_bill
            );
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSearchResultClickListener != null) {
                        onSearchResultClickListener.onLocalBillClick(entity, viewHolder.getCoverImageView(), context.getResources().getString(R.string.transition_name_cover));
                    }
                }
            });
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.size_image_view_holder_local_bill),
                    (int) context.getResources().getDimension(R.dimen.size_image_view_holder_local_bill));
            layoutParams.setMargins((int) context.getResources().getDimension(R.dimen.margin_xs),
                    (int) context.getResources().getDimension(R.dimen.margin_xs),
                    (int) context.getResources().getDimension(R.dimen.margin_xs),
                    (int) context.getResources().getDimension(R.dimen.margin_xs));
            viewHolder.itemView.setLayoutParams(layoutParams);
        } else if (viewType == VIEW_TYPE_LOCAL_ALBUM) {
            final LocalAlbumViewHolder viewHolder = (LocalAlbumViewHolder) holder;
            final LocalAlbumEntity entity = (LocalAlbumEntity) result.get(position);

            ViewUtil.setText(viewHolder.getTitleTextView(), entity.getAlbumTitle(), "");
            ViewUtil.setText(viewHolder.getArtistTextView(), entity.getArtist(), "");
            ViewUtil.setText(viewHolder.getSongCountTextView(), entity.getAlbumSongs().size() + " " + context.getString(R.string.unit_song), "");

            ImageLoader.load(context,
                    new LocalAlbumModelImpl().getAlbumCoverPath(context, entity.getAlbumId()),
                    viewHolder.getCoverImageView(),
                    R.color.place_holder_loading,
                    R.drawable.ic_cover_default_song_bill
            );
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSearchResultClickListener != null) {
                        onSearchResultClickListener.onLocalAlbumClick(entity, view, context.getString(R.string.transition_name_card));
                    }
                }
            });

        } else if (viewType == VIEW_TYPE_NONE) {
            return;
        }

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (result.get(position) instanceof String) {
            return VIEW_TYPE_SECTION;
        }

        if (result.get(position) instanceof LocalSongEntity) {
            return VIEW_TYPE_LOCAL_SONG;
        }

        if (result.get(position) instanceof LocalBillEntity) {
            return VIEW_TYPE_LOCAL_BILL;
        }

        if (result.get(position) instanceof LocalAlbumEntity) {
            return VIEW_TYPE_LOCAL_ALBUM;
        }

        return VIEW_TYPE_NONE;
    }

    public void setOnSearchResultClickListener(OnSearchResultClickListener onSearchResultClickListener) {
        this.onSearchResultClickListener = onSearchResultClickListener;
    }

    public interface OnSearchResultClickListener {
        void onLocalSongClick(LocalSongEntity entity, View sharedElement, String transitionName);

        void onLocalSongMoreClick(LocalSongEntity entity);

        void onLocalBillClick(LocalBillEntity entity, View sharedElement, String transitionName);

        void onLocalAlbumClick(LocalAlbumEntity entity, View sharedElement, String transitionName);
    }

}
