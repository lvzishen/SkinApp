package com.ads.lib.commen;

import android.app.Activity;

import androidx.annotation.NonNull;

public interface LifecycleListener {

    void onCreate(@NonNull Activity activity);

    void onStart(@NonNull Activity activity);

    void onPause(@NonNull Activity activity);

    void onResume(@NonNull Activity activity);

    void onRestart(@NonNull Activity activity);

    void onStop(@NonNull Activity activity);

    void onDestroy(@NonNull Activity activity);

    void onBackPressed(@NonNull Activity activity);
}
