package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalAlbumEntity implements Parcelable {
    private long albumId;
    private String albumTitle;
    private String artist;
    private List<LocalSongEntity> albumSongs;

    public LocalAlbumEntity() {
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public List<LocalSongEntity> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(List<LocalSongEntity> albumSongs) {
        this.albumSongs = albumSongs;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.albumId);
        dest.writeString(this.albumTitle);
        dest.writeString(this.artist);
        dest.writeTypedList(this.albumSongs);
    }

    protected LocalAlbumEntity(Parcel in) {
        this.albumId = in.readLong();
        this.albumTitle = in.readString();
        this.artist = in.readString();
        this.albumSongs = in.createTypedArrayList(LocalSongEntity.CREATOR);
    }

    public static final Parcelable.Creator<LocalAlbumEntity> CREATOR = new Parcelable.Creator<LocalAlbumEntity>() {
        @Override
        public LocalAlbumEntity createFromParcel(Parcel source) {
            return new LocalAlbumEntity(source);
        }

        @Override
        public LocalAlbumEntity[] newArray(int size) {
            return new LocalAlbumEntity[size];
        }
    };

    public long[] getAlbumSongIdsLongArray() {
        if (albumSongs == null || albumSongs.size() == 0) {
            return null;
        }

        long[] idsLongArr = new long[albumSongs.size()];

        for (int i = 0; i < albumSongs.size(); i++) {
            //Reverse the array:The latest added song should be set on first position;
            idsLongArr[i] = Long.valueOf(albumSongs.get(i).getSongId());
        }
        return idsLongArr;
    }

    @Override
    public String toString() {
        return "LocalAlbumEntity{" +
                "albumId=" + albumId +
                ", albumTitle='" + albumTitle + '\'' +
                ", artist='" + artist + '\'' +
                ", albumSongs=" + albumSongs +
                '}';
    }

}
