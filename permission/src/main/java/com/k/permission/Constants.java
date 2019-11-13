package com.k.permission;

/**
 * Created by libo on 2018/3/17.
 */
public class Constants {
    /** 权限请求的标题 */
    public static final String KEY_TITLE = "key_title";
    /** 权限请求的提示内容 */
    public static final String KEY_MSG = "key_msg";
    /** 权限列表 */
    public static final String KEY_PERMISSIONS = "key_permissions";
    /** 权限请求的类型 */
    public static final String KEY_REQUEST_TYPE = "key_request_type";
    /** 用于获取callback的KEY */
    public static final String KEY_ACTIVITY_CALLBACK = "key_activity_callback";
    /** 是否展示挽留弹窗 */
    public static final String KEY_SHOW_STAY_WINDOW = "key_show_stay_window";
    /** 没有弹出框的情况下展示用户引导 */
    public static final String KEY_SHOW_SETTING_GUIDE = "key_show_setting_guide";

    /** 单个权限申请 */
    public static int PERMISSION_TYPE_SINGLE = 1;
    /** 多个权限申请 */
    public static int PERMISSION_TYPE_MUTI = 2;

}
