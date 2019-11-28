package com.goodmorning.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ads.lib.util.PageUtil;

import java.io.Serializable;

import static org.interlaken.common.impl.BaseXalContext.getApplicationContext;

/**
 * Created by hao on 2018/3/27.
 */

public class ActivityCtrl {
    public static final String TRANSFER_DATA = "transfer_data";
    public static final String KEY_LOGIN_EXTRA = "key_login_extra";
    /**
     * 跳转到指定的 {@link Activity}。
     */
    public static void gotoActivitySimple(Context context, Class<? extends Activity> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        context.startActivity(intent);
        ((Activity) context).finish();

        if (context == null) {
            context = getApplicationContext();
            context.startActivity(intent);
        } else {
            context.startActivity(intent);
            ((Activity) context).finish();
        }
    }

    /**
     * 跳转并清空其他所有activity
     */
    public static void gotoActivityWithClear(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);

    }


    /**
     * 跳转到指定的 {@link Activity}。
     */
    public static void gotoActivityOpenSimple(
            Context context, Class<? extends Activity> activityClass) {
        Intent intent = new Intent(getApplicationContext(), activityClass);
        context.startActivity(intent);
    }

    public static void gotoOpenActivity(Context context, Class<? extends Activity> activityClass, Serializable obj){
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.putExtra(TRANSFER_DATA,obj);
        context.startActivity(intent);
    }

    public static void gotoSettingAcitivity(Context context, Class<? extends Activity> activityClass, boolean isLogin){
        Intent intent = new Intent(getApplicationContext(), activityClass);
        intent.putExtra(KEY_LOGIN_EXTRA,isLogin);
        context.startActivity(intent);
    }

}
