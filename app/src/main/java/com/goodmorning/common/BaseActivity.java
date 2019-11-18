package com.goodmorning.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.goodmorning.common.view.BaseExceptionView;
import com.goodmorning.config.GlobalConfig;
import com.goodmorning.utils.statusBar.StatusBarCompat;


public abstract class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    /**
     * 滑动起始占屏幕比例最小值
     */
    private static final float RATIO_PERCENT_BY_SCREEN = 0.05f;

    protected Context mContext;
    protected LayoutInflater mInflater;

    protected FrameLayout mDecorView;
    protected BaseExceptionView mExceptionView;

    protected BaseExceptionView.ITapReload tapReload = new BaseExceptionView.ITapReload() {
        @Override
        public void onTapReload() {
            tapReload();
        }
    };
    /**
     * 是否需要拦截此事件
     */
    protected boolean isShouldIntercept;
    private int screenWidth;//屏宽
    private float startX;
    private float startY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mInflater = getLayoutInflater();

        mDecorView = (FrameLayout) getWindow().getDecorView();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;

        setContentView(getLayoutId());
        initParentView();
        initNightModeView();
        initView();
        initData();
        //调整状态栏适配
        int statusBarColor = getStatusBarColor();
        if (statusBarColor != 0) {
            StatusBarCompat.setStatusBarColor(this, statusBarColor);
        }
    }

    protected int getStatusBarColor() {
        return Color.WHITE;
    }

    private void initNightModeView() {
        //调整状态栏适配
        getWindow().getDecorView().findViewById(android.R.id.content).setBackgroundColor(Color.WHITE);
    }

    private void initExceptionView() {
        mExceptionView = new BaseExceptionView(this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mDecorView.addView(mExceptionView, layoutParams);
        mExceptionView.setTapReload(tapReload);
        hideExceptionView();
    }

    /**
     * 展示tip
     */
    protected void showExceptionView() {
        mExceptionView.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏tip
     */
    protected void hideExceptionView() {
        mExceptionView.setVisibility(View.GONE);
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void initData();

    /**
     * 点击重新加载
     */
    protected void tapReload(){

    }

    @SuppressLint("NewApi")
    private void initParentView() {
        initExceptionView();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        StatisticPageDuration.enterActivity(getLocalClassName());
    }

    @Override
    protected void onPause() {
        super.onPause();
//        StatisticPageDuration.leaveActivity(getLocalClassName());
    }

    protected boolean supportSwipeBack() {
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (supportSwipeBack()) {
            float endX, endY, distanceX, distanceY;
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    // 是否滑动起始站屏幕比例最小值(若小于1/20则认为应该拦截)
                    isShouldIntercept = startX / screenWidth < RATIO_PERCENT_BY_SCREEN;
                    if (GlobalConfig.DEBUG) {
                        Log.d(TAG, "ACTION_DOWN  startX:" + startX + " ,startY:" + startY + "  ,startX / screenWidth :" + startX / screenWidth + " ,isShouldIntercept:" + isShouldIntercept);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    endX = event.getX();
                    endY = event.getY();
                    distanceX = endX - startX;
                    distanceY = Math.abs(endY - startY);
                    //1.判断手势右滑  2.横向滑动的距离要大于竖向滑动的距离
                    if (endX - startX > 0 && distanceY < distanceX) {
                        if (isShouldIntercept) {
                            mDecorView.setX(distanceX);
//                        mDecorView.scrollTo(-(int) distanceX, (int) mDecorView.getY());
                            if (GlobalConfig.DEBUG) {
                                Log.d(TAG, "ACTION_MOVE  distanceX:" + distanceX + " ,distanceY:" + distanceY + "  ---- ,endX:" + endX + " ,endY:" + endY + "  ,startX / screenWidth :" + startX / screenWidth);
                            }
                            //确定拦截此次事件
                            return true;
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //确定拦截当前事件时
                    endX = event.getX();
                    endY = event.getY();
                    distanceX = endX - startX;
                    distanceY = Math.abs(endY - startY);
                    if (isShouldIntercept) {
                        //1.判断手势右滑  2.横向滑动的距离要大于竖向滑动的距离 3.横向滑动距离大于屏幕三分之一才能finish
                        if (endX - startX > 0 && distanceY < distanceX && distanceX > screenWidth / 3) {
                            moveOn(distanceX);
                        }
                        //1.判断手势右滑  2.横向滑动的距离要大于竖向滑动的距离 但是横向滑动距离不够则返回原位置
                        else if (endX - startX > 0 && distanceY < distanceX) {
                            backOrigin(distanceX);
                        } else {
                            mDecorView.setX(0);
                        }
                        if (GlobalConfig.DEBUG) {
                            Log.d(TAG, "ACTION_UP  distanceX:" + distanceX + " ,distanceY:" + distanceY + "  ---- ,endX:" + endX + " ,endY:" + endY);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * 返回原点
     *
     * @param distanceX 横向滑动距离
     */
    private void backOrigin(float distanceX) {
        ObjectAnimator.ofFloat(mDecorView, "X", distanceX, 0).setDuration(300).start();
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "backOrigin  返回原点 横向滑动距离  distanceX:" + distanceX);
        }
    }

    /**
     * 划出屏幕
     *
     * @param distanceX 横向滑动距离
     */
    private void moveOn(float distanceX) {
        if (GlobalConfig.DEBUG) {
            Log.d(TAG, "moveOn  划出屏幕 横向滑动距离  distanceX:" + distanceX);
        }

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(distanceX, screenWidth);
        valueAnimator.setDuration(300);
        valueAnimator.start();

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mDecorView.setX((Float) animation.getAnimatedValue());
//                mDecorView.scrollTo((int) animation.getAnimatedValue(), (int) mDecorView.getY());
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                finish();
            }
        });
    }
}
