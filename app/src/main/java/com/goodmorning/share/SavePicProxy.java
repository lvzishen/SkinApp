package com.goodmorning.share;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import com.baselib.sp.SharedPref;
import com.creativeindia.goodmorning.R;
import com.goodmorning.ui.activity.BaseDetailActivity;
import com.k.permission.CheckCallback;
import com.k.permission.IPermissionSettingResult;
import com.k.permission.PermissionCheck;
import com.k.permission.PermissionItem;
import com.k.permission.PermissionRequest;
import com.k.permission.PermissionSettingManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建日期：2019/11/26 on 11:11
 * 描述:
 * 作者: lvzishen
 */
public class SavePicProxy {
    public static final String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int SAVE_TYPE = 1;
    public static final int INS_TYPE = 2;
    public static final int SAVE_MORE = 3;

    private ISaveImage saveImage;

    public void savePic(Activity context, String picName, Bitmap bitmap, int type) {
        switch (type) {
            case SAVE_TYPE:
                saveImage = new SaveImageImpl();
                break;
            case INS_TYPE:
                saveImage = RInstagramManager.getInstance();
                break;
            case SAVE_MORE:
                saveImage = ShareManager.getInstance();
                break;
        }
        Object proxyInstance = Proxy.newProxyInstance(saveImage.getClass().getClassLoader(), new Class[]{ISaveImage.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !PermissionCheck.checkPermission(context, PERMISSIONS_STORAGE)) {
                    final List<PermissionItem> list = new ArrayList<>();
                    PermissionItem permissionItem = new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, context.getString(R.string.string_storage), 0);
                    permissionItem.needStayWindow = false;
                    list.add(permissionItem);
                    PermissionRequest permissionRequest = new PermissionRequest.Builder().title("").msg("").showSettingGuide(true).permissions(list).build();
                    PermissionCheck.requestPermission(context, permissionRequest, new CheckCallback() {
                        @Override
                        public void onDeny(String permission, boolean willShowDialog) {
                            if (context.isFinishing()) {
                                return;
                            }
                            if (willShowDialog) {
                                Toast.makeText(context.getApplicationContext(), context.getString(R.string.usage_access_permission_fail_toast), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onGranted(String permission) {
                            if (context.isFinishing()) {
                                return;
                            }
                            try {
                                method.invoke(saveImage, context, picName, bitmap);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onSetting(PermissionItem item) {
                            if (context.isFinishing()) {
                                return;
                            }
                            if (SharedPref.getBoolean(context.getApplicationContext(), SharedPref.SP_KEY_SHOW_STORAGE_SETTING_GUIDE, false)) {
                                //registerOpWatcher();
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                                PermissionSettingManager.getInstance().startCheckPermission(context.getApplicationContext(), PERMISSIONS_STORAGE, new IPermissionSettingResult() {
                                    @Override
                                    public void onPermissionIsGrant(String[] permissions) {
                                        try {
                                            method.invoke(saveImage, context, picName, bitmap);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        } catch (InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                SharedPref.setBoolean(context.getApplicationContext(), SharedPref.SP_KEY_SHOW_STORAGE_SETTING_GUIDE, true);
                                Toast.makeText(context.getApplicationContext(), context.getString(R.string.usage_access_permission_fail_toast), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                } else {
                    method.invoke(saveImage, context, picName, bitmap);
                }
                return null;
            }
        });
        ISaveImage instance = (ISaveImage) proxyInstance;
        instance.saveImage(context, picName, bitmap);
    }

}
