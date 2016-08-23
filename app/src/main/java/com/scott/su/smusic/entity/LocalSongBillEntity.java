package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by asus on 2016/8/21.
 */
@Table(name = "LocalSongBillEntity")
public class LocalSongBillEntity implements Parcelable {

    public static final String ID_DIVIDER = "*";

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "billId")
    private long billId;

    @Column(name = "billTitle")
    private String billTitle;

    @Column(name = "billSongIds")
    private String billSongIds;

    private List<LocalSongEntity> billSongs; //Songs in this bill;


    public LocalSongBillEntity() {
        //Auto set bill id with unique number;
        setBillId(System.currentTimeMillis());
    }

    public LocalSongBillEntity(String billTitle) {
        this.billTitle = billTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getBillId() {
        return billId;
    }

    public void setBillId(long billId) {
        this.billId = billId;
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle;
    }

    public String getBillSongIds() {
        return billSongIds;
    }

    public void setBillSongIds(String billSongIds) {
        this.billSongIds = billSongIds;
    }

    public void appendBillSongId(long songId) {
        if (TextUtils.isEmpty(getBillSongIds())) {
            setBillSongIds(songId + ID_DIVIDER);
        } else {
            setBillSongIds(getBillSongIds() + songId + ID_DIVIDER);
        }
    }

    public List<LocalSongEntity> getBillSongs() {
        return billSongs;
    }

    public void setBillSongs(List<LocalSongEntity> billSongs) {
        this.billSongs = billSongs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.billId);
        dest.writeString(this.billTitle);
        dest.writeString(this.billSongIds);
        dest.writeTypedList(this.billSongs);
    }

    protected LocalSongBillEntity(Parcel in) {
        this.id = in.readInt();
        this.billId = in.readLong();
        this.billTitle = in.readString();
        this.billSongIds = in.readString();
        this.billSongs = in.createTypedArrayList(LocalSongEntity.CREATOR);
    }

    public static final Parcelable.Creator<LocalSongBillEntity> CREATOR = new Parcelable.Creator<LocalSongBillEntity>() {
        @Override
        public LocalSongBillEntity createFromParcel(Parcel source) {
            return new LocalSongBillEntity(source);
        }

        @Override
        public LocalSongBillEntity[] newArray(int size) {
            return new LocalSongBillEntity[size];
        }
    };

    @Override
    public String toString() {
        return "LocalSongBillEntity{" +
                "id=" + id +
                ", billId=" + billId +
                ", billTitle='" + billTitle + '\'' +
                ", billSongIds='" + billSongIds + '\'' +
                ", billSongs=" + billSongs +
                '}';
    }
}
