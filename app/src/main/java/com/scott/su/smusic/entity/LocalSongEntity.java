package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by asus on 2016/8/19.
 */
@Entity
public class LocalSongEntity implements Parcelable {

    @Transient
    public static final String ID_DIVIDER = "~";

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

    @Property(nameInDb = "billIds")
    private String billIds;

    public LocalSongEntity() {
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

    public String getBillIds() {
        if (billIds == null) {
            billIds = "";
        }
        return billIds;
    }

    public void setBillIds(String billIds) {
        this.billIds = billIds;
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

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public void appendBillId(long billId) {
        if (TextUtils.isEmpty(getBillIds())) {
            setBillIds(billId + ID_DIVIDER);
        } else {
            setBillIds(getBillIds() + billId + ID_DIVIDER);
        }
    }

    public void removeBillId(long billId) {
        if (!isBelongingToAnyBill()) {
            return;
        }

        setBillIds(getBillIds().replace(billId + ID_DIVIDER, ""));
    }

    public boolean isBelongingToAnyBill() {
        return !TextUtils.isEmpty(getBillIds());
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
        dest.writeString(this.billIds);
    }

    protected LocalSongEntity(Parcel in) {
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
        this.billIds = in.readString();
    }

    @Generated(hash = 2049056534)
    public LocalSongEntity(Long id, long songId, String title, String artist, String album,
                           long albumId, long duration, long size, String path, String coverPath,
                           String billIds) {
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
        this.billIds = billIds;
    }

    public static final Creator<LocalSongEntity> CREATOR = new Creator<LocalSongEntity>() {
        @Override
        public LocalSongEntity createFromParcel(Parcel source) {
            return new LocalSongEntity(source);
        }

        @Override
        public LocalSongEntity[] newArray(int size) {
            return new LocalSongEntity[size];
        }
    };

    @Override
    public String toString() {
        return "LocalSongEntity{" +
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
                ", billIds='" + billIds + '\'' +
                '}';
    }
}
