package com.scott.su.smusic.mvp.model.impl;

import android.content.Context;

import com.scott.su.smusic.entity.LocalAlbumEntity;
import com.scott.su.smusic.entity.LocalSongEntity;
import com.scott.su.smusic.mvp.model.LocalAlbumModel;
import com.scott.su.smusic.mvp.model.LocalSongModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/20.
 */
public class LocalAlbumModelImpl implements LocalAlbumModel {

    private LocalSongModel localSongModel;

    public LocalAlbumModelImpl() {
        localSongModel = new LocalSongModelImpl();
    }

    @Override
    public List<LocalAlbumEntity> getLocalAlbums(Context context) {
        List<LocalSongEntity> songEntities = localSongModel.getLocalSongs(context);
        List<LocalAlbumEntity> albumEntities = new ArrayList<>();
        List<String> albumIds = new ArrayList<>();

        //Collect album id.
        for (LocalSongEntity songEntity : songEntities) {
            String albumId = songEntity.getAlbumId() + "";
            if (!albumIds.contains(albumId)) {
                albumIds.add(albumId);
            }
        }

        for (int i = 0; i < albumIds.size(); i++) {
            LocalAlbumEntity albumEntity = new LocalAlbumEntity();
            List<LocalSongEntity> albumSongEntities = new ArrayList<>();
            long albumId = Long.valueOf(albumIds.get(i));

            albumEntity.setAlbumId(albumId);

            for (LocalSongEntity songEntity : songEntities) {
                if (albumId == songEntity.getAlbumId()) {
                    albumSongEntities.add(songEntity);
                }
            }
            albumEntity.setAlbumSongs(albumSongEntities);
            albumEntity.setAlbumTitle(albumSongEntities.get(0).getTitle());
            albumEntity.setArtist(albumSongEntities.get(0).getArtist());

            albumEntities.add(albumEntity);
        }

        return albumEntities;
    }

}
