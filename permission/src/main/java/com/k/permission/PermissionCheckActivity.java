package com.k.permission;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * Created by libo on 2018/3/19.
 */
public class PermissionCheckActivity extends AppCompatActivity {
    public static final boolean DEBUG = Configs.DEBUG;
    public static final String TAG = "PermissionCheckActivity";

    private static final int REQUEST_CODE_SINGLE = 0x101;
    private static final int REQUEST_CODE_MUTI = 0x102;
    public static final int REQUEST_CODE_RETRY = 0x103;

    private String mTitle;
    private String mMsg;
    private CheckCallback mCallBack;
    private List<PermissionItem> mPermissions;
    private boolean mShowSettingGuide = true;

    private int mRePermissionIndex = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();

        if (mPermissions == null || mPermissions.size() <= 0) {
            finish();
            return;
        }

//        if (TextUtils.isEmpty(mTitle) || TextUtils.isEmpty(mMsg)) {
//            requestPermission(new String[]{mPermissions.get(0).permission}, REQUEST_CODE_SINGLE);
//        } else {
        // TODO 展示权限申请的引导
        requestPermission(getPermissionStrArray(mPermissions), REQUEST_CODE_MUTI);
//        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }

        mTitle = intent.getStringExtra(Constants.KEY_TITLE);
        mMsg = intent.getStringExtra(Constants.KEY_MSG);
        mPermissions = (List<PermissionItem>) intent.getSerializableExtra(Constants.KEY_PERMISSIONS);
        String key = intent.getStringExtra(Constants.KEY_ACTIVITY_CALLBACK);
        mCallBack = CallbackHolder.getInstance().get(key);
        mShowSettingGuide = intent.getBooleanExtra(Constants.KEY_SHOW_SETTING_GUIDE, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_SINGLE: {
                PermissionItem permissionItem = getPermissionItem(permissions[0]);
                if (permissionItem != null) {
                    String permission = getPermissionItem(permissions[0]).permission;
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        onGranted(permission);
                        finish();
                    } else {
                        onDeny(permission, PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, permissionItem.permission));
                        finish();

//                        if (permissionItem.needStayWindow) {
//                            showStayDialog(permissionItem);
//                        } else {
//                            if (!PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, permissionItem.permission) && mShowSettingGuide) {
//                                showSettingGuide(permissionItem);
//                            } else {
//                                onDeny(permission, true);
//                            }
//
//                            finish();
//                        }
                    }
                }
                break;
            }
            case REQUEST_CODE_MUTI: {
                for (int i = 0; i < grantResults.length; i++) {
                    PermissionItem item = getPermissionItem(permissions[i]);

                    //权限允许后，删除需要检查的权限
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        mPermissions.remove(item);
                        onGranted(permissions[i]);
                    } else {
//                        if (!item.needStayWindow && (!(!PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, item.permission) && mShowSettingGuide))) {
//                            mPermissions.remove(item);
//                            onDeny(item.permission);
//                        }
                        onDeny(item.permission, PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, item.permission));
                    }
                }

                if (mPermissions.size() > 0) {
                    mRePermissionIndex = 0;

                    PermissionItem permissionItem = mPermissions.get(mRePermissionIndex);
                    // 上面已经排除了这两种情况之外的情况
                    if (permissionItem.needStayWindow) {
                        showStayDialog(permissionItem);
                    } else if (!PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, permissionItem.permission) && mShowSettingGuide) {
                        showSettingGuide(permissionItem);
                    } else {
                        finish();
                    }
                } else {
                    //全部允许了
                    onFinish();
                }

                break;
            }
            case REQUEST_CODE_RETRY: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onGranted(permissions[0]);
                    if (mRePermissionIndex <= mPermissions.size() - 1) {
                        //继续申请下一个被拒绝的权限
                        retryNext();
                    } else {
                        //全部允许了
                        onFinish();
                    }
                } else {
                    onCancel();
                }
                break;
            }
        }
    }

    private void retryNext() {
        PermissionItem permissionItem = mPermissions.get(mRePermissionIndex);

        if (permissionItem.needStayWindow) {
            showStayDialog(permissionItem);
        } else if (!PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, permissionItem.permission) && mShowSettingGuide) {
            showSettingGuide(permissionItem);
        }
    }

    private void reApplyPermission(PermissionItem permissionItem) {
        if (!PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, permissionItem.permission) && mShowSettingGuide) {
            showSettingGuide(permissionItem);
            finish();
        } else {
            requestPermission(new String[]{mPermissions.get(0).permission}, REQUEST_CODE_RETRY);
            mRePermissionIndex++;
        }
    }

    private void showStayDialog(final PermissionItem permissionItem) {
        String permissionName = permissionItem.permissionName;

        String alertTitle = "";
        if (!TextUtils.isEmpty(permissionItem.stayTitle)) {
            alertTitle = permissionItem.stayTitle;
        } else {
            if (TextUtils.isEmpty(permissionName)) {
                alertTitle = "申请权限";
            } else {
                alertTitle = "申请" + permissionName;
            }
        }

        String msg = "拒绝权限申请会导致功能无法正常使用";
        if (!TextUtils.isEmpty(permissionItem.stayText)) {
            msg = permissionItem.stayText;
        }

        showAlertDialog(alertTitle, msg, "取消", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                reApplyPermission(permissionItem);
            }
        });
    }

//    private void showStayDialog(final int requestCode, final String permission) {
//        String permissionName = permission;
//        String alertTitle = "申请" + permission;
//        String msg = "拒绝权限申请会导致功能无法正常使用";
//        showAlertDialog(alertTitle, msg, "取消", "确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                requestPermission(new String[]{permission}, requestCode);
//            }
//        });
//    }

    private void showAlertDialog(String title, String msg, String cancelTxt, String PosTxt, DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(cancelTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        onCancel();
                    }
                })
                .setPositiveButton(PosTxt, onClickListener).create();
        alertDialog.show();
    }

    // 设置引导
    private void showSettingGuide(PermissionItem item) {
        if (mCallBack != null) {
            mCallBack.onSetting(item);
            if (!isFinishing()) {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void requestPermission(String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(PermissionCheckActivity.this, permissions, requestCode);
    }

    private void onCancel() {
        mRePermissionIndex = mRePermissionIndex - 1;
        if (mRePermissionIndex < 0) {
            mRePermissionIndex = 0;
        }

        for (int i = mRePermissionIndex; i < mPermissions.size(); i++) {
            onDeny(mPermissions.get(i).permission, PermissionUtils.willShowRequestDialog(PermissionCheckActivity.this, mPermissions.get(i).permission));
        }

        finish();
    }

    private void onFinish() {
        if (mCallBack != null) {
            mCallBack.onFinish();
        }

        finish();
    }

    private void onDeny(String permission, boolean willShowDialog) {
        if (mCallBack != null) {
            mCallBack.onDeny(permission, willShowDialog);
        }
    }

    private void onGranted(String permission) {
        if (mCallBack != null) {
            mCallBack.onGranted(permission);
        }
    }

    private PermissionItem getPermissionItem(String permission) {
        for (PermissionItem permissionItem : mPermissions) {
            if (permissionItem.permission.equals(permission))
                return permissionItem;
        }
        return null;
    }

    private String[] getPermissionStrArray(List<PermissionItem> permissionItems) {
        String[] str = new String[permissionItems.size()];
        for (int i = 0; i < permissionItems.size(); i++) {
            str[i] = permissionItems.get(i).permission;
        }

        return str;
    }

}
