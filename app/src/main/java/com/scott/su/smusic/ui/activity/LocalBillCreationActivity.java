package com.scott.su.smusic.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.scott.su.smusic.R;
import com.scott.su.smusic.mvp.contract.LocalBillCreationContract;
import com.su.scott.slibrary.activity.BaseActivity;

public class LocalBillCreationActivity extends BaseActivity<LocalBillCreationContract.LocalBillCreationView,LocalBillCreationContract.LocalBillCreationPresenter> {


    @Override
    protected LocalBillCreationContract.LocalBillCreationPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_bill_creation);
    }

    @Override
    public void initPreData() {

    }

    @Override
    public void initToolbar() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
