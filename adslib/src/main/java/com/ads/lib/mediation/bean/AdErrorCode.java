package com.ads.lib.mediation.bean;

public enum  AdErrorCode {

    ESULT_0K("200", "result ok"),
    UNSPECIFIED("1003", "Unspecified error occurred."),
    NETWORK_RETURN_NULL_RESULT("1005", "return null result,or parse failed"),
    IMAGE_DOWNLOAD_FAILURE("1000", "Unable to download images associated with ad."),
    CONNECTION_ERROR("1002", "Network is unavailable."),
    NETWORK_INVALID_REQUEST("1006", "received invalid request."),
    NETWORK_TIMEOUT("1007", "failed to respond: timout"),
    SERVER_ERROR("1011", "Server Error"),
    NATIVE_RENDERER_CONFIGURATION_ERROR("1013", "A required renderer was not registered for the Custom Event Native."),
    NATIVE_ADAPTER_NOT_FOUND("1012", "Unable to find Custom Event Native."),
    NETWORK_NO_FILL("1008", "No Fill"),
    INTERNAL_ERROR("1010", "internal error."),
    USER_CANCEL("1018", "user cancel."),
    AD_POSITION_CLOSED("1020", "ad position is closed.");


    public String message;
    public String code;


    AdErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "ErrorCode{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
