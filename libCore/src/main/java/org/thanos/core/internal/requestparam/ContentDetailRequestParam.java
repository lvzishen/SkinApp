package org.thanos.core.internal.requestparam;


import org.thanos.core.bean.requestbean.BaseProtocol;

import static org.thanos.core.internal.MorningDataCore.DEBUG;

/**
 * 创建日期：2019/11/14 on 14:51
 * 描述:
 * 作者: lvzishen
 */
public class ContentDetailRequestParam extends BaseRequestParam<ContentDetailRequestParam.ContentDetailProtocol> {
    private ContentDetailProtocol contentDetailProtocol;

    public ContentDetailRequestParam(ContentDetailProtocol contentDetailProtocol) {//, int module
        super("CD", contentDetailProtocol.acceptCache, false, 1);//module
        this.contentDetailProtocol = contentDetailProtocol;
    }


    @Override
    public ContentDetailProtocol createProtocol() {
        return contentDetailProtocol;
    }

    /**
     * 设置内容详情缓存的key
     *
     * @return
     */
    @Override
    public String getCacheKey() {
        return String.valueOf(this.contentDetailProtocol.resourceID + module);
    }

    @Override
    public String toString() {
        return DEBUG ? "ContentDetailBaseRequestParam{" +
                "resourceID=" + contentDetailProtocol.resourceID +
                ", requestModuleName='" + requestModuleName + '\'' +
                ", acceptCache=" + acceptCache +
                ", module=" + module +
                '}' : "";
    }

    public static class ContentDetailProtocol extends BaseProtocol {
        public long resourceID;
    }

}
