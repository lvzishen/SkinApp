package org.thanos.netcore.internal.requestparam;


import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.internal.MorningDataCore;

/**
 * 创建日期：2019/11/14 on 14:51
 * 描述:
 * 作者: lvzishen
 */
public class ContentDetailRequestParam extends BaseRequestParam<ContentDetailRequestParam.ContentDetailProtocol> {
    private int resourceID;

    public ContentDetailRequestParam(boolean acceptCache, int resourceId) {//, int module
        super("CD", acceptCache, false, 1);//module
        this.resourceID = resourceId;
    }


    @Override
    public ContentDetailProtocol createProtocol() {
        ContentDetailProtocol contentDetailProtocol = new ContentDetailProtocol();
        contentDetailProtocol.resourceID = this.resourceID;
        return contentDetailProtocol;
    }

    /**
     * 设置内容详情缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return String.valueOf(this.resourceID + module);
    }

    @Override
    public String toString() {
        return MorningDataCore.DEBUG ? "ContentDetailBaseRequestParam{" +
                "resourceID=" + resourceID +
                ", requestModuleName='" + requestModuleName + '\'' +
                ", acceptCache=" + acceptCache +
                ", module=" + module +
                '}' : "";
    }

    public static class ContentDetailProtocol extends BaseProtocol {
        public long resourceID;
    }

}