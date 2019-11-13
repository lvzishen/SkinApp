package com.ads.lib.prop;

import android.content.Context;
import android.text.format.DateUtils;

import com.ads.lib.ModuleConfig;

import org.cloud.library.Cloud;
import org.interlaken.common.env.BasicProp;

public class CtrControl extends BasicProp {

    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = DEBUG ? "CtrControl" : "";
    public static final String REMOTE_CONFIG_NAME = "fctv.prop";

    private static CtrControl mInstance;

    private CtrControl(Context context) {
        super(context, REMOTE_CONFIG_NAME);
    }

    private static final String FB_CTR_ENABLE = "enable";

    private static final String FB_CTR_TRIGGER_Random_Range = "trigger_random_range";

    private static final String FB_CTR_Delay_Show_Time = "delay_show_time";

    private static final String FB_CTR_Click_Threshold = "delay_click_threshold";

    private static final String FB_CTR_Delay_Dismiss_Time= "delay_dismiss_time";

    private static final String FB_CTR_PlacementID= "placements";

    private static final String FB_CTR_Cover_Scale= "coverscale";

    private static final String FB_CTR_Cover_Location= "coverlocation";


    public static CtrControl getInstance(Context context) {
        if (null == mInstance) {
            synchronized (AdPositionIdProp.class) {
                if (null == mInstance) {
                    mInstance = new CtrControl(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }


    /**
     * ctr 控制开关 非0即开 默认关
     *
     * @return
     */
    public boolean isEnable() {
        int value = getInt(FB_CTR_ENABLE, 0);
        return value > 0;
    }


    /**
     * 有N次没有命中,判断触发的阈值 默认10
     *
     * @return
     */
    public int getMissingThreshold() {
        return getInt(FB_CTR_Click_Threshold, 1);
    }


    /**
     * 随机范围 如果是1则命中 默认 1,100 配置说明: [start,end]
     *
     * @return
     */
    public String getRandomRange() {
        return get(FB_CTR_TRIGGER_Random_Range, "1,100");
    }

    /**
     * 延迟N秒后展示插屏覆层 默认1 单位秒
     *
     * @return
     */
    public long getDelayShowTime() {
        return getLong(FB_CTR_Delay_Show_Time, 500);
    }

    /**
     * 延迟N秒后关闭插屏覆层 默认 3 单位秒
     *
     * @return
     */
    public long getDelayDismissTime() {
        return getLong(FB_CTR_Delay_Dismiss_Time, 3) * DateUtils.SECOND_IN_MILLIS;
    }

    /**
     * 获取受控制的placementId 多条已","分隔
     *
     * @return
     */
    public String getControlPlacements() {
        return get(FB_CTR_PlacementID, "");
    }

    /**
     * 获取覆层缩放比例 默认 1.0,0.5 配置说明: [width,height]
     *
     * @return
     */
    public String getCoverScale() {
        return get(FB_CTR_Cover_Scale, "1.0,0.7");
    }

    /**
     * 获取覆层位置 1顶部显示 2居中显示 3底部显示 默认1
     *
     * @return
     */
    public int getCoverLocation() {
        return getInt(FB_CTR_Cover_Location, 3);
    }

}
