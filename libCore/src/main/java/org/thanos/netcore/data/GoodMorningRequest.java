package org.thanos.netcore.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import org.interlaken.common.XalContext;
import org.interlaken.common.utils.LocationData;
import org.interlaken.common.utils.PackageInfoUtil;
import org.interlaken.common.utils.SimcardUtils;
import org.interlaken.common.utils.TimeZoneUtil;
import org.thanos.netcore.bean.requestbean.AppInfo;
import org.thanos.netcore.bean.requestbean.BaseProtocol;
import org.thanos.netcore.bean.requestbean.Device;
import org.thanos.netcore.bean.requestbean.Location;
import org.thanos.netcore.bean.requestbean.RequestAllData;
import org.thanos.netcore.internal.MorningDataCore;
import org.thanos.netcore.internal.requestparam.BaseRequestParam;
import org.thanos.netcore.utils.AESUtils;
import org.zeus.model.AbstractZeusPostRequest;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okio.BufferedSink;

import static org.thanos.netcore.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-11.
 */
public class GoodMorningRequest extends AbstractZeusPostRequest {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "Request";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
    private final String url;
    private BaseRequestParam baseRequestParam;
    private Context context;
    private String requestBodyString;

    public GoodMorningRequest(Context context, @NonNull BaseRequestParam baseRequestParam, String url) {
        this.context = context;
        this.baseRequestParam = baseRequestParam;

        String protocolType = MorningDataCore.isProtocolEncrypt() ? "dson" : "json";
        if (url.contains("?")) {
            this.url = url + "&protocol=" + protocolType;
        } else {
            this.url = url + "?protocol=" + protocolType;
        }
    }

    private static Location createLocationInfo() {
        //纬度
        double latitude = 0;
        //经度
        double longitude = 0;
        //高度
        double altitude = 0;
        //精确度
        double accuracy = 0;
        LocationData locationData = LocationData.getLocationDataProcessSafety(XalContext.getContext(), false);
        if (locationData != null) {
            latitude = locationData.latitude;
            longitude = locationData.longitude;
            altitude = locationData.altitude;
            accuracy = locationData.accuracy;
        }
        Location location = new Location();
        location.accuracy = accuracy;
        location.altitude = altitude;
        location.latitude = latitude;
        location.longitude = longitude;
        location.localTime = DATE_FORMAT.format(new Date(System.currentTimeMillis()));
        location.localZone = String.valueOf(TimeZoneUtil.getOffset(TimeUnit.MINUTES));
        return location;
    }

    private AppInfo createAppInfo(Context context) throws IllegalArgumentException {
        AppInfo appInfo = new AppInfo();
        appInfo.channelId = XalContext.getChannelId();
        appInfo.installTime = PackageInfoUtil.getSelfFirstInstallTime(context);
        appInfo.updateTime = PackageInfoUtil.getSelfLastUpdateTime(context);
        appInfo.module = baseRequestParam.module;
        appInfo.packageName = context.getPackageName();
        int productID = MorningDataCore.getThanosCoreConfig().getProductID();
        if (productID < 0) {
            throw new IllegalArgumentException("productID 不能为负数");
        }
        appInfo.product = MorningDataCore.getThanosCoreConfig().getProductID();
        appInfo.versionCode = XalContext.getVersionCode();
        appInfo.versionName = XalContext.getChannelId();
        return appInfo;
    }

    private Device createDeviceInfo(Context context) {
        Device device = new Device();
        device.clientId = XalContext.getClientId();
        device.locale = Locale.getDefault().toString();
        device.mccCode = SimcardUtils.getSimMccCountryCode(context);
        device.newsCountry = MorningDataCore.getNewsCountry(baseRequestParam.onlyAcceptUserChooseNewsCountry);
        if (DEBUG) {
            String ip = MorningDataCore.getTestProp().getProperty("ip");
            if (!TextUtils.isEmpty(ip)) {
                device.ip = ip;
            }
        }
        return device;
    }

    public BaseRequestParam getBaseRequestParam() {
        return baseRequestParam;
    }

    @Override
    public void preBuildBody() throws IOException {
        super.preBuildBody();
        RequestAllData requestParam = generateRequestParam();
        if (requestParam == null) {
            throw new IOException("Generate Request Param FAIL");
        }
        requestBodyString = new Gson().toJson(requestParam);
        if (DEBUG) {
            Log.i(TAG + "." + getModuleName(), "preBuildBody: " + requestBodyString);
        }
        if (MorningDataCore.isProtocolEncrypt()) {
            String encrypted = AESUtils.encrypt(requestBodyString);
            requestBodyString = URLEncoder.encode(encrypted, "utf-8");
        }
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/json");
    }

    @Override
    public void writeTo(BufferedSink bufferedSink) throws IOException {
        bufferedSink.writeString(requestBodyString, Charset.forName("UTF-8"));
        bufferedSink.flush();
    }

    private RequestAllData generateRequestParam() {
        RequestAllData requestAllData = new RequestAllData();
        requestAllData.location = createLocationInfo();
        requestAllData.device = createDeviceInfo(context);
        requestAllData.appInfo = createAppInfo(context);
        requestAllData.protocol = createProtocol();
        return requestAllData;
    }

    private BaseProtocol createProtocol() {
        return baseRequestParam.createProtocol();
    }

    @Override
    public String getServerUrl() {
        if (DEBUG) {
            Log.d(TAG, "getServerUrl() called, " + this.url);
        }
        return this.url;
    }

    @Override
    public String getModuleName() {
        return baseRequestParam.requestModuleName;
    }


}
