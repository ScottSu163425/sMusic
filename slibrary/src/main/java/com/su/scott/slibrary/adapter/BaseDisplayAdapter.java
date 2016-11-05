package com.su.scott.slibrary.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.su.scott.slibrary.callback.ItemClickCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by su on 2016/4/6.
 * Feature: RecyclerView适配器基类
 */
public abstract class BaseDisplayAdapter<VH extends RecyclerView.ViewHolder, E> extends RecyclerView.Adapter<VH> {
    public static final int POSITION_NONE = -1;

    protected Activity context;
    private RecyclerView recyclerView;
    private List<E> dataList = new ArrayList<>();
    private int selectedPosition = -1;
    private ItemClickCallback<E> itemClickCallback;


    protected abstract VH createVH(ViewGroup parent, int viewType);

    protected abstract void bindVH(VH viewHolder, E entity, int position);

    protected abstract void onDataListChanged();


    public BaseDisplayAdapter(Activity context) {
        this.context = context;
    }

    public BaseDisplayAdapter(Activity context, List<E> dataList) {
        this.context = context;
        this.setDataList(dataList);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createVH(parent, viewType);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        bindVH(holder, dataList.get(holder.getAdapterPosition()), holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    public List<E> getDataList() {
        return this.dataList;
    }

    public void setDataList(@NonNull List<E> dataList) {
//        this.dataList = dataList;
        this.dataList.clear();
        this.dataList.addAll(dataList);
        onDataListChanged();
    }

    public void addData(@NonNull E data) {
        dataList.add(data);
        onDataListChanged();
    }

    public void addDataList(@NonNull List<E> dataList) {
        this.dataList.addAll(dataList);
        onDataListChanged();
    }


    protected ItemClickCallback<E> getItemClickCallback() {
        return itemClickCallback;
    }

    public void setItemClickCallback(@NonNull ItemClickCallback<E> itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public E getItemEntity(int position) {
        if (null == getDataList()) {
            return null;
        }
        return dataList.get(position);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        if (position == selectedPosition) {
            selectedPosition = -1;
        }
    }

}
