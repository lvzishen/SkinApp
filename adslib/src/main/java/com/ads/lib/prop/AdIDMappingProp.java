package com.ads.lib.prop;

import android.content.Context;
import android.text.TextUtils;

import com.ads.lib.ModuleConfig;

import org.interlaken.common.env.BasicProp;

public class AdIDMappingProp extends BasicProp {

    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = DEBUG ? "AdPositionIdProp" : "";

    public static final String PROP_FILE = "app_apid_mapping.prop";

    private static AdIDMappingProp mInstance;

    private AdIDMappingProp(Context c) {
        super(c, PROP_FILE, "utf-8");

    }


    public static AdIDMappingProp getInstance(Context context) {
        if (null == mInstance) {
            synchronized (AdPositionIdProp.class) {
                if (null == mInstance) {
                    mInstance = new AdIDMappingProp(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public static void reload(Context context) {
        synchronized (AdPositionIdProp.class) {
            mInstance = new AdIDMappingProp(context.getApplicationContext());
        }
    }

    public String getCachePoolPositionId(String adPositionId) {
        if (TextUtils.isEmpty(adPositionId)) {
            return "";
        }
        String poolPositionId = getId(adPositionId);
        return correctData(adPositionId, poolPositionId);
    }

    /**
     * 针对只有广告池id没有广告位id的数据进行修正，广告池id即广告位id
     * @return
     */
    private String correctData(String adPositionId, String poolPositionId) {
        switch (adPositionId){
            //应用外共用缓存池
            case "148013281":
                return adPositionId;

        }
        return poolPositionId;
    }

    public String getPlacementId(String cachePoolPositionId) {
        if (TextUtils.isEmpty(cachePoolPositionId)) {
            return "";
        }
        String placementId = getId(cachePoolPositionId);
        return placementId;
    }

    private String getId(String adPositionId) {
        String defaultValue = "";
        switch (adPositionId) {
            //广告位对应广告池
            case "148013319":
                defaultValue = "148013317";
                break;
            case "148013342":
                defaultValue = "148013317";
                break;
            case "148013294":
                defaultValue = "148013317";
                break;
            case "148013295":
                defaultValue = "148013317";
                break;

            case "148013320":
                defaultValue = "148013271";
                break;
            case "148013321":
                defaultValue = "148013271";
                break;
            case "148013296":
                defaultValue = "148013271";
                break;
            case "148013322":
                defaultValue = "148013271";
                break;

            case "148013343":
                defaultValue = "148013273";
                break;

            case "148013323":
                defaultValue = "148013292";
                break;

            case "148013344":
                defaultValue = "148013318";
                break;

            case "148013345":
                defaultValue = "148013293";
                break;

            case "148013346":
                defaultValue = "148013274";
                break;

            case "148013331":
                defaultValue = "148013301";
                break;

            case "148013390":
                defaultValue = "148013413";
                break;

            case "148013391":
                defaultValue = "148013460";
                break;

            case "148013392":
                defaultValue = "148013437";
                break;

            case "148013414":
                defaultValue = "148013389";
                break;
            case "148013418":
                defaultValue = "148013466";
                break;

            //广告池对应positionId
            case "pid_p_result_n":
                defaultValue = "148013317";
                break;
            case "pid_p_result_i":
                defaultValue = "148013271";
                break;
            case "pid_p_main_back_n":
                defaultValue = "148013273";
                break;
            case "pid_p_sec_entry_i":
                defaultValue = "148013292";
                break;
            case "pid_p_res_back_i":
                defaultValue = "148013318";
                break;
            case "pid_p_charge_i":
                defaultValue = "148013293";
                break;
            case "pid_p_home_i":
                defaultValue = "148013274";
                break;
            case "pid_p_msn_feed_i":
                defaultValue = "148013301";
                break;
            case "pid_p_add_out":
                defaultValue = "148013281";
                break;
            case "pid_p_cpu_res_n":
                defaultValue = "148013413";
                break;
            case "pid_p_cpu_res_i":
                defaultValue = "148013460";
                break;
            case "pid_p_charge_save_res_n":
                defaultValue = "148013437";
                break;
            case "pid_p_charge_save_res_i":
                defaultValue = "148013389";
                break;
            case "pid_p_speed_limit_i":
                defaultValue = "148013466";
                break;

            //广告池对应策略
            case "148013317":
                defaultValue = "mp:9bf3ec9dfc8e40d4a9bff7c88b1192ff";
                break;
            case "148013271":
                defaultValue = "mp1:d5361e861a9845108f14b4a0218159b0";
                break;
            case "148013273":
                defaultValue = "mp:8058e0313e12453eb0064e1e4df95262";
                break;
            case "148013292":
                defaultValue = "mp1:981275a3d7464b568cf6308840cc8ceb";
                break;
            case "148013318":
                defaultValue = "mp1:0da506a714cd4aefa71c430d35aaa15b";
                break;
            case "148013293":
                defaultValue = "mp1:0778c86e19e64df3b491347f33090e9d";
                break;
            case "148013274":
                defaultValue = "mp1:8f6e9aeadd3841f681ea3e9ea7156b3e";
                break;
            case "148013301":
                defaultValue = "mp1:f20ebdd1ca1048ca8c537701c763d52b";
                break;
            case "148013281":
                defaultValue = "mp1:a78dd0e46d7f43f8a403d536917a2fc7";
                break;
            case "148013413":
                defaultValue = "mp:a98ce44589ba4e73ad7eea1ac2da4bce";
                break;
            case "148013460":
                defaultValue = "mp1:211c6548cf0846db940efed77e50372c";
                break;
            case "148013437":
                defaultValue = "mp:eb4a86cc6b4943da819253d469d7a0ed";
                break;
            case "148013389":
                defaultValue = "mp1:53b2f0a855be42eeb1ebdb4d4e84f189";
                break;
            case "148013466":
                defaultValue = "mp1:0076d3c263744185a69e0d582ff0c37b";
                break;
        }
        return get(adPositionId, defaultValue);
    }


}
