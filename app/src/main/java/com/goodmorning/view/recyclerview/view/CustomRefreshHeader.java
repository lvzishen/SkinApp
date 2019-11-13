package com.goodmorning.view.recyclerview.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cleanerapp.supermanager.R;
import com.goodmorning.view.recyclerview.WeakHandler;
import com.goodmorning.view.recyclerview.interfaces.IRefreshHeader;

public class CustomRefreshHeader extends LinearLayout implements IRefreshHeader {

    private LinearLayout mContainer;
    private ImageView ivIcon;
    private TextView tvRefresh;

    public int mMeasuredHeight;
    private int mState = STATE_NORMAL;
    private WeakHandler mHandler = new WeakHandler();

    public CustomRefreshHeader(Context context) {
        super(context);
        initView();
    }

    public CustomRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView(){
        // 初始情况，设置下拉刷新view高度为0
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        this.setLayoutParams(lp);
        this.setPadding(0, 0, 0, 0);

        mContainer = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_head_refresh, null);
        addView(mContainer,new LayoutParams(LayoutParams.MATCH_PARENT, 0));
        setGravity(Gravity.BOTTOM);

        ivIcon = findViewById(R.id.iv_icon);
        tvRefresh = findViewById(R.id.tv_refresh);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        mMeasuredHeight = getMeasuredHeight();
    }

    public void setState(int state){
        if (state == mState){
            return;
        }

        if (state == STATE_REFRESHING){
            smoothScrollTo(mMeasuredHeight);
        }

        switch (state){
            case STATE_NORMAL:
                tvRefresh.setText("下拉刷新");
                break;
            case STATE_RELEASE_TO_REFRESH:
                if (mState != STATE_RELEASE_TO_REFRESH){
                    tvRefresh.setText("释放刷新");
                }
                break;
            case STATE_REFRESHING:
                tvRefresh.setText("正在刷新");
                break;
            case STATE_DONE:
                tvRefresh.setText("刷新完成");
                break;
        }
        mState = state;

    }

    private void smoothScrollTo(int destHeight) {
        ValueAnimator animator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                setVisibleHeight((int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LayoutParams lp = (LayoutParams) mContainer .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public void reset() {
        smoothScrollTo(0);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_NORMAL);
            }
        }, 500);
    }

    @Override
    public void onReset() {
        setState(STATE_NORMAL);
    }

    @Override
    public void onPrepare() {
        setState(STATE_RELEASE_TO_REFRESH);
    }

    @Override
    public void onRefreshing() {
        setState(STATE_REFRESHING);
    }

    @Override
    public void onMove(float offSet, float sumOffSet) {
        int top = getTop();
        if (offSet > 0 && top == 0){
            setVisibleHeight((int) (offSet + getVisibleHeight()));
        }else if (offSet < 0 && getVisibleHeight() > 0){
            //重新布局让head显示在顶端
            layout(getLeft(),0,getRight(),getHeight());
            setVisibleHeight((int) (offSet + getVisibleHeight()));
        }
        if (mState <= STATE_RELEASE_TO_REFRESH){
            if (getVisibleHeight() > mMeasuredHeight){
                onPrepare();
            }else {
                onReset();
            }
        }
    }

    @Override
    public boolean onRelease() {
        boolean isOnRefresh = false;
        int height = getVisibleHeight();
        if (height == 0){
            isOnRefresh = false;
        }

        if (getVisibleHeight() > mMeasuredHeight && mState < STATE_REFRESHING){
            setState(STATE_REFRESHING);
            isOnRefresh = true;
        }

        if (mState == STATE_REFRESHING && height > mMeasuredHeight){
            smoothScrollTo(mMeasuredHeight);
        }

        if (mState != STATE_REFRESHING){
            smoothScrollTo(0);
        }

        if (mState == STATE_REFRESHING){
            int destHeight = mMeasuredHeight;
            smoothScrollTo(destHeight);
        }

        return isOnRefresh;
    }

    @Override
    public void refreshComplete() {
        setState(STATE_DONE);
        mHandler.postDelayed(new Runnable(){
            @Override
            public void run() {
                reset();
            }
        }, 200);
    }

    @Override
    public View getHeaderView() {
        return this;
    }

    @Override
    public int getVisibleHeight() {
        LayoutParams lp = (LayoutParams) mContainer.getLayoutParams();
        return lp.height;
    }

    @Override
    public int getVisibleWidth() {
        return 0;
    }

    @Override
    public int getType() {
        return TYPE_HEADER_NORMAL;
    }
}
