package com.k.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * 权限申请
 * Created by libo on 2018/3/17.
 */
public class PermissionCheck {

    /**
     * 申请单个权限
     *
     * @param context
     * @param permission
     * @param callback
     */
    public static void requestPermission(Context context, String permission, CheckCallback callback) {
        if (TextUtils.isEmpty(permission)) {
            if (callback != null) {
                callback.onFinish();
            }

            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onGranted(permission);
                callback.onFinish();
            }
            return;
        }

        List<PermissionItem> permissions = new ArrayList<>();
        permissions.add(new PermissionItem(permission));

        PermissionRequest request = new PermissionRequest.Builder().permissions(permissions).build();
        requestPermission(context, request, callback);
    }

    /**
     * 申请多个权限
     *
     * @param context
     * @param title
     * @param msg
     * @param permissionItems
     * @param callback
     */
    public static void requestPermission(Context context, String title, String msg, List<PermissionItem> permissionItems, CheckCallback callback) {
        requestPermission(context, title, msg, true, permissionItems, callback);
    }

    /**
     * 申请多个权限，并指定是否在用户拒绝后显示用户引导
     *
     * @param context
     * @param title
     * @param msg
     * @param showSettingGuide
     * @param permissionItems
     * @param callback
     */
    public static void requestPermission(Context context, String title, String msg, boolean showSettingGuide, List<PermissionItem> permissionItems, CheckCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                if (permissionItems != null) {
                    for (PermissionItem item : permissionItems) {
                        callback.onGranted(item.permission);
                    }
                }

                callback.onFinish();
            }
            return;
        }

        PermissionRequest request = new PermissionRequest.Builder().title(title).msg(msg).showSettingGuide(showSettingGuide).permissions(permissionItems).build();
        requestPermission(context, request, callback);
    }

    /**
     * 申请权限
     *
     * @param context
     * @param request
     * @param callback
     */
    public static void requestPermission(Context context, PermissionRequest request, CheckCallback callback) {
        // 如果请求对象为空，直接返回
        if (request == null || request.permissions == null || request.permissions.size() <= 0) {
            if (callback != null) {
                callback.onFinish();
            }

            return;
        }

        Iterator<PermissionItem> itemIterator = request.permissions.iterator();
        while (itemIterator.hasNext()) {
            PermissionItem item = itemIterator.next();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermission(context, item.permission)) {
                if (callback != null) {
                    callback.onGranted(item.permission);
                }

                itemIterator.remove();
            }
        }

        if (request.permissions == null || request.permissions.size() <= 0) {
            if (callback != null) {
                callback.onFinish();
            }

            return;
        }

        startPermissionActivity(context, request, callback);
    }

    private static void startPermissionActivity(Context context, PermissionRequest request, CheckCallback callback) {
        String key = CallbackHolder.getInstance().put(callback);

        Intent intent = new Intent(context, PermissionCheckActivity.class);
        intent.putExtra(Constants.KEY_ACTIVITY_CALLBACK, key);
        intent.putExtra(Constants.KEY_PERMISSIONS, (Serializable) request.permissions);
        intent.putExtra(Constants.KEY_TITLE, request.title);
        intent.putExtra(Constants.KEY_MSG, request.msg);
        intent.putExtra(Constants.KEY_SHOW_SETTING_GUIDE, request.showSettingGuide);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    /**
     * 检查某项权限是否存在
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkPermission(Context context, String permission) {
        if (TextUtils.isEmpty(permission)) {
            throw new IllegalArgumentException("requested permission is null.");
        }

        int checkPermission = ContextCompat.checkSelfPermission(context, permission);
        if (checkPermission == PackageManager.PERMISSION_GRANTED) {
            return true;
        }

        return false;
    }

    /**
     * 检查多种权限是否都已经允许通过
     *
     * @param context
     * @param permissions
     * @return
     */
    public static boolean checkPermission(Context context, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            throw new IllegalArgumentException("requested permission is null.");
        }
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}