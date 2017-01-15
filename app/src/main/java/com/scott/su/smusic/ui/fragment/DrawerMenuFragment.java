package com.scott.su.smusic.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.DrawerMenuCallback;
import com.scott.su.smusic.config.AppConfig;
import com.scott.su.smusic.mvp.contract.DrawerMenuContract;
import com.su.scott.slibrary.fragment.BaseFragment;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.PopupMenuUtil;
import com.su.scott.slibrary.util.ScreenUtil;
import com.su.scott.slibrary.util.TimeUtil;
import com.su.scott.slibrary.util.ViewUtil;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DrawerMenuFragment extends BaseFragment<DrawerMenuContract.DrawerMenuView, DrawerMenuContract.DrawerMenuPresenter>
        implements View.OnClickListener {
    public static final float PERCENTAGE_OF_SCREEN_WIDTH = 0.83f;
    private View mRootView;
    private ImageView mUserHeadImageView;
    private View mUserCenterItem, mStatisticMenuItem, mTimerMenuItem, mLanguageMenuItem, mSettingsMenuItem;
    private TextView mTimerTimeTextView;
    private SwitchCompat mNightModeSwitch;
    private DrawerMenuCallback mMenuCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_drawer_menu, container, false);

        initPreData();
        initView();
        initData();
        initListener();

        //Make the width of drawer menu is 80% of the screen width.
        mRootView.setLayoutParams(new FrameLayout.LayoutParams((int) (ScreenUtil.getScreenWidth(getActivity()) * PERCENTAGE_OF_SCREEN_WIDTH), ViewGroup.LayoutParams.MATCH_PARENT));
        return mRootView;
    }

    @Override
    public void initPreData() {

    }


    @Override
    public void initView() {
        mUserHeadImageView = (ImageView) mRootView.findViewById(R.id.iv_user_head_fragment_drawer_menu);
        mUserCenterItem = mRootView.findViewById(R.id.rl_item_user_center_drawer_menu);
        mStatisticMenuItem = mRootView.findViewById(R.id.rl_item_statistic_drawer_menu);
        mTimerMenuItem = mRootView.findViewById(R.id.rl_item_timer_drawer_menu);
        mNightModeSwitch = (SwitchCompat) mRootView.findViewById(R.id.swtich_night_mode_drawer_menu);
        mLanguageMenuItem = mRootView.findViewById(R.id.rl_item_language_drawer_menu);
        mTimerTimeTextView = (TextView) mRootView.findViewById(R.id.tv_time_timer_drawer_menu);
        mStatisticMenuItem = mRootView.findViewById(R.id.rl_item_statistic_drawer_menu);
        mSettingsMenuItem = mRootView.findViewById(R.id.rl_item_settings_drawer_menu);
    }

    @Override
    public void initData() {
        mNightModeSwitch.setChecked(AppConfig.isNightModeOn(getActivity()));
    }

    @Override
    public void initListener() {
        mUserHeadImageView.setOnClickListener(this);
        mUserCenterItem.setOnClickListener(this);
        mStatisticMenuItem.setOnClickListener(this);
        mTimerMenuItem.setOnClickListener(this);
        mLanguageMenuItem.setOnClickListener(this);
        mSettingsMenuItem.setOnClickListener(this);
        mNightModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if (mMenuCallback != null) {
                    if (on) {
                        mMenuCallback.onDrawerMenuNightModeOn();
                    } else {
                        mMenuCallback.onDrawerMenuNightModeOff();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (mMenuCallback == null) {
            return;
        }

        if (ViewUtil.isFastClick()) {
            return;
        }

        int id = view.getId();

        if (id == mUserHeadImageView.getId()) {
            mMenuCallback.onDrawerUserHeadClick(view, getString(R.string.transition_name_head));
        } else if (id == mUserCenterItem.getId()) {
            mMenuCallback.onDrawerMenuUserCenterClick(view, mUserHeadImageView, getString(R.string.transition_name_head));
        } else if (id == mStatisticMenuItem.getId()) {
            mMenuCallback.onDrawerMenuStatisticClick(view);
        } else if (id == mTimerMenuItem.getId()) {
            popTimerMenu();
        } else if (id == mLanguageMenuItem.getId()) {
            popLanguageMenu();
        } else if (id == mSettingsMenuItem.getId()) {
            mMenuCallback.onDrawerMenuSettingsClick(view);
        }
    }

    private void popLanguageMenu() {
        PopupMenuUtil.popMenu(getActivity(), R.menu.popup_language_drawer, mLanguageMenuItem,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (mMenuCallback == null) {
                            return true;
                        }

                        if (item.getItemId() == R.id.menu_item_chinese_language_popup_drawer) {
                            mMenuCallback.onDrawerMenuLanguageModeOff();
                        } else if (item.getItemId() == R.id.menu_item_english_language_popup_drawer) {
                            mMenuCallback.onDrawerMenuLanguageModeOn();
                        }
                        return true;
                    }
                });
    }

    private void popTimerMenu() {
        PopupMenuUtil.popMenu(getActivity(), R.menu.popup_timer_drawer, mTimerMenuItem,
                new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (mMenuCallback == null) {
                            return true;
                        }
                        if (item.getItemId() == R.id.menu_item_cancel_timer_popup_drawer) {
                            mMenuCallback.onDrawerMenuTimerCancelClick();
                        } else if (item.getItemId() == R.id.menu_item_15_min_timer_popup_drawer) {
                            mMenuCallback.onDrawerMenuTimerMinutesClick(TimeUtil.minuteToMillisecond(15));
                        } else if (item.getItemId() == R.id.menu_item_30_min_timer_popup_drawer) {
                            mMenuCallback.onDrawerMenuTimerMinutesClick(TimeUtil.minuteToMillisecond(30));
                        } else if (item.getItemId() == R.id.menu_item_60_min_timer_popup_drawer) {
                            mMenuCallback.onDrawerMenuTimerMinutesClick(TimeUtil.minuteToMillisecond(60));
                        } else if (item.getItemId() == R.id.menu_item_120_min_timer_popup_drawer) {
                            mMenuCallback.onDrawerMenuTimerMinutesClick(TimeUtil.minuteToMillisecond(120));
                        }
                        return true;
                    }
                });
    }

    public void setMenuCallback(DrawerMenuCallback menuCallback) {
        this.mMenuCallback = menuCallback;
    }

    public void setTimerTime(long millis) {
        if (millis == 0) {
            mTimerTimeTextView.setText("");
        } else {
            if (TextUtils.isEmpty(mTimerTimeTextView.getText().toString().trim())) {
                AnimUtil.tada(mTimerTimeTextView);
            }

            if (TimeUtil.millisecondToHour(millis) >= 1) {
                mTimerTimeTextView.setText(TimeUtil.millisecondToHHMMSS(millis));
            } else {
                mTimerTimeTextView.setText(TimeUtil.millisecondToMMSS(millis));
            }
        }
    }

    @Override
    protected DrawerMenuContract.DrawerMenuPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onFirstTimeCreateView() {

    }
}
