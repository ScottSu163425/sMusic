package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by asus on 2016/11/13.
 */
@Table(name = "PlayStatisticEntity")
public class PlayStatisticEntity implements Parcelable {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "songId")
    private long songId;

    @Column(name = "title")
    private String title;

    @Column(name = "artist")
    private String artist;

    @Column(name = "album")
    private String album;

    @Column(name = "albumId")
    private long albumId;

    @Column(name = "duration")
    private long duration;

    @Column(name = "size")
    private long size;

    @Column(name = "path")
    private String path;

    @Column(name = "coverPath")
    private String coverPath;

    @Column(name = "playCount")
    private int playCount;

    @Column(name = "lastPlayTime")
    private String lastPlayTime;


    public void copy(@NonNull LocalSongEntity songEntity) {
        setSongId(songEntity.getSongId());
        setTitle(songEntity.getTitle());
        setArtist(songEntity.getArtist());
        setAlbum(songEntity.getAlbum());
        setAlbumId(songEntity.getAlbumId());
        setDuration(songEntity.getDuration());
        setPath(songEntity.getPath());
        setSize(songEntity.getSize());
        setCoverPath(songEntity.getCoverPath());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public int getPlayCount() {
        return playCount;
    }

    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }

    public String getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(String lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public static Creator<PlayStatisticEntity> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.songId);
        dest.writeString(this.title);
        dest.writeString(this.artist);
        dest.writeString(this.album);
        dest.writeLong(this.albumId);
        dest.writeLong(this.duration);
        dest.writeLong(this.size);
        dest.writeString(this.path);
        dest.writeString(this.coverPath);
        dest.writeInt(this.playCount);
        dest.writeString(this.lastPlayTime);
    }

    public PlayStatisticEntity() {
    }

    protected PlayStatisticEntity(Parcel in) {
        this.id = in.readInt();
        this.songId = in.readLong();
        this.title = in.readString();
        this.artist = in.readString();
        this.album = in.readString();
        this.albumId = in.readLong();
        this.duration = in.readLong();
        this.size = in.readLong();
        this.path = in.readString();
        this.coverPath = in.readString();
        this.playCount = in.readInt();
        this.lastPlayTime = in.readString();
    }

    public static final Creator<PlayStatisticEntity> CREATOR = new Creator<PlayStatisticEntity>() {
        @Override
        public PlayStatisticEntity createFromParcel(Parcel source) {
            return new PlayStatisticEntity(source);
        }

        @Override
        public PlayStatisticEntity[] newArray(int size) {
            return new PlayStatisticEntity[size];
        }
    };

    @Override
    public String toString() {
        return "PlayStatisticEntity{" +
                "id=" + id +
                ", songId=" + songId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", albumId=" + albumId +
                ", duration=" + duration +
                ", size=" + size +
                ", path='" + path + '\'' +
                ", coverPath='" + coverPath + '\'' +
                ", playCount=" + playCount +
                ", lastPlayTime='" + lastPlayTime + '\'' +
                '}';
    }
}
