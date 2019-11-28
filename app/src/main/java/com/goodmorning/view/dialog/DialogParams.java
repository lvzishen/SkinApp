package com.goodmorning.view.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.creativeindia.goodmorning.R;

public final class DialogParams {

    public boolean cancelable;
    public DialogInterface.OnCancelListener mCancelListener;
    public int themeResId = R.style.alpha_Dialog;

    public Context context;
    public String title;
    public String content;

    /**
     * Set the TextView's text alignment.
     *
     */
    public int contentAlign = TextView.TEXT_ALIGNMENT_CENTER;

    /**
     * for single button
     */
    public ConfirmDialogClickListener confirmDialogListener;
    public String confirmStr;

    /**
     * for double button
     */
    public CommonDialogClickListener commonDialogListener;
    public String leftBtnStr;
    public String rightBtnStr;

    public String bigTitle;
    public float titleTextsize;
}
