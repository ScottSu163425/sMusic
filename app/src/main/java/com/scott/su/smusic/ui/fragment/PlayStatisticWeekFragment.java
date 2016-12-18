package com.scott.su.smusic.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.Tools;
import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;
import com.scott.su.smusic.R;
import com.scott.su.smusic.mvp.contract.PlayStatisticWeekContract;
import com.su.scott.slibrary.fragment.BaseFragment;

/**
 * Created by asus on 2016/11/19.
 */

public class PlayStatisticWeekFragment extends BaseFragment<PlayStatisticWeekContract.PlayStatisticWeekView, PlayStatisticWeekContract.PlayStatisticWeekPresenter> {
    private View mRootView;
    private LineChartView mChart;

    //    private final String[] mLabels = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
//    private final String[] mLabels = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private final float[] mValues = {13, 18, 26, 25, 30, 27, 29};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_play_statistic_week, container, false);

        return mRootView;
    }

    @Override
    protected PlayStatisticWeekContract.PlayStatisticWeekPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onFirstTimeCreateView() {
        mChart = (LineChartView) mRootView.findViewById(R.id.chart_fragment_play_statistic_week);

        LineSet dataset = new LineSet(getResources().getStringArray(R.array.labels_play_statistic), mValues);
        dataset.setColor(Color.parseColor("#ffc1bd"))
                .setFill(Color.parseColor("#3dff73"))
                .setGradientFill(new int[]{getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary)},
                        null);
        mChart.addData(dataset);

        mChart.setBorderSpacing(1)
                .setXLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .setXAxis(true)
                .setAxisColor(getResources().getColor(R.color.colorAccent))
                .setAxisThickness(5)
                .setLabelsColor(Color.WHITE)
                .setYAxis(true)
                .setBorderSpacing(Tools.fromDpToPx(3));

        Animation anim = new Animation();
        mChart.show(anim);
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

    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
