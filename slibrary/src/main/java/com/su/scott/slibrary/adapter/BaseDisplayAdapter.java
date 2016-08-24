package com.su.scott.slibrary.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.su.scott.slibrary.callback.ItemClickCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseDisplayAdapter
 * ESchoolbag <com.ftet.userhomeframe>
 * Created by su on 2016/4/6.
 * Feature: RecyclerView适配器基类
 */
public abstract class BaseDisplayAdapter<VH extends RecyclerView.ViewHolder, E> extends RecyclerView.Adapter<VH> {

    protected Activity context;

    private RecyclerView recyclerView;

    private List<E> dataList;

    private int selectedPosition = -1;

    private ItemClickCallback<E> itemClickCallback;

    protected abstract VH createVH(ViewGroup parent, int viewType);

    protected abstract void bindVH(VH viewHolder, E entity, int position);

    public BaseDisplayAdapter(Activity context) {
        this.context = context;
    }

    public BaseDisplayAdapter(Activity context, List<E> dataList) {
        this.context = context;
        this.dataList = dataList;
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
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        setRecyclerView(recyclerView);
    }

    public List<E> getDataList() {
        return dataList;
    }

    public void setDataList(List<E> dataList) {
        this.dataList = dataList;
    }

    protected ItemClickCallback<E> getItemClickCallback() {
        return itemClickCallback;
    }

    public void setItemClickCallback(ItemClickCallback<E> itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
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
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        dataList.remove(position);
        notifyItemRemoved(position);
        if (position == selectedPosition) {
            selectedPosition = -1;
        }
    }

}
