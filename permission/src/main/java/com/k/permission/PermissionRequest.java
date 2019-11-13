package com.k.permission;

import java.util.List;

/**
 * Created by libo on 2018/3/19.
 */
public class PermissionRequest {
    /** 提示开启权限的标题 */
    public String title;
    /** 提示开启权限的内容 */
    public String msg;
    /** 在不展示权限申请对话框后，是否显示开启权限引导 */
    public boolean showSettingGuide = true;
    public List<PermissionItem> permissions;

    public PermissionRequest(Builder builder) {
        this.title = builder.title;
        this.msg = builder.msg;
        this.showSettingGuide = showSettingGuide;
        this.permissions = builder.permissions;
    }

    public static class Builder {
        private String title;
        private String msg;
        private boolean showSettingGuide = true;
        private List<PermissionItem> permissions;

        public Builder() {

        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder msg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder showSettingGuide(boolean showSettingGuide) {
            this.showSettingGuide = showSettingGuide;
            return this;
        }

        public Builder permissions(List<PermissionItem> permissions) {
            this.permissions = permissions;
            return this;
        }

        public PermissionRequest build() {
            if(permissions == null || permissions.size() <= 0) {
                throw new IllegalStateException("no request permission.");
            }

            return new PermissionRequest(this);
        }
    }
}
