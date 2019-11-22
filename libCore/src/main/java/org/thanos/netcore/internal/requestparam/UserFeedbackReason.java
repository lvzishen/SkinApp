package org.thanos.netcore.internal.requestparam;


import org.thanos.netcore.internal.MorningDataCore;

public class UserFeedbackReason {
    public final int firstId;
    public final int secondId;
    public final String text;

    UserFeedbackReason(int firstId, int secondId) {
        this(firstId, secondId, "");
    }

    public UserFeedbackReason(int firstId, int secondId, String text) {
        this.firstId = firstId;
        this.secondId = secondId;
        this.text = text;
    }


    public UserFeedbackReasonIn getUserFeedbackReason() {
        UserFeedbackReasonIn userFeedbackReasonIn = new UserFeedbackReasonIn();
        userFeedbackReasonIn.first_id = this.firstId;
        userFeedbackReasonIn.second_id = this.secondId;
        // 只有这两种情况允许传text
        // 1. 新闻屏蔽关键词 Block keywords first_id=4 second_id=0 text="xxx"
        // 2. 视频屏蔽关键词：最多3个. first_id=7 second_id=0 text="xxx"
        if (firstId == 4 && secondId == 0 || firstId == 7 && secondId == 0) {
            userFeedbackReasonIn.text = this.text;
        } else {
            userFeedbackReasonIn.text = "";
        }
        return userFeedbackReasonIn;
    }

    @Override
    public String toString() {
        return MorningDataCore.DEBUG ? "UserFeedbackReason{" +
                "firstId=" + firstId +
                ", secondId=" + secondId +
                ", text='" + text + '\'' +
                '}' : "";
    }

    public static class UserFeedbackReasonIn {
        public int first_id;
        public int second_id;
        public String text;
    }

}