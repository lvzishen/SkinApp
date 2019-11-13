package com.ads.lib.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.ads.lib.prop.CtrControl;
import com.facebook.ads.AudienceNetworkActivity;

import java.util.Calendar;

public class CoversControl {

    private static final boolean DEBUG = true;

    private static final String TAG = "CoversControl";

    private static final String SP_KEY_COVER_MISSING = "c_missing";

    private static CoversControl mInstance;

    private static final String SP_NAME = "times_config";

    private static final int HIT = 1;


    private IActivityCapture mActivityCapture;


    public interface IActivityCapture {

        void initCapture(String captureActivityName);

        Activity getCaptureActivity();
    }

    public void setActivityCapture(IActivityCapture activityCapture) {
        this.mActivityCapture = activityCapture;
        activityCapture.initCapture(AudienceNetworkActivity.class.getName());
    }

    public static CoversControl getInstance() {
        if (mInstance == null) {
            synchronized (CoversControl.class) {
                if (mInstance == null) {
                    mInstance = new CoversControl();
                }
            }
        }
        return mInstance;
    }


    public void trigger(Context context, String placementId) {

        CtrControl ctrControl = CtrControl.getInstance(context);

        if (!ctrControl.isEnable()) {
            return;
        }

        if (!TextUtils.equals(placementId, "all")) {
            if (!ctrControl.getControlPlacements().contains(placementId)) {
                return;
            }
        }

        boolean random = getRandom(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);

        int missing = sharedPreferences.getInt(SP_KEY_COVER_MISSING, 0);
        if (!isToday(sharedPreferences)) {
            missing = 0;
        }

        boolean isHit = random;
        Activity captureActivity = mActivityCapture.getCaptureActivity();
        if (isHit && captureActivity != null) {
            float[] coverScale = getCoverScale(context);
            Covers.Style style = new Covers.Style.Builder(ctrControl.getCoverLocation())
                    .setScaleWidht(coverScale[0])
                    .setScaleHeight(coverScale[1])
                    .build();
            Covers.coverView2Window(captureActivity, style, ctrControl.getDelayShowTime(), ctrControl.getDelayDismissTime());
        } else {
            sharedPreferences.edit().putInt(SP_KEY_COVER_MISSING, ++missing).apply();
        }
    }

    /**
     * 获取缩放比例
     *
     * @param context
     * @return [width, height]
     */
    private static float[] getCoverScale(Context context) {
        String coverScale = CtrControl.getInstance(context).getCoverScale();
        String[] split = coverScale.split(",");
        float[] range = {1.0f, 0.5f};
        if (split.length == 2) {
            try{
                range[0] = Float.valueOf(split[0]);
                range[1] = Float.valueOf(split[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return range;
    }

    private boolean getRandom(Context context) {
        String randomRangeStr = CtrControl.getInstance(context).getRandomRange();
        String[] split = randomRangeStr.split(",");
        int[] range = {1, 100};
        if (split.length == 2) {
            try{
                range[0] = Integer.valueOf(split[0]);
                range[1] = Integer.valueOf(split[1]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        int random = Randoms.getRandom(1, 100);
        if (random >= range[0] && random <= range[1]) {
            return true;
        }
        return false;
    }


    private boolean isToday(SharedPreferences sharedPreferences) {
        long today24Mills = sharedPreferences.getLong("DAY_IS", 0);
        if (today24Mills == 0) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            today24Mills = getTimesNight();
            edit.putLong("DAY_IS", today24Mills).apply();
        }
        long nowTime = System.currentTimeMillis();
        if (nowTime <= today24Mills) {
            //当日
            return true;
        } else {
            if (DEBUG) {
                Log.d(TAG, "isLowerDailyMaxShow :非当日，清零 ");
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putLong("DAY_IS", getTimesNight()).apply();
        }
        return false;
    }

    //获得当天24点的时间（毫秒数）
    private static long getTimesNight() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 24);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    public void destroy(){
        Covers.destroy();
    }

}
