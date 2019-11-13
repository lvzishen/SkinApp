package com.ads.lib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.IntDef;

public class Covers {

    private static final boolean DEBUG = true;

    private static Handler mH = new Handler(Looper.getMainLooper());
    private static Runnable showTask;
    private static Runnable dismissTask;


    public static final class Style {

        public static final int TOP = 1;
        public static final int CENTER = 2;
        public static final int BOTTOM = 3;

        public static final int IMMORTAL_COVER = -1;

        private int location = TOP;

        private float scaleWidht = 1.0f;

        private float scaleHeight = 0.5f;

        public Style(Builder builder) {
            this.location = builder.location;
            this.scaleWidht = builder.scaleWidht;
            this.scaleHeight = builder.scaleHeight;
        }

        @IntDef({TOP, CENTER, BOTTOM})
        public @interface Location {
        }


        public static class Builder {
            private int location = TOP;

            private float scaleWidht = 1.0f;

            private float scaleHeight = 0.5f;

            public Builder(@Location int location) {
                this.location = location;
            }

            public Builder setScaleWidht(float scaleWidht) {
                this.scaleWidht = scaleWidht;
                return this;
            }

            public Builder setScaleHeight(float scaleHeight) {
                this.scaleHeight = scaleHeight;
                return this;
            }

            public Style build() {
                return new Style(this);
            }

        }

    }


    public static void coverView2Window(final Activity activity, final Style style, long delayShowTime, long delayDismissTime) {
        boolean isIllegalTime = timeCheck(delayShowTime, delayDismissTime);
        if (isIllegalTime) {
            return;
        }
        final View[] coverView = new View[1];
        showTask = new Runnable() {
            @Override
            public void run() {
                coverView[0] = coverView2Window(activity, style);
            }
        };
        mH.postDelayed(showTask, delayShowTime);
        dismissTask = new Runnable() {
            @Override
            public void run() {
                if (coverView[0] != null) {
                    removeCoverViewFromWindow(activity, coverView[0]);
                }
            }
        };
        if (delayDismissTime != Style.IMMORTAL_COVER) {
            mH.postDelayed(dismissTask, delayDismissTime);
        }
    }


    /**
     * @param delayShowTime
     * @param delayDismissTime
     * @return true Illegal time period
     */
    private static boolean timeCheck(long delayShowTime, long delayDismissTime) {
        if (delayDismissTime == Style.IMMORTAL_COVER) {
            return false;
        }
        if (delayDismissTime < delayShowTime) {
            if (DEBUG) {
                illegalArgument();
            }
            return true;
        }
        return false;
    }

    private static void illegalArgument() {
        throw new IllegalArgumentException("Please check your time range");
    }


    public static View coverView2Window(Activity activity, Style style) {
        final WindowManager wm = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        View coverView = generateCoverView(activity);
        Rect displayArea = generateDisplayArea(activity, style,wm);
        WindowManager.LayoutParams lp = generateLayoutParam(displayArea);
        if (wm!=null) {
            wm.addView(coverView, lp);
        }
        return coverView;
    }

    private static void removeCoverViewFromWindow(Context context, View coverView) {
        final WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm!=null) {
            wm.removeView(coverView);
        }
    }

    private static Rect generateDisplayArea(Context context,Style style,WindowManager wm) {
        int screenWidth = getScreenWidth(wm);
        int screenHeight = getScreenHeight(wm);
        int width = (int) (screenWidth * style.scaleWidht);
        int height = (int) (screenHeight * style.scaleHeight);

        int left = 0;
        int top = 0;
        int right = width;
        switch (style.location) {
            case Style.CENTER:
                top = (screenHeight - height) / 2;
                break;
            case Style.BOTTOM:
                top = (screenHeight - height);
                break;

        }
        int bottom = top + height;
        return new Rect(left, top, right, bottom);
    }



    private static int getScreenWidth(WindowManager wm) {

        return wm.getDefaultDisplay().getWidth();
    }

    private static int getScreenHeight(WindowManager wm) {
        return wm.getDefaultDisplay().getHeight();
    }

    private static View generateCoverView(Context context) {
        final View coverView = new View(context);
        return coverView;
    }

    private static WindowManager.LayoutParams generateLayoutParam(Rect rect) {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.format = PixelFormat.TRANSLUCENT;
        lp.gravity = Gravity.TOP;
        lp.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        lp.width = rect.width();
        lp.height = rect.height();
        lp.x = rect.left;
        lp.y = rect.top;
        return lp;
    }

    public static void destroy(){
        mH.removeCallbacks(showTask);
        mH.removeCallbacks(dismissTask);
    }


}
