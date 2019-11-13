package com.ads.lib.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import com.ads.lib.ModuleConfig;
import com.ads.lib.mediation.widget.AdIconView;
import com.ads.lib.mediation.widget.NativeMediaView;
import com.ads.lib.mediation.widget.NativeStaticViewHolder;
import com.ads.lib.prop.FastClickProp;
import com.mopub.nativeads.BaseNativeAd;

import java.util.ArrayList;
import java.util.List;

public class FastClickUtils {

    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = "FastClickUtils";

    private long mClickDelayTime = 1000;
    private long barrierTime = 0;
    private View barrierView = null;
    private Runnable mCoverRunnable = null;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());


    public void addCoverViewIfNeed(final NativeStaticViewHolder viewHolder, long clickDelayTime) {
        if (viewHolder.mainView == null) {
            return;
        }
        mClickDelayTime = clickDelayTime;
        if (mClickDelayTime <= 0L) {
            if (DEBUG) {
                Log.d(TAG, "mClickDelayTime = 0, Do not need barrierView");
            }
            return;
        }
        barrierTime = System.currentTimeMillis();
        if (DEBUG) {
            Log.d(TAG, "putBarrier barrierView barrierTime = $barrierTime");
        }
        final ViewGroup adMainViewParent = (ViewGroup) viewHolder.mainView;
        try {
            float elevation = 0;
            if (barrierView != null) {
                barrierView.setClickable(false);
                adMainViewParent.removeView(barrierView);

                if (barrierView.getParent() != null) {
                    ViewGroup bvRoot = (ViewGroup) barrierView.getParent();
                    bvRoot.removeView(barrierView);
                }
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    elevation = barrierView.getElevation();
                }
                if (DEBUG) {
                    Log.d(TAG, "barrierView !=null,elevation = " + elevation);
                }
            }

            barrierView = new View(adMainViewParent.getContext());
            setBarrierViewElevation(adMainViewParent, elevation);
            setupBarrierView(adMainViewParent);
            removeBarrierViewDelay(adMainViewParent);
        } catch (Exception e) {

            if (DEBUG) {
                Log.e(TAG, "addCoverViewIfNeed error", e);
            }

            if (mMainHandler != null) {
                mMainHandler.removeCallbacksAndMessages(null);
            }


            if (barrierView != null) {
                adMainViewParent.removeView(barrierView);
            }

            if (mCoverRunnable != null) {
                adMainViewParent.removeCallbacks(mCoverRunnable);
            }
        }

    }

    private void setupBarrierView(ViewGroup adMainViewParent) {
        barrierView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DEBUG) {
                    Log.d(TAG, "barrierView onClick");
                }
            }
        });

        if (barrierView != null) {
            if (DEBUG) {
                Log.d(TAG, "adMainView width == " + adMainViewParent.getWidth());
                Log.d(TAG, "adMainView height == " + adMainViewParent.getHeight());
            }

            if (mCoverRunnable != null) {
                adMainViewParent.removeCallbacks(mCoverRunnable);
            }

            mCoverRunnable = new CoverRunnable(adMainViewParent, barrierView);
            adMainViewParent.post(mCoverRunnable);
        }
    }

    private void removeBarrierViewDelay(final ViewGroup adMainViewParent) {
        mMainHandler.removeCallbacksAndMessages(null);
        mMainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DEBUG) {
                    long currentTimeMillis = System.currentTimeMillis();
                    Log.d(TAG, "removeView barrierView currentTimeMillis = " + currentTimeMillis
                            + "; barrierLastingTime = " + (currentTimeMillis - barrierTime));
                }
                if (barrierView != null) {
                    adMainViewParent.removeView(barrierView);
                }
            }
        }, mClickDelayTime);
    }

    private void setBarrierViewElevation(ViewGroup adMainViewParent, float elevation) {
        if (elevation == 0) {
            elevation = ElevationUtils.getMaxElevation(adMainViewParent);
        }
        //为什么要设置这个呢，这可是一个大坑，如果容器中有设置了elevation，新增加的子view会显示在下层。
        //详见链接 https://www.jianshu.com/p/c1d17a39bc09
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            barrierView.setElevation(elevation);

        }
    }


    private static class CoverRunnable implements Runnable {

        private ViewGroup mainView;
        private View barrierView;

        public CoverRunnable(ViewGroup mainView, View barrierView) {
            this.mainView = mainView;
            this.barrierView = barrierView;
        }

        @Override
        public void run() {
            if (DEBUG) {

                Log.e(TAG, "adMainView POST width == " + mainView.getWidth());
                Log.e(TAG, "adMainView POST height == " + mainView.getHeight());
            }

            if (barrierView != null) {
                mainView.removeView(barrierView);
                if (barrierView.getParent() == null) {
                    try {
                        if (mainView.getWidth() == 0 || mainView.getHeight() == 0) {
                            mainView.addView(barrierView, mainView.getLayoutParams());
                        } else {
                            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(mainView.getWidth(), mainView.getHeight());
                            mainView.addView(barrierView, lp);
                        }
                    } catch (Throwable throwable) {
                    }
                }

            }
        }
    }


    public void detachCoverView() {
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (barrierView != null) {
            ViewParent parent = barrierView.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(barrierView);
            }
            barrierView = null;
            //移除
        }
    }


    public static List<View> getClickableViews(Context context, NativeStaticViewHolder viewHolder, BaseNativeAd baseNativeAd, String unitId) {
        ArrayList<View> views = new ArrayList<>();
        int clickAreaStrategy = FastClickProp.getInstance(context.getApplicationContext()).getClickAreaStrategy(unitId);
        ImageView mainImageView = viewHolder.mainImageView;
        NativeMediaView mediaView = viewHolder.mediaView;
        ViewGroup adChoiceViewGroup = viewHolder.adChoiceViewGroup;
        TextView callToActionView = viewHolder.callToActionView;
        AdIconView iconImageView = viewHolder.iconImageView;
        TextView textView = viewHolder.textView;
        TextView titleView = viewHolder.titleView;
        if (!isNeedIntercepClick(context, baseNativeAd)) {
            clickAreaStrategy = FastClickProp.TYPE_CAN_CLICK_TOTAL;
        }
        switch (clickAreaStrategy) {
            case FastClickProp.TYPE_CAN_CLICK_TOTAL:
            default:
                addIfNotNull(views, mainImageView);
                addIfNotNull(views, mediaView);
                addIfNotNull(views, adChoiceViewGroup);
                addIfNotNull(views, callToActionView);
                addIfNotNull(views, iconImageView);
                addIfNotNull(views, textView);
                addIfNotNull(views, titleView);
                break;
            case FastClickProp.TYPE_CAN_CLICK_TOP:
                addIfNotNull(views, adChoiceViewGroup);
                addIfNotNull(views, mediaView);
                break;
            case FastClickProp.TYPE_CAN_CLICK_BOTTOM:
                addIfNotNull(views, titleView);
                addIfNotNull(views, textView);
                addIfNotNull(views, iconImageView);
                addIfNotNull(views, callToActionView);
                break;
            case FastClickProp.TYPE_CAN_CLICK_BUTTON:
                addIfNotNull(views, callToActionView);
                break;
        }

        return views;
    }

    public static void clearOldCoverView(View targetView) {
        if (targetView == null) {
            return;
        }
        ViewParent parent = targetView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup parentView = (ViewGroup) parent;
            View cover = parentView.findViewWithTag("cover");
            if (cover != null) {
                parentView.removeView(cover);
            }

        }
    }

    public static void coverView(View targetView) {
        if (targetView == null) {
            return;
        }
        int top = targetView.getTop();
        int left = targetView.getLeft();
        int width = targetView.getWidth();
        int height = targetView.getHeight();
        View coverView = new View(targetView.getContext());
        ViewParent parent = targetView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(width, height);
            layoutParams.topMargin = top;
            layoutParams.leftMargin = left;
            coverView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
            coverView.setTag("cover");
            coverView.setLayoutParams(layoutParams);
            ((ViewGroup) parent).addView(coverView);
        }

    }

    public static void resetInterceptTouchEvent(View targetView) {
        if (targetView == null) {
            return;
        }
        ViewParent parent = targetView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).setOnTouchListener(null);
        }

    }

    public static void interceptTouchEvent(final View targetView) {
        if (targetView == null) {
            return;
        }
        ViewParent parent = targetView.getParent();
        if (parent != null && parent instanceof ViewGroup) {
            ((ViewGroup) parent).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (v == targetView) {
                        return true;
                    }
                    return false;
                }
            });

        }

    }


    private static void addIfNotNull(List<View> views, View clickView) {
        if (clickView != null) {
            views.add(clickView);
        }
    }

    public static boolean isNeedIntercepClick(Context context, BaseNativeAd baseNativeAd) {
        String unClickableAreaSource = FastClickProp.getInstance(context.getApplicationContext()).getUnClickableAreaSource();
        if (!TextUtils.isEmpty(unClickableAreaSource)) {
            if (unClickableAreaSource.contains("an") && isFacebook(baseNativeAd)) {
                return true;
            } else if (unClickableAreaSource.contains("ab") && isAdmob(baseNativeAd)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isAdmob(BaseNativeAd baseNativeAd) {
        return baseNativeAd instanceof com.mopub.nativeads.GooglePlayServicesNative.GooglePlayServicesNativeAd;
    }

    private static boolean isFacebook(BaseNativeAd baseNativeAd) {
        return baseNativeAd instanceof com.mopub.nativeads.FacebookNative.FacebookVideoEnabledNativeAd;
    }

}
