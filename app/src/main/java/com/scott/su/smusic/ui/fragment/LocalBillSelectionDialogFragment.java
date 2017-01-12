package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.scott.su.smusic.R;
import com.scott.su.smusic.adapter.LocalBillSelectionDisplayAdapter;
import com.scott.su.smusic.entity.LocalBillEntity;
import com.scott.su.smusic.mvp.model.impl.LocalBillModelImpl;
import com.su.scott.slibrary.callback.ItemClickCallback;
import com.su.scott.slibrary.util.CirclarRevealUtil;

/**
 * Created by asus on 2016/8/23.
 */
public class LocalBillSelectionDialogFragment extends DialogFragment {
    private View mRootView;
    private RecyclerView mRecyclerView;
    private LocalBillSelectionDisplayAdapter mAdapter;
    private BillSelectionCallback mCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_dialog_local_bill_selection, container, false);
            mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recycler_view_fragment_dialog_local_bill_selection);

            mAdapter=new LocalBillSelectionDisplayAdapter(getActivity());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setItemClickCallback(new ItemClickCallback<LocalBillEntity>() {
                @Override
                public void onItemClick(View itemView, LocalBillEntity entity, int position, @Nullable View[] sharedElements, @Nullable String[] transitionNames, @Nullable Bundle data) {
                    if (mCallback!=null){
                        mCallback.onBillSelected(entity);
                    }
                }
            });
        }

        mAdapter.setDataList(new LocalBillModelImpl().getBills(getActivity()));
        mAdapter.notifyDataSetChanged();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                CirclarRevealUtil.revealIn(mRootView, CirclarRevealUtil.DIRECTION.LEFT_TOP);
            }
        });

    }

    public void setCallback(BillSelectionCallback mCallback) {
        this.mCallback = mCallback;
    }

    public interface BillSelectionCallback {
        void onBillSelected(LocalBillEntity billEntity);
    }

}
