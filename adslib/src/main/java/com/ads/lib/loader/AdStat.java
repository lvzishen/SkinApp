package com.ads.lib.loader;

import android.content.Context;

public class AdStat {

	public static final int POSITION_UNKNOWN = -1;
	public static final int POSITION_BOOST_NONE = 1;
	public static final int POSITION_BOOST_RESULT = 2;
	public static final int POSITION_COOLDOWN_RESULT = 3;
	public static final int TEST_INDEX_POSITION_BOOST_RESULT = 4;
	
	public static final int POSITION_RUBBISH_RESULT = 13;
	public static final int POSITION_ANTI_VIRUS = 14;
	public static final int POSITION_NOTIFICATIOIN_BOOST_RESULT = 15;
	public static final int POSITION_BATTERY_RESULT = 16;
	public static final int POSITION_FIRST_SCAN_VIRUS = 17;
	public static final int POSITION_NC_RESULT = 18;
	public static final int POSITION_NS_RESULT = 19;
	public static final int POSITION_WIFI_NORMAL_RESULT = 20;

//	public static void logNormalAdPosition(Context cxt , int position){
//		int funcCode = -1;
//		switch(position){
//			case POSITION_BOOST_NONE:
//				funcCode = Statistics.FUNC_CLICK_BOOSTNONE_ADS;
//				break;
//			case POSITION_BOOST_RESULT:
//				funcCode = Statistics.FUNC_CLICK_BOOST_ADS;
//				break;
//			case POSITION_COOLDOWN_RESULT:
//				funcCode = Statistics.FUNC_CLICK_CPU_ADS;
//				break;
//			case POSITION_ANTI_VIRUS:
//				funcCode = Statistics.FUNC_CLICK_ANTI_VIRUS_ADS;
//				break;
//			case POSITION_RUBBISH_RESULT:
//				funcCode = Statistics.FUNC_CLICK_RUBBISH_RESULT_ADS;
//				break;
//			case POSITION_BATTERY_RESULT:
//				funcCode = Statistics.FUNC_CLICK_BATTERY_RESULT_ADS;
//				break;
//			case POSITION_NC_RESULT:
//				funcCode = Statistics.FUNC_CLICK_NOTIFICATION_CLEAN_AD;
//				break;
//			case POSITION_NS_RESULT:
//				funcCode = Statistics.FUNC_CLICK_NOTIFICATION_SECURITY_AD;
//				break;
//			case POSITION_WIFI_NORMAL_RESULT:
//				funcCode = Statistics.FUNC_CLICK_WIFI_NORMAL_AD;
//				break;
//		}
//
//		if(funcCode > 0){
//			Statistics.logAddition(cxt, funcCode, 1);
//			Statistics.logAddition(cxt, Statistics.FUNC_CLICK_ADS, 1);
//		}
//	}

//	public static void logBigAdPosition(Context cxt, int position) {
//		int funcCode = -1;
//		switch (position) {
//			case POSITION_BOOST_RESULT:
//				funcCode = Statistics.FUNC_CLICK_BOOST_BIG_ADS;
//				break;
//			case POSITION_COOLDOWN_RESULT:
//				funcCode = Statistics.FUNC_CLICK_CPU_BIG_ADS;
//				break;
//			case POSITION_ANTI_VIRUS:
//				funcCode = Statistics.FUNC_CLICK_AV_BIG_ADS;
//				break;
//			case POSITION_RUBBISH_RESULT:
//				funcCode = Statistics.FUNC_CLICK_RUBBISH_BIG_ADS;
//				break;
//			case POSITION_NOTIFICATIOIN_BOOST_RESULT:
//				funcCode = Statistics.FUNC_CLICK_NOTIFICAITON_BOOST_BIG_ADS;
//				break;
//			case POSITION_BATTERY_RESULT:
//				funcCode = Statistics.FUNC_CLICK_BATTERY_RESULT_BIG_ADS;
//				break;
//			case POSITION_FIRST_SCAN_VIRUS:
//				funcCode = Statistics.FUNC_CLICK_FIRST_AVSCAN_RESULT_ADS;
//				break;
//			case POSITION_NC_RESULT:
//				funcCode = Statistics.FUNC_FIRST_CLICK_NOTIFICATION_CLEAN_AD;
//				break;
//			case POSITION_NS_RESULT:
//				funcCode = Statistics.FUNC_FIRST_CLICK_NOTIFICATION_SECURITY_AD;
//				break;
//			case POSITION_WIFI_NORMAL_RESULT:
//				funcCode = Statistics.FUNC_FIRST_CLICK_WIFI_NORMAL_AD;
//				break;
//		}
//
//		if (funcCode > 0) {
//			Statistics.logAddition(cxt, funcCode, 1);
//			Statistics.logAddition(cxt, Statistics.FUNC_CLICK_ADS, 1);
//		}
//	}

}