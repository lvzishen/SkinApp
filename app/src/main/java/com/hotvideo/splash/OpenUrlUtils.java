package com.hotvideo.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


public class OpenUrlUtils {

    public static void openDefaultBrower(Context context, String url) {
        try {//防止没有手机没有浏览器引发的崩溃
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            if (!(context instanceof Activity)) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        } catch (Exception e1) {
            ToastUtils.showToast(
                    Toast.makeText(context, com.cleanerapp.supermanager.baselib.R.string.no_browser_installed, Toast.LENGTH_SHORT)
            );
            e1.printStackTrace();
        }
    }

}
