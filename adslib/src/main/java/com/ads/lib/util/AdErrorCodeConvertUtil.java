package com.ads.lib.util;


import com.ads.lib.mediation.bean.AdErrorCode;

public class AdErrorCodeConvertUtil {

    public static org.saturn.splash.loader.error.AdErrorCode convert(AdErrorCode errorCode) {
        switch (errorCode) {
            case ESULT_0K:
                return org.saturn.splash.loader.error.AdErrorCode.RESULT_0K;
            case UNSPECIFIED:
                return org.saturn.splash.loader.error.AdErrorCode.UNSPECIFIED;
            case NETWORK_RETURN_NULL_RESULT:
                return org.saturn.splash.loader.error.AdErrorCode.NETWORK_RETURN_NULL_RESULT;
            case IMAGE_DOWNLOAD_FAILURE:
                return org.saturn.splash.loader.error.AdErrorCode.IMAGE_DOWNLOAD_FAILURE;
            case CONNECTION_ERROR:
                return org.saturn.splash.loader.error.AdErrorCode.CONNECTION_ERROR;
            case NETWORK_INVALID_REQUEST:
                return org.saturn.splash.loader.error.AdErrorCode.NETWORK_INVALID_REQUEST;
            case NETWORK_TIMEOUT:
                return org.saturn.splash.loader.error.AdErrorCode.NETWORK_TIMEOUT;
            case SERVER_ERROR:
                return org.saturn.splash.loader.error.AdErrorCode.SERVER_ERROR;
            case NATIVE_RENDERER_CONFIGURATION_ERROR:
                return org.saturn.splash.loader.error.AdErrorCode.NATIVE_RENDERER_CONFIGURATION_ERROR;
            case NATIVE_ADAPTER_NOT_FOUND:
                return org.saturn.splash.loader.error.AdErrorCode.NATIVE_ADAPTER_NOT_FOUND;
            case NETWORK_NO_FILL:
                return org.saturn.splash.loader.error.AdErrorCode.NETWORK_NO_FILL;
            case INTERNAL_ERROR:
                return org.saturn.splash.loader.error.AdErrorCode.INTERNAL_ERROR;
            default:
                return org.saturn.splash.loader.error.AdErrorCode.UNSPECIFIED;
        }
    }

}
