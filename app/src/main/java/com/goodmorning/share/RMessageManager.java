package com.goodmorning.share;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

import com.goodmorning.share.util.RFileHelper;
import com.goodmorning.share.util.RPlatformHelper;

/**
 * Instagram <a href=https://www.instagram.com/developer/mobile-sharing/android-intents/>Instagram
 * Android分享</a>
 */
final public class RMessageManager extends RShare {

    private final String TAG = "RInstagramManager===>";

    private static RMessageManager mManager;

    private RMessageManager() {
    }

    public static RMessageManager getInstance() {
        if (mManager == null) {
            synchronized (RMessageManager.class) {
                if (mManager == null) {
                    mManager = new RMessageManager();
                }
            }
        }
        return mManager;
    }

    public void shareText(Context context,String text) {
        Uri smsToUri = Uri.parse("smsto:");
        Intent mIntent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", text);
        context.startActivity(mIntent);
    }

}
