package com.baselib.bitmap.core;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.baselib.utils.ModuleConfig;

import java.io.FileDescriptor;

public class BitmapDecoder {

    private static final boolean DEBUG = ModuleConfig.DEBUG;
    private static final String TAG = "BitmapDecoder";

    private BitmapDecoder() {
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            BitmapFactory.decodeResource(res, resId, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        } catch (Throwable t) {
            if (DEBUG) {
                Log.e(TAG, "decodeSampledBitmapFromResource | ", t);
            }
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int reqWidth, int reqHeight) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        } catch (Throwable t) {
            if (DEBUG) {
                Log.e(TAG, "decodeSampledBitmapFromDescriptor | ", t);
            }
            return null;
        }
    }

    public static Bitmap decodeSampledBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPurgeable = true;
            BitmapFactory.decodeByteArray(data, offset, length, options);
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(data, offset, length, options);
        } catch (Throwable t) {
            if (DEBUG) {
                Log.e(TAG, "decodeSampledBitmapFromByteArray | ", t);
            }
            return null;
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }

            final float totalPixels = width * height;

            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }
}
