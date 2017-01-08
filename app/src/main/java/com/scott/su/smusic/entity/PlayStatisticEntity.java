package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

/**
 * Created by asus on 2016/11/13.
 */
@Entity
public class PlayStatisticEntity implements Parcelable {

    @Id
    private Long id;

    @Property(nameInDb = "songId")
    private long songId;

    @Property(nameInDb = "title")
    private String title;

    @Property(nameInDb = "artist")
    private String artist;

    @Property(nameInDb = "album")
    private String album;

    @Property(nameInDb = "albumId")
    private long albumId;

    @Property(nameInDb = "duration")
    private long duration;

    @Property(nameInDb = "size")
    private long size;

    @Property(nameInDb = "path")
    private String path;

    @Property(nameInDb = "coverPath")
    private String coverPath;

    @Property(nameInDb = "playCount")
    private int playCount;

    @Property(nameInDb = "lastPlayTime")
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

    public LocalSongEntity toLocalSongEntity() {
        LocalSongEntity songEntity = new LocalSongEntity();

        songEntity.setSongId(getSongId());
        songEntity.setTitle(getTitle());
        songEntity.setArtist(getArtist());
        songEntity.setAlbum(getAlbum());
        songEntity.setAlbumId(getAlbumId());
        songEntity.setDuration(getDuration());
        songEntity.setPath(getPath());
        songEntity.setSize(getSize());
        songEntity.setCoverPath(getCoverPath());

        return songEntity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
//        dest.writeLong(this.id);
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
//        this.id = in.readLong();
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

    @Generated(hash = 85455696)
    public PlayStatisticEntity(Long id, long songId, String title, String artist, String album,
            long albumId, long duration, long size, String path, String coverPath, int playCount,
            String lastPlayTime) {
        this.id = id;
        this.songId = songId;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumId = albumId;
        this.duration = duration;
        this.size = size;
        this.path = path;
        this.coverPath = coverPath;
        this.playCount = playCount;
        this.lastPlayTime = lastPlayTime;
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
                "id=" + (id == null ? "" : id) +
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
