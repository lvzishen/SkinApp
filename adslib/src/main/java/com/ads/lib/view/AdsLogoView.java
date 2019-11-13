package com.ads.lib.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;


/**
 * Created by yeguolong on 17-6-19.
 */
public class AdsLogoView extends TextView {

    public static final int DEFAULT_TEXT_COLOR = Color.parseColor("#55000000");
    public static final int DEFAULT_BG_COLOR = Color.TRANSPARENT;
    protected int mLogoTextColor = DEFAULT_TEXT_COLOR;
    protected int mLogoBgColor = DEFAULT_BG_COLOR;
    protected Drawable mLogoBg;

    public AdsLogoView(Context context) {
        this(context, null);
    }

    public AdsLogoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AdsLogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if (TextUtils.isEmpty(getText())) {
            setText("AD");
        }

        setGravity(Gravity.CENTER);
        setTextColor(mLogoTextColor);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
        setBackgroundColor(mLogoBgColor);

    }

    public int getLogoTextColor() {
        return mLogoTextColor;
    }

    public int getLogoBgColor() {
        return mLogoBgColor;
    }

}
