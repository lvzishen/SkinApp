package org.thanos.core.internal.requestparam;

/**
 * 创建日期：2019/11/15 on 10:30
 * 描述:
 * 作者: lvzishen
 */

import org.interlaken.common.XalContext;
import org.thanos.core.MorningDataAPI;
import org.thanos.core.bean.ContentItem;
import org.thanos.core.bean.requestbean.BaseProtocol;

import static org.thanos.core.internal.MorningDataCore.DEBUG;

/**
 * 用户行为上报参数
 */
public class UserBehaviorUploadParam extends BaseRequestParam<UserBehaviorUploadParam.UserBehaviorUploadProtocal> {
    private ContentItem contentListItem;
    private UserBehavior userBehavior;
    private boolean cancel;

    public UserBehaviorUploadParam(ContentItem contentListItem, UserBehavior userBehavior, boolean cancel) {
        this(contentListItem, userBehavior, cancel, 1);
    }

    /**
     * 用户行为
     *
     * @param contentListItem 用户行为的目标
     * @param userBehavior    用户具体行为
     * @param cancel          用户是是否是取消该行为
     */
    public UserBehaviorUploadParam(ContentItem contentListItem, UserBehavior userBehavior, boolean cancel, int module) {
        super("UU", false, false, module);
        this.contentListItem = contentListItem;
        this.userBehavior = userBehavior;
        this.cancel = cancel;
        this.module = module;
    }

    @Override
    public UserBehaviorUploadProtocal createProtocol() {
        UserBehaviorUploadProtocal userBehaviorUploadProtocal = new UserBehaviorUploadProtocal();
        userBehaviorUploadProtocal.client_id = XalContext.getClientId();
        userBehaviorUploadProtocal.id = this.contentListItem.id;
        userBehaviorUploadProtocal.type = this.userBehavior.type;
        // 资源类型. 1新闻，2视频
        int resourceType = this.contentListItem.type == MorningDataAPI.RESOURCE_TYPE_NEWS ? 1 : 2;
        userBehaviorUploadProtocal.resource_type = resourceType;
        userBehaviorUploadProtocal.cancel = this.cancel;


        return userBehaviorUploadProtocal;
    }


    @Override
    public String getCacheKey() {
        return "";
    }

    @Override
    public String toString() {
        return DEBUG ? "UserBehaviorUploadParam{" +
                "contentListItem=" + contentListItem +
                ", userBehavior=" + userBehavior +
                ", cancel=" + cancel +
                ", requestModuleName='" + requestModuleName + '\'' +
                ", acceptCache=" + acceptCache +
                ", module=" + module +
                '}' : "";
    }


    public static class UserBehaviorUploadProtocal extends BaseProtocol {
        public String client_id;
        public long id;
        public Object type;
        public int resource_type;
        public boolean cancel;
    }

    /**
     * 用户行为集合
     */
    public enum UserBehavior {
        /**
         * 阅读
         */
        READ(1),
        /**
         * 点赞
         */
        LIKE(2),
        /**
         * 吐槽
         */
        DISLIKE(3),
        /**
         * 评论
         */
        COMMENT(4),
        /**
         * 分享
         */
        SHARE(5),
        /**
         * 收藏
         */
        FAVORITE(6),
        /**
         * 下载
         */
        DOWNLOAD(7);

        public final int type;

        UserBehavior(int type) {
            this.type = type;
        }
    }
}