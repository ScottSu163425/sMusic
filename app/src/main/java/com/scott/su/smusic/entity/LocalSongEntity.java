package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by asus on 2016/8/19.
 */
public class LocalSongEntity implements Parcelable{
    private long songId;
    private String title;
    private String artist;
    private String album;
    private long albumId;
    private long duration;
    private long size;
    private String path;

    public LocalSongEntity() {
    }

    public long getSongId() {
        return songId;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "LocalSongEntity{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", size=" + size +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.songId);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeLong(this.albumId);
        dest.writeLong(this.duration);
        dest.writeLong(this.size);
        dest.writeString(this.path);
    }

    protected LocalSongEntity(Parcel in) {
        this.songId = in.readLong();
        this.title = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.albumId = in.readLong();
        this.duration = in.readLong();
        this.size = in.readLong();
        this.path = in.readString();
    }

    public static final Parcelable.Creator<LocalSongEntity> CREATOR = new Parcelable.Creator<LocalSongEntity>() {
        @Override
        public LocalSongEntity createFromParcel(Parcel source) {
            return new LocalSongEntity(source);
        }

        @Override
        public LocalSongEntity[] newArray(int size) {
            return new LocalSongEntity[size];
        }
    };
}
