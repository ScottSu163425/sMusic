package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalAlbumEntity implements Parcelable {
    private long albumId;
    private String albumTitle;
    private String artist;
    private int totalSongCount;


    public LocalAlbumEntity() {
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public int getTotalSongCount() {
        return totalSongCount;
    }

    public void setTotalSongCount(int totalSongCount) {
        this.totalSongCount = totalSongCount;
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
        dest.writeInt(this.totalSongCount);
    }

    protected LocalAlbumEntity(Parcel in) {
        this.albumId = in.readLong();
        this.albumTitle = in.readString();
        this.artist = in.readString();
        this.totalSongCount = in.readInt();
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
}
