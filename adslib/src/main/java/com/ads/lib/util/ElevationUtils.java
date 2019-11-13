package com.ads.lib.util;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ads.lib.ModuleConfig;

public class ElevationUtils {

    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = "ElevationUtils";

    public static float getMaxElevation(ViewGroup viewGroup) {
        float dstElevation = 0;
        try {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewGroup != null && viewGroup.getChildCount() > 0) {
                    int size = viewGroup.getChildCount();
                    for (int i = 0; i < size; i++) {
                        View childView = viewGroup.getChildAt(i);
                        float elevation = childView.getElevation();
                        if (elevation > dstElevation) {
                            dstElevation = elevation;
                        }
                    }
                }
            }
        } catch (Exception e) {

        }

        if (DEBUG) {
            Log.d(TAG, "getMaxElevation = " + dstElevation);
        }
        return dstElevation;
    }
}
