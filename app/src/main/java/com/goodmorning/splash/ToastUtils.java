package com.goodmorning.splash;

import android.widget.Toast;

/**
 * Created by erictjitra on 2016/12/26.
 */

public class ToastUtils {

    /**
     * Safe method to show a toast
     *
     * @param toast Toast to show
     */
    public static void showToast(Toast toast) {
        if (toast == null)
            return;

        try {
            toast.show();
        } catch (Throwable throwable) {
        }
    }
}
