package org.thanos.netcore.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;


import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.exception.AuthFailureError;
import org.n.account.core.model.Account;
import org.n.account.core.utils.NjordIdHelper;
import org.n.account.core.utils.SessionHelper;
import org.thanos.netcore.internal.requestparam.BaseRequestParam;

import okhttp3.Request;

/**
 * 创建日期：2019/11/27 on 15:31
 * 描述:
 * 作者: lvzishen
 */
public class GoodMorningCollectRequest extends GoodMorningRequest {
    public static final String APPID = "100410024";

    public GoodMorningCollectRequest(Context context, @NonNull BaseRequestParam baseRequestParam, String url) {
        super(context, baseRequestParam, url);
    }

    private String createSession(Context context) {
        Account account = NjordAccountManager.getCurrentAccount(context);
        if (null != account) {
            String appid = "app_id=" + APPID;
            String psu = NjordIdHelper.getPSU(account);
            String key = NjordIdHelper.getKey(account);
            String random = NjordIdHelper.getRandom(account);
            try {
                return SessionHelper.composeCookieWithSession(
                        context,
                        key,
                        psu,
                        random,
                        appid.getBytes()
                );
            } catch (AuthFailureError ex) {
            }
        }
        return "";
    }

    @Override
    public void configRequest(Context context, Request.Builder builder) {
        super.configRequest(context, builder);
        String session = createSession(context);
        String str[] = session.split(";");
        builder.addHeader("psu", str[0].substring(4));
        builder.addHeader("pmc", str[1].substring(4));
        Log.i("GoodMorningCollect", "session=" + session);
        Log.i("GoodMorningCollect", "psu=" + str[0].substring(4));
        Log.i("GoodMorningCollect", "pmc=" + str[1].substring(4));
    }

}
