package com.ads.lib.util;

public class PageUtil {

    /**
     * 临时方案 没必要给loader拓展接口 目前只会出现在本次打点方案上
     *
     * @param positionId
     * @return
     */
    public static String getPageName(String positionId) {
        if (positionId == null) {
            return "";
        }
        switch (positionId) {
            case "148013319":
                return "VirusRes_Native";
            case "148013342":
                return "RAMRes_Native";
            case "148013294":
                return "CleanRes_Native";
            case "148013295":
                return "NotifyClean_Native";
            case "148013320":
                return "VirusRes_Interstitial";
            case "148013321":
                return "RAMRes_Interstitial";
            case "148013296":
                return "CleanRes_Interstitial";
            case "148013322":
                return "NotifyClean_Interstitial";
            case "148013297":
            case "148013272":
                return "Splash_Native";
            case "148013275":
            case "148013341":
                return "Splash_Interstitial";
            case "148013343":
            case "148013273":
                return "MainBack_Native";
            case "148013323":
            case "148013292":
                return "SecEntry_Interstitial";
            case "148013344":
            case "148013318":
                return "ResBack_Interstitial";
            case "148013345":
            case "148013293":
                return "Charge_Interstitial";
            case "148013346":
            case "148013274":
                return "Homepage_Interstitial";
            case "148013414":
            case "148013389":
                return "ChargeSave_Res_Interstitial";
            case "148013392":
            case "148013437":
                return "ChargeSave_Res_Native";
            case "148013391":
            case "148013460":
                return "CPU_Res_Interstitial";
            case "148013390":
            case "148013413":
                return "CPU_Res_Native";
            case "148013331":
            case "148013301":
                return "MSNFeed_Interstitial";
            case "148013281":
                return "OutAdd_Interstitial";
            case "148013271":
                return "Result_Interstitial";
            case "148013317":
                return "Result_Native";
            case "148013466":
            case "148013418":
                return "SpeedLimit_Interstitial";
            case "148013340":
                return "Splash_Spare_Interstitial";
            case "148013291":
                return "Splash_Spare_Native";
            default:
                return "no match";
        }
    }

}
