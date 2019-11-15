package org.thanos.core.data;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.thanos.core.bean.ResponseData;
import org.thanos.core.internal.MorningDataCore;
import org.thanos.core.utils.AESUtils;
import org.zeus.ZeusNetworkCallback;
import org.zeus.ZeusRequestResult;
import org.zeus.parser.AbstractZeusResponseParser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.Response;

import static org.thanos.core.internal.MorningDataCore.DEBUG;

/**
 * Created by zhaobingfeng on 2019-07-12.
 */
public class GoodMorningBaseParser<T extends ResponseData> extends AbstractZeusResponseParser<T> {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "BaseParser";
    private Class<T> responseDataItemClazz;

    public GoodMorningBaseParser(Class<T> responseDataItemClazz) {
        this.responseDataItemClazz = responseDataItemClazz;
    }

    public Class<T> getResponseDataItemClazz() {
        return responseDataItemClazz;
    }

    @Override
    public final ZeusRequestResult<T> parser(Response response) {
        try {
            String body = response.body().string();
            beforeDecrypt(body);
            ZeusRequestResult<T> result = decryptBody(body);
            afterParse(result);
            return result;
        } catch (Exception e) {
            if (DEBUG) {
                Log.e(TAG, "parser: ", e);
            }
        }
        return new ZeusRequestResult<>(ZeusNetworkCallback.ERROR_PROTOCOL_ERROR);
    }

    protected void afterParse(ZeusRequestResult<T> result) {

    }

    private ZeusRequestResult<T> decryptBody(String body) throws UnsupportedEncodingException, JSONException {
        if (MorningDataCore.isProtocolEncrypt()) {
            body = AESUtils.decrypt(URLDecoder.decode(body, "utf-8"));
        }
        if (DEBUG) {
            Log.i(TAG, "parser: " + getRequest().getServerUrl());
            Log.i(TAG, "parser: " + body);
        }
        return onParse(body);
    }

    protected void beforeDecrypt(String body) {
    }

    public final ZeusRequestResult<T> parseFromCache(String body) throws UnsupportedEncodingException, JSONException {
        return decryptBody(body);
    }

    private ZeusRequestResult<T> onParse(String body) throws JSONException {
        T t = ResponseData.create(responseDataItemClazz, new JSONObject(body));
        return new ZeusRequestResult<>(t);
    }
}
