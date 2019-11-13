package com.hotvideo.xal;

import android.content.Context;
import android.util.Log;

import com.baselib.cloud.CloudPropertyManager;
import com.hotvideo.config.GlobalConfig;

import org.adoto.xrg.IAdotoActivateListener;
import org.cloud.library.Cloud;
import org.interlaken.common.XalContext;
import org.jetbrains.annotations.NotNull;
import org.saturn.splash.prop.SplashProp;

import java.util.Map;

/**
 * Created by wangyida on 17-12-14.
 * <p>
 * V5 云控强开的逻辑在这里修改
 */

public class NeptuneReporter implements Cloud.CloudAttributeUpdateListener, Cloud.CloudFileUpdatedListener, IAdotoActivateListener {

    private static final boolean DEBUG = GlobalConfig.DEBUG;
    private static final String TAG = "NeptuneReporter";
    private Context mContext;

    public NeptuneReporter(Context context) {
        mContext = context;
    }

    @Override
    public void onActivateSuccess(boolean isBecauseReferer) {
        if (XalContext.isPersistProcess()) {
//            String refer = TextUtils.isEmpty(RegistrationHelper.getFirstReferrer())
//                    ? XalContext.getChannelId() : RegistrationHelper.getFirstReferrer();
//            QuestionnaireTask
//                    .builder()
//                    .setProductId(AppConfig.UIVERSION)
//                    .setClientId(XalContext.getClientId()) // XAL激活的client ID
//                    .setRefer(refer)
//                    .setIconResId(R.drawable.ic_launcher1)
//                    .setQuestionnaireConfigProvider(new QuestionnaireConfigProvider() {
//                        @Override
//                        public long getExecuteDelay(String key, long defaultValue) {
//                            return Cloud.getLong(key, defaultValue);
//                        }
//                    })
//                    .request(mContext, isBecauseReferer);
        }

    }

    @Override
    public void onAttributeUpdate(@NotNull String moduleName, @NotNull Map<String, String> map) {
        if (DEBUG) {
            Log.i(TAG, "onAttributeUpdate->");
        }

      /*  if ("Trade_SkConfig".equalsIgnoreCase(moduleName)
                || MagnetoApi.MODULE_V5_NAME.equalsIgnoreCase(moduleName)
                || "g_trade_autoopt".equalsIgnoreCase(moduleName)
                || "g_trade_charging_v2".equalsIgnoreCase(moduleName)) {
            Iterator<Map.Entry<String, String>> iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = iter.next();

                if (DEBUG) {
                    Log.d(TAG, "onRemoteConfigUpdate configName :" + entry.getKey());
                }
                if ("Trade_SkConfig".equalsIgnoreCase(moduleName)) {
                    ScenarizedControlProp.getInstance(BoosterApplication.sContext).onXalCloudValueChanged(entry.getKey());
                } else if (MagnetoApi.MODULE_V5_NAME.equalsIgnoreCase(moduleName)) {
                    MagnetoApi.onXalCloudValueChanged(BoosterApplication.sContext, entry.getKey(), entry.getValue());
                } else if ("g_trade_autoopt".equalsIgnoreCase(moduleName)) {
                    AutoCleanSdk.onXalCloudValueChanged(BoosterApplication.sContext, entry.getKey());
                } else if ("g_trade_charging_v2".equalsIgnoreCase(moduleName)) {
                    AutoCleanSdk.onXalCloudValueChanged(BoosterApplication.sContext, entry.getKey());
                }
            }

        }*/
        SplashProp.reload(mContext);
    }

    @Override
    public void onCloudFileUpdated(@NotNull String fileName) {
        if (DEBUG) {
            Log.i(TAG, "onFileUpdate->" + fileName);
        }

        if (!XalContext.isPersistProcess()) {
            return;
        }
/*        if (TemperProp.isCloudUpdateFile(fileName)) {
            ThreadPool.getInstance().submit(new Runnable() {
                @Override
                public void run() {
                    TemperProp.reload(mContext);
                }
            });
            return;
        }*/
        CloudPropertyManager.reload(mContext, fileName);
        SplashProp.reload(mContext);

    }

}
