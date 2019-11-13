package com.k.permission;

/**
 * Created by libo on 2018/3/17.
 */
public interface CheckCallback {
    /** 用户拒绝权限申请 */
    void onDeny(String permission, boolean willShowDialog);
    /** 用户同意权限申请 */
    void onGranted(String permission);
    /** 全部申请结束 */
    void onFinish();

    void onSetting(PermissionItem item);
}