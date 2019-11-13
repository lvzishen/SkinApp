package com.baselib.ui.activity;

import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.baselib.transition.ChangeColor;
import com.baselib.transition.ShareElemEnterRevealTransition;
import com.baselib.transition.ShareElemReturnRevealTransition;

/**
 * 页面转场动画基类
 *
 * @author yangweining
 * 2019/9/24
 */
public abstract class BaseTransitionActivity extends BaseEventLoggerActivity {
    public static final String TRANSITION_NAME = "transition_comment";
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != -1) {
            setContentView(getLayoutId());
        }
        root = findViewById(getRootId());
        ViewCompat.setTransitionName(root, TRANSITION_NAME);
        setTransition();
    }

    public abstract int getLayoutId();

    public abstract int getRootId();

    public abstract View getTitleView();

    public abstract int getStartColor();

    public abstract int getEndColor();


    private void setTransition() {
        // 顶部 title 和底部输入框的进入动画
//        getWindow().setEnterTransition(new CommentEnterTransition(this, mTitleBarTxt, mBottomSendBar));

        getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
        getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());

    }

    /**
     * 分享 元素 进入动画
     *
     * @return
     */
    private TransitionSet buildShareElemEnterSet() {
        TransitionSet transitionSet = new TransitionSet();

//        Transition changePos = new ChangePosition();
//        changePos.setDuration(200);
//        changePos.addTarget(getRootId());
//        transitionSet.addTransition(changePos);

        Transition revealTransition = new ShareElemEnterRevealTransition(root);
        transitionSet.addTransition(revealTransition);
        revealTransition.addTarget(getRootId());
//        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        revealTransition.setDuration(200);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(getStartColor()), getResources().getColor(getEndColor()));
        changeColor.addTarget(getRootId());
        changeColor.setDuration(100);

        transitionSet.addTransition(changeColor);

        transitionSet.setDuration(300);

        return transitionSet;
    }

    /**
     * 分享元素返回动画
     *
     * @return
     */
    private TransitionSet buildShareElemReturnSet() {
        TransitionSet transitionSet = new TransitionSet();

//        Transition changePos = new ShareElemReturnChangePosition();
//        changePos.addTarget(getRootId());
//        changePos.setDuration(200);
//        transitionSet.addTransition(changePos);

        Transition revealTransition = new ShareElemReturnRevealTransition(root);
        revealTransition.addTarget(getRootId());
        revealTransition.setDuration(200);
        transitionSet.addTransition(revealTransition);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(getEndColor()), getResources().getColor(getStartColor()));
        changeColor.addTarget(getRootId());
        changeColor.setDuration(200);
        transitionSet.addTransition(changeColor);


        transitionSet.setDuration(400);

        return transitionSet;
    }

    @Override
    public void onBackPressed() {
        if (getTitleView() != null) {
            getTitleView().setVisibility(View.INVISIBLE);
        }
        super.onBackPressed();
    }

}
