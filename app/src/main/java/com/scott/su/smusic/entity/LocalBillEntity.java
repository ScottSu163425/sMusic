package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2016/8/21.
 */
@Table(name = "LocalBillEntity")
public class LocalBillEntity implements Parcelable {

    public static final String ID_DIVIDER = "~";

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "billId")
    private long billId;

    @Column(name = "billTitle")
    private String billTitle;

    @Column(name = "billSongIds")
    private String billSongIds;

//    private List<LocalSongEntity> billSongs; //Songs in this bill;


    public LocalBillEntity() {
        //Auto set bill id with unique number;
        setBillId(System.currentTimeMillis());
    }

    public LocalBillEntity(String billTitle) {
        this.billTitle = billTitle;
        setBillId(System.currentTimeMillis());
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

//    public List<LocalSongEntity> getBillSongs() {
//        //Keep billSongs not null;
//        if (billSongs == null) {
//            billSongs = new ArrayList<>();
//        }
//        return billSongs;
//    }

//    public void setBillSongs(List<LocalSongEntity> billSongs) {
//        this.billSongs = billSongs;
//    }

//    public LocalSongEntity getLatestSong() {
//        if (isBillEmpty()) {
//            return null;
//        }
//        return getBillSongs().get(getBillSongs().size() - 1);
//    }

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
        dest.writeInt(this.id);
        dest.writeLong(this.billId);
        dest.writeString(this.billTitle);
        dest.writeString(this.billSongIds);
//        dest.writeTypedList(this.billSongs);
    }

    protected LocalBillEntity(Parcel in) {
        this.id = in.readInt();
        this.billId = in.readLong();
        this.billTitle = in.readString();
        this.billSongIds = in.readString();
//        this.billSongs = in.createTypedArrayList(LocalSongEntity.CREATOR);
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
                "id=" + id +
                ", billId=" + billId +
                ", billTitle='" + billTitle + '\'' +
                ", billSongIds='" + billSongIds + '\'' +
//                ", billSongs=" + billSongs +
                '}';
    }
}
