package com.k.permission;

import android.app.Activity;

import androidx.core.app.ActivityCompat;

/**
 * Created by libo on 2018/3/21.
 */
public class PermissionUtils {


    /**
     * 是否还会展示权限申请对话框
     * @param activity
     * @param permission
     * @return
     */
    public static boolean willShowRequestDialog(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }
}
