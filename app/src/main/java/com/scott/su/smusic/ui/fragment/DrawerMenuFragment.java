package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;

import com.scott.su.smusic.R;
import com.scott.su.smusic.config.AppConfig;
import com.su.scott.slibrary.fragment.BaseFragment;
import com.su.scott.slibrary.util.ScreenUtil;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DrawerMenuFragment extends BaseFragment implements View.OnClickListener {
    private View mRootView;
    private View mStatisticsMenuItem, mUpdateMenuItem, mAboutMenuItem;
    private SwitchCompat mNightModeSwitch, mLanguageModeSwitch;
    private DrawerMenuCallback mMenuCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_drawer_menu, container, false);

        initPreData();
        initView();
        initData();
        initListener();

        mRootView.setLayoutParams(new FrameLayout.LayoutParams((int)(ScreenUtil.getScreenWidth(getActivity()) * 0.8), ViewGroup.LayoutParams.MATCH_PARENT));
        return mRootView;
    }

    @Override
    public View getSnackbarParent() {
        return null;
    }

    @Override
    public void initPreData() {

    }


    @Override
    public void initView() {
        mStatisticsMenuItem = mRootView.findViewById(R.id.rl_item_statistics_drawer_menu);
        mUpdateMenuItem = mRootView.findViewById(R.id.rl_item_update_drawer_menu);
        mAboutMenuItem = mRootView.findViewById(R.id.rl_item_about_drawer_menu);
        mNightModeSwitch = (SwitchCompat) mRootView.findViewById(R.id.swtich_night_mode_drawer_menu);
        mLanguageModeSwitch = (SwitchCompat) mRootView.findViewById(R.id.swtich_language_mode_drawer_menu);
    }

    @Override
    public void initData() {
        mNightModeSwitch.setChecked(AppConfig.isNightModeOn(getActivity()));
        mLanguageModeSwitch.setChecked(AppConfig.isLanguageModeOn(getActivity()));
    }

    @Override
    public void initListener() {
        mStatisticsMenuItem.setOnClickListener(this);
        mUpdateMenuItem.setOnClickListener(this);
        mAboutMenuItem.setOnClickListener(this);

        mNightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if (mMenuCallback != null) {
                    if (on) {
                        mMenuCallback.onNightModeOn();
                    } else {
                        mMenuCallback.onNightModeOff();
                    }
                }
            }
        });

        mLanguageModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if (mMenuCallback != null) {
                    if (on) {
                        mMenuCallback.onLanguageModeOn();
                    } else {
                        mMenuCallback.onLanguageModeOff();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == mStatisticsMenuItem.getId()) {
            if (mMenuCallback != null) {
                mMenuCallback.onStatisticsClick(view);
            }
        } else if (id == mUpdateMenuItem.getId()) {
            if (mMenuCallback != null) {
                mMenuCallback.onUpdateClick(view);
            }
        } else if (id == mAboutMenuItem.getId()) {
            if (mMenuCallback != null) {
                mMenuCallback.onAboutClick(view);
            }
        }
    }

    public void setMenuCallback(DrawerMenuCallback menuCallback) {
        this.mMenuCallback = menuCallback;
    }

    public interface DrawerMenuCallback {
        void onStatisticsClick(View v);

        void onNightModeOn();

        void onNightModeOff();

        void onLanguageModeOn();

        void onLanguageModeOff();

        void onUpdateClick(View v);

        void onAboutClick(View v);
    }

}
