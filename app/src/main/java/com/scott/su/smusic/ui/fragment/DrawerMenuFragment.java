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
import android.widget.TextView;

import com.scott.su.smusic.R;
import com.scott.su.smusic.callback.DrawerMenuCallback;
import com.scott.su.smusic.config.AppConfig;
import com.su.scott.slibrary.fragment.BaseFragment;
import com.su.scott.slibrary.util.AnimUtil;
import com.su.scott.slibrary.util.PopupMenuUtil;
import com.su.scott.slibrary.util.ScreenUtil;
import com.su.scott.slibrary.util.TimeUtil;

/**
 * Created by Administrator on 2016/9/8.
 */
public class DrawerMenuFragment extends BaseFragment implements View.OnClickListener {
    private View mRootView;
    private View mTimerMenuItem, mLanguageMenuItem, mUpdateMenuItem, mAboutMenuItem;
    private TextView mTimerTimeTextView;
    private SwitchCompat mNightModeSwitch;
    private DrawerMenuCallback mMenuCallback;

    public static final float PERCENTAGE_OF_SCREEN_WIDTH = 0.80f;

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
    public View getSnackbarParent() {
        return null;
    }

    @Override
    public void initPreData() {

    }


    @Override
    public void initView() {
        mTimerMenuItem = mRootView.findViewById(R.id.rl_item_timer_drawer_menu);
        mUpdateMenuItem = mRootView.findViewById(R.id.rl_item_update_drawer_menu);
        mAboutMenuItem = mRootView.findViewById(R.id.rl_item_about_drawer_menu);
        mNightModeSwitch = (SwitchCompat) mRootView.findViewById(R.id.swtich_night_mode_drawer_menu);
        mLanguageMenuItem = mRootView.findViewById(R.id.rl_item_language_drawer_menu);
        mTimerTimeTextView = (TextView) mRootView.findViewById(R.id.tv_time_timer_drawer_menu);
    }

    @Override
    public void initData() {
        mNightModeSwitch.setChecked(AppConfig.isNightModeOn(getActivity()));
    }

    @Override
    public void initListener() {
        mTimerMenuItem.setOnClickListener(this);
        mLanguageMenuItem.setOnClickListener(this);
        mUpdateMenuItem.setOnClickListener(this);
        mAboutMenuItem.setOnClickListener(this);
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

        int id = view.getId();
        if (id == mTimerMenuItem.getId()) {
            popTimerMenu();
        } else if (id == mLanguageMenuItem.getId()) {
            popLanguageMenu();
        } else if (id == mUpdateMenuItem.getId()) {
            mMenuCallback.onDrawerMenuUpdateClick(view);
        } else if (id == mAboutMenuItem.getId()) {
            mMenuCallback.onDrawerMenuAboutClick(view);
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

}
