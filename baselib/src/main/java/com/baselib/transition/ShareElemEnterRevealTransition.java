package com.baselib.transition;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * 页面转场动画进入时的揭露动画
 *
 * @author yangweining
 * 2019/9/24
 */
public class ShareElemEnterRevealTransition extends Transition {
    private static final String TAG = "ShareElemEnterRevealTransition";

    private static final String PROPNAME_RADIUS = "custom_reveal:change_radius:radius";

    private boolean hasAnim = false;

    private View animView;

    public ShareElemEnterRevealTransition(View animView) {
        this.animView = animView;
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_RADIUS, 0);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        float widthSquared = view.getWidth() * view.getWidth();
        float heightSquared = view.getHeight() * view.getHeight();
        int radius = (int) Math.sqrt(widthSquared + heightSquared) / 2;
        transitionValues.values.put(PROPNAME_RADIUS, radius);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot, TransitionValues startValues, TransitionValues endValues) {

        if (null == startValues || null == endValues) {
            return null;
        }
        final View view = endValues.view;
        int startRadius = (int) startValues.values.get(PROPNAME_RADIUS);
        int endRadius = (int) endValues.values.get(PROPNAME_RADIUS);
        if (view == animView) {
            Animator reveal = createAnimator(view, startRadius, endRadius);
            hasAnim = true;
            return reveal;
        }

        return null;
    }

    private Animator createAnimator(View view, float startRadius, float endRadius) {
        int centerX = view.getWidth() / 2;
        int centerY = view.getHeight() / 2;
        Animator reveal = ViewAnimationUtils.createCircularReveal(view, centerX, centerY,
                startRadius, endRadius);
        return new NoPauseAnimator(reveal);
    }


    public static class NoPauseAnimator extends Animator {
        private final Animator mAnimator;
        private final ArrayMap<AnimatorListener, AnimatorListener> mListeners =
                new ArrayMap<AnimatorListener, AnimatorListener>();

        public NoPauseAnimator(Animator animator) {
            mAnimator = animator;
        }

        @Override
        public void addListener(AnimatorListener listener) {
            AnimatorListener wrapper = new AnimatorListenerWrapper(this, listener);
            if (!mListeners.containsKey(listener)) {
                mListeners.put(listener, wrapper);
                mAnimator.addListener(wrapper);
            }
        }

        @Override
        public void cancel() {
            mAnimator.cancel();
        }

        @Override
        public void end() {
            mAnimator.end();
        }

        @Override
        public long getDuration() {
            return mAnimator.getDuration();
        }

        @Override
        public TimeInterpolator getInterpolator() {
            return mAnimator.getInterpolator();
        }

        @Override
        public ArrayList<AnimatorListener> getListeners() {
            return new ArrayList<AnimatorListener>(mListeners.keySet());
        }

        @Override
        public long getStartDelay() {
            return mAnimator.getStartDelay();
        }

        @Override
        public boolean isPaused() {
            return mAnimator.isPaused();
        }

        @Override
        public boolean isRunning() {
            return mAnimator.isRunning();
        }

        @Override
        public boolean isStarted() {
            return mAnimator.isStarted();
        }

        @Override
        public void removeAllListeners() {
            mListeners.clear();
            mAnimator.removeAllListeners();
        }

        @Override
        public void removeListener(AnimatorListener listener) {
            AnimatorListener wrapper = mListeners.get(listener);
            if (wrapper != null) {
                mListeners.remove(listener);
                mAnimator.removeListener(wrapper);
            }
        }

        /* We don't want to override pause or resume methods because we don't want them
         * to affect mAnimator.
        public void pause();

        public void resume();

        public void addPauseListener(AnimatorPauseListener listener);

        public void removePauseListener(AnimatorPauseListener listener);
        */

        @Override
        public Animator setDuration(long durationMS) {
            mAnimator.setDuration(durationMS);
            return this;
        }

        @Override
        public void setInterpolator(TimeInterpolator timeInterpolator) {
            mAnimator.setInterpolator(timeInterpolator);
        }

        @Override
        public void setStartDelay(long delayMS) {
            mAnimator.setStartDelay(delayMS);
        }

        @Override
        public void setTarget(Object target) {
            mAnimator.setTarget(target);
        }

        @Override
        public void setupEndValues() {
            mAnimator.setupEndValues();
        }

        @Override
        public void setupStartValues() {
            mAnimator.setupStartValues();
        }

        @Override
        public void start() {
            mAnimator.start();
        }
    }

    public static class AnimatorListenerWrapper implements Animator.AnimatorListener {
        private final Animator mAnimator;
        private final Animator.AnimatorListener mListener;

        public AnimatorListenerWrapper(Animator animator, Animator.AnimatorListener listener) {
            mAnimator = animator;
            mListener = listener;
        }

        @Override
        public void onAnimationStart(Animator animator) {
            mListener.onAnimationStart(mAnimator);
        }

        @Override
        public void onAnimationEnd(Animator animator) {
            mListener.onAnimationEnd(mAnimator);
        }

        @Override
        public void onAnimationCancel(Animator animator) {
            mListener.onAnimationCancel(mAnimator);
        }

        @Override
        public void onAnimationRepeat(Animator animator) {
            mListener.onAnimationRepeat(mAnimator);
        }
    }

    // setLeftTopRightBottom 需要反射执行, 该方法能够控制 View 的大小以及位置 在 ChangeBounds 类中有调用
    private void relfectInvoke(View view, int left, int top, int right, int bottom) {

        Class clazz = view.getClass();
        try {
            Method m1 = clazz.getMethod("setLeftTopRightBottom", new Class[]{int.class, int.class, int.class, int.class});
            m1.invoke(view, left, top, right, bottom);
        } catch (Exception e) {
            e.printStackTrace();

            // 5.0版本  没有 setLeftTopRightBottom 这个方法  使用一下方法 ,额 其实 5.0 以上也可以用这些方法?
            view.setLeft(left);
            view.setRight(right);
            view.setTop(top);
            view.setBottom(bottom);
        }

    }
}
