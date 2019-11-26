package com.goodmorning;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.alex.analytics.Alex;
import org.alex.analytics.biz.logger.general.AppEventsLogger;
import org.homeplanet.sharedpref.RegistrationUtil;
import org.interlaken.common.XalContext;
import org.n.account.core.api.NjordAccountManager;
import org.n.account.core.constant.AlexConstant;
import org.n.account.core.constant.Constant;
import org.n.account.core.contract.IAlexLogger;
import org.n.account.core.contract.IConfiguration;
import org.n.account.core.contract.IExceptionHandler;
import org.n.account.net.NetCode;
import org.n.account.ui.utils.ProfileScope;

public class AccountConfig implements IConfiguration {

    private static final boolean DEBUG = AppConfig.DEBUG;
    private static final String TAG = "AccountConfig";

    public static final int[] PROFILE_SCOPE = new int[]{ProfileScope.ALL};

    public AccountConfig(Context context) {
    }

    @Override
    public String getChannelId() {
        try {
            return XalContext.getChannelId();
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, "getChannelId: ", e);
            }
        }
        return null;
    }

    @Override
    public String getXALClientId() {
        try {
            return XalContext.getClientId();
        } catch (Exception e) {
            if (DEBUG) {
                Log.w(TAG, "getXALClientId: ", e);
            }
        }
        return null;
    }

    @Override
    public String getTLVClientId() {
        return XalContext.getClientId();
    }

    @Override
    public String getAppId() {
        return AppConfig.ACCOUNT_CONFIG_APP_ID;//OA申请
    }

    @Override
    public int getClientType() {
        return Constant.ClientType.ANDROID;
    }

    @Override
    public int[] allowLoginType() {
        //按需，支持facebook、手机号、邮箱、匿名登录
        return new int[]{Constant.LoginType.FACEBOOK, Constant.LoginType.GUEST};
    }

    @Override
    public Bundle getExtChannelId() {
        return new Bundle();
    }

    @Override
    public Bundle getRequestExtParams() {
        return new Bundle();
    }

    @Override
    public String getHost() {
        //TODO 账号替换 AccountReplace
//        return "http://account.picku.cloud/v2/";//正式
        return "http://test-account.apuscn.com/v2/";//测试
    }

    public static class AccountExceptionHandler implements IExceptionHandler {

        @Override
        public void handleException(Context context, int code, String msg) {
            switch (code) {
                case NetCode.ACCOUNT_DATA_ERROR_NET:
                case NetCode.ACCOUNT_DATA_ERROR_TOKEN:
                case NetCode.VERIFICATION_ERROR:
                case NetCode.USER_MISSING_AUTHORITY:
                    NjordAccountManager.localLogout(context);
                    //AccountUIHelper.startLogin(context,PROFILE_SCOPE);
                    if (context instanceof Activity) {
                        ((Activity) context).finish();
                    }
                    break;
                case NetCode.NEED_TOAST:
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onLogout() {
        }
    }

    public static class AlexLogStatistic implements IAlexLogger {
        AppEventsLogger eventsLogger;

        public AlexLogStatistic(String module) {
            this.eventsLogger = Alex.newLogger(module);

        }

        @Override
        public void log(int eventName, Bundle bundle) {
            eventsLogger.logEvent(eventName, bundle);
        }

        @Override
        public void setState(Bundle bundle) {
            Object f = bundle.get(AlexConstant.FROM_STATE);
            Object t = bundle.get(AlexConstant.TO_STATE);
            String name = bundle.getString(AlexConstant.PARAM_NAME);
            if (f instanceof String) {
                eventsLogger.setState(name, (String) f, (String) t);
            } else if (f instanceof Integer) {
                eventsLogger.setState(name, (Integer) f, (Integer) t);
            } else if (f instanceof Boolean) {
                eventsLogger.setState(name, (Boolean) f, (Boolean) t);
            }
        }
    }

}