package org.thanos.netcore.utils;

import android.util.Base64;
import android.util.Log;

import org.thanos.netcore.internal.MorningDataCore;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
    private static final String TAG = MorningDataCore.LOG_PREFIX + "AESUtils";

    public static String encrypt(String strIn) {
        if (strIn == null || strIn.length() == 0) {
            return null;
        }
        SecretKeySpec skeySpec = getKey();
        byte[] encrypted = null;
        try {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(strIn.getBytes());
        } catch (Exception e) {
            if (MorningDataCore.DEBUG) {
                Log.e(TAG, "encrypt: ", e);
            }
        }
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    private static Cipher getCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance("AES/ECB/PKCS5Padding");
    }

    public static String decrypt(String strIn) {
        if (strIn == null || strIn.length() == 0) {
            return null;
        }
        SecretKeySpec skeySpec = getKey();
        byte[] original = null;
        try {

            Cipher cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = Base64.decode(strIn, Base64.DEFAULT);
            original = cipher.doFinal(encrypted1);
        } catch (Exception e) {
            if (MorningDataCore.DEBUG) {
                Log.e(TAG, "decrypt: ", e);
            }
        }
        return new String(original);
    }

    private static SecretKeySpec getKey() {
        String KEY = "$m^c(u)rr@y=.!";
        return new SecretKeySpec(get16Bytes(KEY), "AES");
    }

    private static byte[] get16Bytes(String key) {// 空位补0
        byte[] arrBTmp = key.getBytes();
        byte[] realKey = new byte[16]; // 创建一个空的16位字节数组（默认值为0）
        for (int i = 0; i < arrBTmp.length && i < realKey.length; i++) {
            realKey[i] = arrBTmp[i];
        }
        return realKey;
    }
}
