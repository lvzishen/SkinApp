package org.thanos.core;

import android.os.Bundle;

import org.interlaken.common.XalContext;

/**
 * Created by zhaobingfeng on 2019-07-29.
 * 打点工具
 */
public class StatisticsUtil {
    private static final String MODULE_NAME = "thanos";

    public static void logEvent(int eventName, Bundle params) {
        XalContext.logEvent(MODULE_NAME, eventName, params);
    }
}
