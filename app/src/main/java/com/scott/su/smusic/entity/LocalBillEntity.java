package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;
import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by asus on 2016/8/21.
 */
@Entity
public class LocalBillEntity implements Parcelable {

    @Transient
    public static final String ID_DIVIDER = "~";

    @Id
    private Long id;

    @Property(nameInDb = "billId")
    private long billId;

    @Property(nameInDb = "billTitle")
    private String billTitle;

    @Property(nameInDb = "billSongIds")
    private String billSongIds;


    public LocalBillEntity() {
        //Auto set bill id with unique number;
        setBillId(System.currentTimeMillis());
    }

    public LocalBillEntity(String billTitle) {
        this.billTitle = billTitle;
        setBillId(System.currentTimeMillis());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
        if (billSongIds == null) {
            billSongIds = "";
        }
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

    public void removeSongId(long songId) {
        if (TextUtils.isEmpty(getBillSongIds())) {
            return;
        }

        setBillSongIds(getBillSongIds().replace(songId + ID_DIVIDER, ""));
    }

    public void clearSongId() {
        setBillSongIds("");
    }

    /**
     * Get the long array of songs in the bill(Order by add time des).
     *
     * @return
     */
    public long[] getBillSongIdsLongArray() {
        if (TextUtils.isEmpty(getBillSongIds())) {
            return null;
        }

        String[] idsStrArr = getBillSongIds().split(ID_DIVIDER);
        long[] idsLongArr = new long[idsStrArr.length];

        for (int i = 0; i < idsStrArr.length; i++) {
            //Reverse the array:The latest added song should be set on first position;
            idsLongArr[idsStrArr.length - 1 - i] = Long.valueOf(idsStrArr[i]);
        }

        return idsLongArr;
    }

    public long getLatestSongId() {
        if (isBillEmpty()) {
            return -1;
        }
        return getBillSongIdsLongArray()[0];
    }

    public boolean isBillEmpty() {
        return TextUtils.isEmpty(getBillSongIds());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(this.id);
        dest.writeLong(this.billId);
        dest.writeString(this.billTitle);
        dest.writeString(this.billSongIds);
    }

    protected LocalBillEntity(Parcel in) {
//        this.id = in.readLong();
        this.billId = in.readLong();
        this.billTitle = in.readString();
        this.billSongIds = in.readString();
    }

    @Generated(hash = 144524664)
    public LocalBillEntity(Long id, long billId, String billTitle, String billSongIds) {
        this.id = id;
        this.billId = billId;
        this.billTitle = billTitle;
        this.billSongIds = billSongIds;
    }

    public static final Parcelable.Creator<LocalBillEntity> CREATOR = new Parcelable.Creator<LocalBillEntity>() {
        @Override
        public LocalBillEntity createFromParcel(Parcel source) {
            return new LocalBillEntity(source);
        }

        @Override
        public LocalBillEntity[] newArray(int size) {
            return new LocalBillEntity[size];
        }
    };

    @Override
    public String toString() {
        return "LocalBillEntity{" +
                "id=" + (id == null ? "" : id) +
                ", billId=" + billId +
                ", billTitle='" + billTitle + '\'' +
                ", billSongIds='" + billSongIds + '\'' +
                '}';
    }
}
