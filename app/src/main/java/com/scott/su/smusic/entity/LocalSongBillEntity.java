package com.scott.su.smusic.entity;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * Created by asus on 2016/8/21.
 */
@Table(name = "LocalSongBillEntity")
public class LocalSongBillEntity implements Parcelable {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "billTitle")
    private String billTitle;

    @Column(name = "billSongEntities")
    private List<LocalSongEntity> billSongEntities;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBillTitle() {
        return billTitle;
    }

    public void setBillTitle(String billTitle) {
        this.billTitle = billTitle;
    }

    public List<LocalSongEntity> getBillSongEntities() {
        return billSongEntities;
    }

    public void setBillSongEntities(List<LocalSongEntity> billSongEntities) {
        this.billSongEntities = billSongEntities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.billTitle);
        dest.writeTypedList(this.billSongEntities);
    }

    public LocalSongBillEntity() {
    }

    protected LocalSongBillEntity(Parcel in) {
        this.id = in.readInt();
        this.billTitle = in.readString();
        this.billSongEntities = in.createTypedArrayList(LocalSongEntity.CREATOR);
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
                ", billTitle='" + billTitle + '\'' +
                ", billSongEntities=" + billSongEntities +
                '}';
    }
}
