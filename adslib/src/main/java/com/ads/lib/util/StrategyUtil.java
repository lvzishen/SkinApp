package com.ads.lib.util;

import android.util.Log;

import com.ads.lib.ModuleConfig;
import com.ads.lib.adapter.LocalStrategy;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class StrategyUtil {
    public static final String TAG = "StrategyUtil";
    public static final boolean DEBUG = ModuleConfig.DEBUG;

    /**
     * 单条策略分割规则
     */
    public static final String STRATEGY_SPLIT_REG = ":";
    /**
     * 组策略分割规则
     */
    public static final String STRATEGY_SPLIT_GROUP_REG = ",";


    public static List<LocalStrategy> getStrtegyByPlacementId(@NotNull String placementID) {
        try {
            placementID = placementID.trim().toLowerCase();
            String[] strategyGroup = placementID.split(STRATEGY_SPLIT_GROUP_REG);
            ArrayList<LocalStrategy> strategies = new ArrayList<>();
            for (String aStrategyGroup : strategyGroup) {
                String[] strategy = aStrategyGroup.split(STRATEGY_SPLIT_REG);
                if (strategy.length > 1) {
                    LocalStrategy localStrategy = new LocalStrategy();
                    localStrategy.adType = strategy[0];
                    localStrategy.placementId = strategy[1];
                    strategies.add(localStrategy);
                }
            }
            return strategies;

        } catch (Exception e) {
            if (DEBUG) {
                Log.d(TAG, "getStrtegyByPlacementId: Exception",e);
            }
        }
        return null;
    }
}
