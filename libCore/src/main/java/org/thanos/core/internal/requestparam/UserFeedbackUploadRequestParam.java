package org.thanos.core.internal.requestparam;

import org.thanos.core.MorningDataAPI;
import org.thanos.core.bean.ContentItem;
import org.thanos.core.bean.requestbean.BaseProtocol;

import java.util.ArrayList;
import java.util.List;

import static org.thanos.core.internal.MorningDataCore.DEBUG;

/**
 * 用户反馈上报参数
 */
public class UserFeedbackUploadRequestParam extends BaseRequestParam<UserFeedbackUploadRequestParam.UserFeedbackUploadProtocal> {
    /**
     * 新闻不感兴趣
     */
    public static final UserFeedbackReason NEWS_NOT_INTERESTED = new UserFeedbackReason(1, 0);
    /**
     * 新闻屏蔽来源
     */
    public static final UserFeedbackReason NEWS_BLOCK_SOURCE = new UserFeedbackReason(2, 0);
    /**
     * 新闻内容虚假
     */
    public static final UserFeedbackReason NEWS_FAKE_NEWS = new UserFeedbackReason(3, 1);
    /**
     * 新闻内容色情
     */
    public static final UserFeedbackReason NEWS_PORN = new UserFeedbackReason(3, 2);
    /**
     * 新闻标题党
     */
    public static final UserFeedbackReason NEWS_CLICKBAIT = new UserFeedbackReason(3, 3);
    /**
     * 新闻过旧或重复
     */
    public static final UserFeedbackReason NEWS_OLD_OR_REPETITIVE = new UserFeedbackReason(3, 4);
    /**
     * 视频不感兴趣
     */
    public static final UserFeedbackReason VIDEO_NOT_INTERESTED = new UserFeedbackReason(1, 0);
    /**
     * 视频屏蔽作者
     */
    public static final UserFeedbackReason VIDEO_BLOCK_SOURCE = new UserFeedbackReason(2, 0);
    /**
     * 视频内容虚假
     */
    public static final UserFeedbackReason VIDEO_FAKE_OR_MISLEADING = new UserFeedbackReason(3, 0);
    /**
     * 视频内容色情
     */
    public static final UserFeedbackReason VIDEO_PORN = new UserFeedbackReason(4, 0);
    /**
     * 视频内容重复
     */
    public static final UserFeedbackReason VIDEO_REPETITIV = new UserFeedbackReason(5, 0);
    /**
     * 视频内容引起不适
     */
    public static final UserFeedbackReason VIDEO_INAPPROPRIATE = new UserFeedbackReason(6, 0);
    /**
     * 视频屏蔽关键词
     */
    public static final UserFeedbackReason VIDEO_BLOCK_KEYWORDS = new UserFeedbackReason(7, 0);
    /**
     * 视频播放画质不清晰
     */
    public static final UserFeedbackReason VIDEO_PLAY_NOT_CLEAR = new UserFeedbackReason(8, 1);
    /**
     * 视频播放多次加载
     */
    public static final UserFeedbackReason VIDEO_PLAY_MULTIPLE_LOAD = new UserFeedbackReason(8, 2);
    /**
     * 视频播放失败黑屏
     */
    public static final UserFeedbackReason VIDEO_PLAY_BLACK_SCREEN = new UserFeedbackReason(8, 3);
    /**
     * 视频播放音视频不同步或者没声音
     */
    public static final UserFeedbackReason VIDEO_PLAY_NOT_SYNC_OR_NO_SOUND = new UserFeedbackReason(8, 4);

    public final ContentItem contentItem;
    private UserFeedbackReason userFeedbackReason;


    public UserFeedbackUploadRequestParam(ContentItem contentItem, UserFeedbackReason userFeedbackReason, boolean acceptCache) {
        this(contentItem, userFeedbackReason, acceptCache, 1);
    }

    public UserFeedbackUploadRequestParam(ContentItem contentItem, UserFeedbackReason userFeedbackReason, boolean acceptCache, int module) {
        super("UFU", acceptCache, false, module);
        this.contentItem = contentItem;
        this.userFeedbackReason = userFeedbackReason;
        this.module = module;
    }

    /**
     * 新闻屏蔽关键词
     *
     * @param keywords 要屏蔽的关键词
     */
    public static UserFeedbackReason createNewsBlockKeywordsReason(String keywords) {
        return new UserFeedbackReason(4, 0, keywords);
    }

    /**
     * 视频屏蔽关键词
     *
     * @param keywords 要屏蔽的关键词
     */
    public static UserFeedbackReason createVideoBlockKeywordsReason(String keywords) {
        return new UserFeedbackReason(7, 0, keywords);
    }


    @Override
    public UserFeedbackUploadProtocal createProtocol() {
        UserFeedbackUploadProtocal userFeedbackUploadProtocal = new UserFeedbackUploadProtocal();
        userFeedbackUploadProtocal.resource_id = this.contentItem.id;
        int resourceType = this.contentItem.type == MorningDataAPI.RESOURCE_TYPE_NEWS ? 1 : 2;
        userFeedbackUploadProtocal.resource_type = resourceType;
        userFeedbackUploadProtocal.labels.add(userFeedbackReason.getUserFeedbackReason());
        return userFeedbackUploadProtocal;
    }

    @Override
    public String getCacheKey() {
        return "";
    }

    @Override
    public String toString() {
        return DEBUG ? "UserFeedbackUploadParam{" +
                "contentItem=" + contentItem +
                ", userFeedbackReason=" + userFeedbackReason +
                ", requestModuleName='" + requestModuleName + '\'' +
                ", acceptCache=" + acceptCache +
                '}' : "";
    }

    public static class UserFeedbackUploadProtocal extends BaseProtocol {

        public long resource_id;
        public int resource_type;
        public List<UserFeedbackReason.UserFeedbackReasonIn> labels = new ArrayList<>();
    }


}