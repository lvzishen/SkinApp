package com.k.permission;

import java.io.Serializable;

/**
 * Created by libo on 2018/3/17.
 */
public class PermissionItem  implements Serializable {
    /** 权限，比如：Manifest.permission.CALL_PHONE */
    public String permission;
    /** 用于申请权限的名称 */
    public String permissionName;
    /** 用于申请权限的图标 */
    public int permissionRes;
    /** 用于挽留权限申请的标题 */
    public String stayTitle;
    /** 用于挽留权限申请的内容 */
    public String stayText;
    /** 是否需要挽留弹窗， 默认是需要的 */
    public boolean needStayWindow = true;

    public PermissionItem(String permission) {
        this.permission = permission;
    }

    public PermissionItem(String permission, String permissionName, int permissionIconRes) {
        this.permission = permission;
        this.permissionName = permissionName;
        this.permissionRes = permissionIconRes;
    }

    public PermissionItem(String permission, String permissionName, int permissionIconRes, String stayTitle, String stayText) {
        this.permission = permission;
        this.permissionName = permissionName;
        this.permissionRes = permissionIconRes;
        this.stayTitle = stayTitle;
        this.stayText = stayText;
    }

}
