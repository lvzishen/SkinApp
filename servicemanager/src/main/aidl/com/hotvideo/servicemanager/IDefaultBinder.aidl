// IDefaultBinder.aidl
package com.hotvideo.servicemanager;
import android.content.Intent;

// Declare any non-default types here with import statements

interface IDefaultBinder {
   void startCommand(in Intent intent);
   boolean isRunning(String serviceKey);
}
