// ISeviceManager.aidl
package com.hotvideo.servicemanager;
import android.content.Intent;
// Declare any non-default types here with import statements

interface ISeviceManager {
   void startServiceWithIntent(in Intent intent );
   void stopService(in Intent intent);
}
