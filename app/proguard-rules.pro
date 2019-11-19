-renamesourcefileattribute filemagic
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
-repackageclasses clean

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.database.sqlite.SQLiteOpenHelper {
    public void onDowngrade(...);
}

-keep class com.ultron.rv3.server.RootMain{
	public static void main(...);
}

-keep class com.ultron.rv3.ifs.*{
	public static void main(...);
}

## Needed for Parcelable/SafeParcelable Creators to not get stripped
#-keepnames class * implements android.os.Parcelable {
#    public static final ** CREATOR;
#}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class * implements com.tools.ad.NoProguard {
	public protected <methods>;
}
-keep class com.tools.ad.NoProguard {
	*;
}

-keep public class com.google.android.gms.ads.** {
	public *;
}
-keep public class com.google.ads.** {
	public *;
}

-keep class com.apusapps.launcher.track.Statistics {
    public static final <fields>;    
}

-keep class com.doit.aar.applock.track.AppLockStatisticsConstants {
    public static final <fields>;
}

-keep class android.support.v7.widget.RoundRectDrawable { *; }
-keep class android.support.design.** { *; }


-keep class com.mobvista.** {*; }
-keep interface com.mobvista.** {*; }
-keep class com.facebook.ads.** { *; }
-keep class android.webkit.WebSettings
-dontwarn android.webkit.WebSettings
-dontwarn com.google.android.gms.**

# for facebook sdk start
-keep class com.facebook.** { *; }
-dontwarn com.facebook.BuildConfig

# for facebook sdk  end 


-keep class com.dec.un7z.** { *; }


#######################################################
# for apusapps framework related things
-keep class * implements com.apusapps.fw.proguard.IDoNotObfuscate {
    public protected <methods>;
}
# for apusapps framework related things END

#EventBus greenbots
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}

-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}
# for google ads
-keep class com.apusapps.admob.lib.** { *; }
-keep class com.google.android.gms.internal.** { *; }
-keep class com.google.android.gms.iid.** { *; }
-keep class com.google.android.gms.gcm.** { *; }
-keep class com.google.android.gms.common.** { *; }
# for google ads

-dontwarn com.google.android.gms.**

#for stark sdk
-keep class * extends com.apus.stark.nativeads.CustomEventNative {}
#for stark sdk end

# for Facebook AN SDK
-keep class com.facebook.ads.** { *; }
-keep class android.webkit.WebSettings
-dontwarn android.webkit.WebSettings

-dontwarn com.apus.taskmanager.processclear.ProcessInfoHelper

-keepclassmembers class com.apusapps.launcher.track.StatisticConstants {
    public static final <fields>;    
}
-keepclassmembers class com.apusapps.launcher.track.StatisticConstantsAd {
    public static final <fields>;    
}


-dontwarn com.apusapps.libzurich.**

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.mobvista.** {*; }
-keep interface com.mobvista.** {*; }
-keep class android.support.v4.** { *; }
-dontwarn com.mobvista.**
-keep class **.R$* {
    public static final int mobvista*;
}

-keep class android.content.pm.**{*;}
# crash guard
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

########################################company forced sdk
-dontwarn com.facebook.ads.internal.adapters.**
#-dontwarn org.saturn.stark.**
# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}
# Keep SafeParcelable value, needed for reflection. This is required to support backwards
# compatibility of some classes.
-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keep class * extends org.saturn.stark.nativeads.CustomEventNative {
}

-keep class * implements org.tethys.NoProguard {
   public protected <methods>;
}
-keep class org.tethys.NoProguard {
    *;
}
-keepclassmembers class org.tethys.refactor.HostEvent {
    public static final <fields>;
}
-assumenosideeffects class org.tethys.utils.ALog {
   *;
}
#myTarget
-keep class ru.mail.android.mytarget.** {*;}

-dontwarn org.saturn.stark.nativeads.adapter**
#-dontwarn com.my.target.nativeads.** {*;}
#-dontwarn ru.mail.android.mytarget.**
#-dontwarn org.dions.zurich.**
#-dontwarn ru.mail.android.mytarget.**
-dontwarn com.tools.athene.**

#AVL SDK
-keep class com.avl.engine.**{*;}

#二次激活
-keepclasseswithmembernames,includedescriptorclasses class * {
    native <methods>;
}
-keep class com.apusapps.liblausanne.Lausanne {
    native <methods>;
}
-keepclasseswithmembernames,includedescriptorclasses class com.apusapps.liblausanne.Lausanne {
    public java.nio.ByteBuffer s;
}
-keep class cn.shuzilm.core.Main {
public *;
}
-keepclasseswithmembernames class cn.shuzilm.core.Main {
native <methods>;
}


# myTarget begin
-keep class com.my.target.** {*;}
-dontwarn com.mopub.**
# myTarget end
-dontwarn org.mimas.notify.clean.ad.NotifyCleanAdProp

#for athene
-keep class org.dions.libathene.LoadConfigFileManner { *; }
#for athene end

-keep class com.facebook.ads.NativeAd {*;}
-keep class com.facebook.ads.InterstitialAd {*;}
-keep class com.google.android.gms.ads.InterstitialAd {*;}
-keep class com.google.android.gms.ads.formats.NativeAd {*;}
-keep class org.dions.zurich.AdvertisingItem {*;}
-keep class bolts.Task {*;}
-keep class com.applovin.api.entity.AppLovinAd {*;}
-keep class com.my.target.nativeads.NativeAd {*;}


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
**[] $VALUES;
public *;
}

-keepclassmembers class com.mopub.** { public *; }
-keep public class com.mopub.**
-keep public class android.webkit.JavascriptInterface {}

# Explicitly keep any custom event classes in any package.
-keep class * extends com.mopub.mobileads.CustomEventBanner {}
-keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}


# LMSDK end

-keep public class com.google.android.gms.ads.** {
	public *;
}
-keep public class com.google.ads.** {
	public *;
}

# for facebook sdk start
-keep class com.facebook.** { *; }
# for facebook sdk  end

#for stark sdk
-keep class * extends org.saturn.stark.nativeads.CustomEventNative {}
#for stark sdk end

# for Facebook AN SDK
-keep class com.facebook.ads.** { *; }
-keep class android.webkit.WebSettings
-dontwarn android.webkit.WebSettings
-dontwarn com.facebook.BuildConfig
-dontwarn com.facebook.ads.internal.**


#for stark interstitial start
-keep class org.saturn.stark.interstitial.CustomEventInterstitial { *; }
-keep class * extends org.saturn.stark.interstitial.CustomEventInterstitial {}
#for stark interstitial end
-dontwarn ru.mail.android.mytarget.**
-dontwarn org.dions.zurich.**
-dontwarn org.mimas.notify.clean.track.ILogger
-dontwarn org.mimas.notify.clean.NotifyCleanHelper
-dontwarn org.saturn.stark.nativeads.adapter.**
-dontwarn com.augeapps.battery.view.LockerPreference$Mode


# Keep the names of classes/members we need for client functionality.
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

#EventBus 3.0
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }
#mopub end

# BatMobi sdk
-keep class com.mnt.**{*;}

#locker sdk start
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
  public static java.lang.String TABLENAME;
  }
  -keep class **$Properties

  # If you do not use SQLCipher:
  -dontwarn org.greenrobot.greendao.database.**
  # If you do not use Rx:
  -dontwarn rx.**

  -keep class com.weathersdk.weather.domain.model.**{*;}
  -keep class *.R
  -keepclasseswithmembers class **.R$* {    public static <fields>;}
#  -keep class org.interlaken.**{*;}

  #-keep class com.weathersdk.api.**{*;}
  -keep class com.weathersdk.WeatherApi{
      <methods>;
  }
  -keep class com.weathersdk.WeatherApi{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack{
      <methods>;
  }
  -keep class com.weathersdk.WeatherLauncher{
    <methods>;
  }
  -keep class com.weathersdk.WeatherLauncher$IWeatherLauncher{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack$IWeatherInfo{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack$ICityInfo{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack$IWeatherCacheInfo{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack$IWeatherCacheInfos{
      <methods>;
  }
  -keep class com.weathersdk.IWeatherCallBack$ILocationInfo{
      <methods>;
  }
  -keep class com.weathersdk.ServerException{
      <methods>;
  }
  -keep class com.weathersdk.IError{
      <fields>;
  }
  -keep class com.weathersdk.weather.utils.WeatherUtils{
      <methods>;
  }
  -keepclasseswithmembers class com.weathersdk.WeatherApi$Builder {
         <methods>;
   }
  -keepclasseswithmembers class com.weathersdk.WeatherApi$IBuildParams {
       <fields>;
  }
#locker sdk end
-dontwarn org.saturn.stark.interstitial.adapter.**
  -keep class com.google.i18n.** { *; }
  -keep class com.googlecode.** { *; }
  -keep class android.telephony.** { *; }
  -keep class com.android.internal.telephony.ITelephony { *; }
  -keep class com.android.internal.telephony.ITelephony$Stub { *; }
-keep  class android.os.ServiceManager

-keep class com.weathersdk.weather.dao.**{*;}
#gdpr-start
-keep class com.chaos.** {
    public *;
}

-keep class com.fantasy.guide.jsapi.**{
     public *;
}
-dontwarn com.chaos.engine.js.**
-dontwarn com.fantasy.manager.**
#gdpr-end


#Stark - Reward 混淆
-dontwarn com.unity3d.ads.**
-dontwarn com.adcolony.sdk.**
-dontwarn com.vungle.publisher.**

#Stark - Native 混淆
-dontwarn com.monet.bidder.**
-dontwarn com.inmobi.**
-dontwarn com.mobpower.**

-keep class org.saturn.stark.interstitial.CustomEventInterstitial {
*;
}
-keep class * extends org.saturn.stark.interstitial.CustomEventInterstitial {
}
-keep class * extends org.saturn.stark.nativeads.CustomEventNative {
}
-keep class com.facebook.ads.** { *; }
-keep class android.webkit.WebSettings
-dontwarn android.webkit.WebSettings

-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.ads.** {
   public *;
}
-keepclassmembers class com.mopub.** { public *; }
-keep public class com.mopub.**
-keep public class android.webkit.JavascriptInterface {}

# Explicitly keep any custom event classes in any package.
-keep class * extends com.mopub.mobileads.CustomEventBanner {}
-keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }
-keep class com.applovin.** { *; }
-keep class com.applovin.api.entity.AppLovinAd {*;}

-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.mobpower.**
-keep class com.mobpower.ad.appwall.ui.** {*; }
-keep class com.mobpower.ad.common.ui.** {*; }
-keep class com.power.PowerService {*; }
-keep class com.power.PowerReceiver {*; }

-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
# skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**
# skip AVID classes
-keep class com.integralads.avid.library.* {*;}
-keep class com.mnt.**{*;}
-keep class org.dions.libathene.LoadConfigFileManner { *; }
#branch io
-dontwarn com.google.firebase.appindexing.**
-dontwarn com.crashlytics.android.answers.shim.**

-optimizations "method/inlining/short,method/inlining/unique"


# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile


-keep class android.webkit.WebSettings
-dontwarn android.webkit.WebSettings
-keep class com.facebook.ads.** { *; }

# for Facebook AN SDK
-keep class com.facebook.** { *; }
-dontwarn com.facebook.BuildConfig
-dontwarn com.facebook.ads.internal.**
# for Facebook AN SDK END


# Support for Android Advertiser ID.
-keep class com.google.android.gms.common.GooglePlayServicesUtil {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {*;}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {*;}
# for admob
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep public class com.google.android.gms.internal.zzgw { *; }
-keep public class com.google.android.gms.internal.zzhb { *; }
-keep public class com.google.android.gms.internal.zzhg { *; }
-keep public class com.google.android.gms.internal.zzhj { *; }
-keep public class com.google.android.gms.internal.zzhj$zza { *; }
-keep class com.google.android.gms.internal.zzhj$zza$zza { *; }
-keep public class com.google.android.gms.internal.zzhk { *; }
-keep public class com.google.android.gms.internal.zzhl { *; }
-keep public class com.google.android.gms.internal.zzhl$zza { *; }
-keep class com.google.android.gms.internal.zzhl$zza$zza { *; }
-keep public class com.google.android.gms.internal.zzhm { *; }

-keepattributes SourceFile,LineNumberTable
-keep class com.inmobi.** { *; }
-keep public class com.google.android.gms.**
-dontwarn com.google.android.gms.**
-dontwarn com.squareup.picasso.**
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient{
public *;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info{
public *;
}
# skip the Picasso library classes
-keep class com.squareup.picasso.** {*;}
-dontwarn com.squareup.picasso.**
-dontwarn com.squareup.okhttp.**
# skip Moat classes
-keep class com.moat.** {*;}
-dontwarn com.moat.**
# skip AVID classes
-keep class com.integralads.avid.library.* {*;}

-keep public class com.google.ads.** {
   public *;
}
-dontwarn com.google.android.gms.**
# for admob end

-dontwarn ru.mail.android.mytarget.**
-dontwarn org.mimas.notify.clean.track.ILogger
-dontwarn org.mimas.notify.clean.NotifyCleanHelper
-dontwarn org.saturn.stark.nativeads.adapter.**
-dontwarn com.augeapps.battery.view.LockerPreference$Mode

#for stark interstitial start
-keep class org.saturn.stark.interstitial.CustomEventInterstitial { *; }
-keep class * extends org.saturn.stark.interstitial.CustomEventInterstitial {}
-dontwarn org.saturn.stark.interstitial.adapter.**
#for stark interstitial end

#Stark - Reward 混淆
-dontwarn com.unity3d.ads.**
-dontwarn com.adcolony.sdk.**
-dontwarn com.vungle.publisher.**

#Stark - Native 混淆
-dontwarn com.monet.bidder.**
-dontwarn com.inmobi.**
-dontwarn com.mobpower.**

#for stark sdk
#-keep class org.saturn.stark.nativeads.adapter.FacebookNative { *; }
#-keep class org.saturn.stark.nativeads.adapter.AdmobNative { *; }
-keep class * extends org.saturn.stark.nativeads.CustomEventNative {}
-keep class org.dions.libathene.LoadConfigFileManner { *; }

-dontwarn com.my.target.nativeads.**
-dontwarn com.mopub.**
#for stark sdk end

# for appsflyerlib
-dontwarn com.google.android.gms.ads.**
-dontwarn android.content.pm.**
-keep class android.content.pm.** { *; }
-keep class com.appsflyer.** { *; }
-keep class com.android.internal.app.** { *; }
# for appsflyer sdk end


# LMSDK begin
-keep class * implements org.tethys.NoProguard {
   public protected <methods>;
}
-keep class org.tethys.NoProguard {
    *;
}
-keepclassmembers class org.tethys.HostEvent {
    public static final <fields>;
}
-assumenosideeffects class org.tethys.utils.ALog {
   *;
}

-keep class org.tethys.NoProguard {
    <fields>;
    <methods>;
}
-keepclassmembers class org.tethys.IEventCallback {
    public static final <fields>;
}

# LMSDK end
-keep class com.facebook.ads.InterstitialAd {*;}
-keep class com.google.android.gms.ads.InterstitialAd {*;}
-keep class bolts.Task {*;}
-keep class com.applovin.api.entity.AppLovinAd {*;}
-keep class com.my.target.nativeads.NativeAd {*;}
-keep class com.facebook.ads.NativeAd {*;}
-keep class ru.mail.android.mytarget.** {*;}
-keep class com.my.target.** {*;}
-keep class com.google.android.gms.ads.formats.NativeAd {*;}


-keepclassmembers class com.mopub.** { public *; }
-keep public class com.mopub.**
-keep public class android.webkit.JavascriptInterface {}

# Explicitly keep any custom event classes in any package.
-keep class * extends com.mopub.mobileads.CustomEventBanner {}
-keep class * extends com.mopub.mobileads.CustomEventInterstitial {}
-keep class * extends com.mopub.nativeads.CustomEventNative {}

# Keep methods that are accessed via reflection
-keepclassmembers class ** { @com.mopub.common.util.ReflectionTarget *; }

# Keep glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-optimizations !class/unboxing/enum

-keep class com.tshare.transfer.model.GuidesModuleImpl { *; }
-keep class guide.model.ModuleState { *; }

# BatMobi sdk
-keep class com.mnt.**{*;}
-dontwarn com.mnt.**

#XAL
-keep class org.anticheater.AntiCheater {
    public *;
}
-keep class com.anna.glide.AnnaGlide {
    public *;
}
-keep class org.neptune.ext.download.ApolloDownloader {
    public *;
}

# for mobpower sdk
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn com.mobpower.nativeads.*
-keep class com.mobpower.ad.appwall.ui.** {*; }
-keep class com.mobpower.ad.common.ui.** {*; }
-keep class com.power.PowerService {*; }
-keep class com.power.PowerReceiver {*; }
-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# popup_scene
-keep class org.tethys.popup.module.scene.popup.SceneTiming{*; }
-keep class org.tethys.popup.module.scene.popup.SceneAppChange{*; }
-keep class org.tethys.popup.module.scene.lokcer.SceneUnlock{*; }
-keep class org.tethys.popup.module.openapi.** {*; }

-keep class com.weathersdk.weather.dao.**{*;}

-keep class ru.mail.android.mytarget.** {*;}
-keep class com.my.target.** {*;}
-dontwarn com.mopub.**

-keep class com.applovin.** { *; }
-keep class com.applovin.api.entity.AppLovinAd {*;}

-keep class org.saturn.stark.core.BaseCustomNetWork { *; }
-keep class * extends org.saturn.stark.core.BaseCustomNetWork {}

-keep class org.trade.popupad.module.scene.popup.SceneTiming{*; }
-keep class org.trade.popupad.module.scene.popup.SceneAppChange{*; }
-keep class org.trade.popupad.module.scene.lokcer.SceneUnlock{*; }

# altamob
-keep class com.mobi.** { *; }
-keep class com.altamob.** { *; }
-keepclassmembers class * {   @android.webkit.JavascriptInterface <methods>; }
-dontwarn java.lang.invoke.*
-dontwarn org.saturn.stark.altamob.adapter.**
-dontwarn org.saturn.stark.applovin.adapter.**

-keepclassmembers class com.mopub.nativeads.NativeAd { *; }
-keepclassmembers class com.inmobi.ads.InMobiNative { *; }

-keep class com.mopub.nativeads.NativeAd { *; }
-keep class com.inmobi.ads.InMobiNative { *; }


-keep class com.mopub.mobileads.BannerVisibilityTracker { *; }
-keep class com.mopub.mobileads.BannerVisibilityTracker$BannerVisibilityChecker { *; }
-keep class com.mopub.mobileads.MoPubActivity { *; }
-keep class org.saturn.stark.core.natives.NativeStaticViewHolder { *; }
-keep class com.mopub.mobileads.MoPubView { *; }
-keep class org.saturn.stark.core.natives.NativeStaticViewHolder { *; }

-dontwarn  org.saturn.stark.applovin.adapter.*
-keep class org.saturn.autosdk.ui.AutoShowActivity {
     *;
}
-keep class com.katai.auto.UltronConfig{*; }

# Required to preserve the Flurry SDK
-keep class com.flurry.** { *; }
-dontwarn com.flurry.**
-keepattributes *Annotation*,EnclosingMethod,Signature
-keepclasseswithmembers class * {
   public <init>(android.content.Context, android.util.AttributeSet, int);
 }

 # Google Play Services library
  -keep class * extends java.util.ListResourceBundle {
   protected Object[][] getContents();
}

 -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
  public static final *** NULL;
 }

 -keepnames @com.google.android.gms.common.annotation.KeepName class *
 -keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
  }

 -keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
 }

# Vungle start
-keep class com.vungle.warren.** { *; }
-dontwarn com.vungle.warren.error.VungleError$ErrorCode

# Moat SDK
-keep class com.moat.** { *; }
-dontwarn com.moat.**

# Okio
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Retrofit
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Google Android Advertising ID
-keep class com.google.android.gms.internal.** { *; }
-dontwarn com.google.android.gms.ads.identifier.**
 # Vungle end

 #APPLOVIN START
 -dontwarn com.applovin.**
 -keep class com.applovin.** { *; }
 #APPLOVIN END


# AdColony
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepclassmembers class com.adcolony.sdk.ADCNative** {
    *;
 }
 # Tapjoy
 -keep class com.tapjoy.** { *; }
 -keepattributes JavascriptInterface
 -keepattributes *Annotation*
 -keep class * extends java.util.ListResourceBundle {
 protected Object[][] getContents();
 }
 -keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
 public static final *** NULL;
 }
 -keepnames @com.google.android.gms.common.annotation.KeepName class *
 -keepclassmembernames class * {
 @com.google.android.gms.common.annotation.KeepName *;
 }
 -keepnames class * implements android.os.Parcelable {
 public static final ** CREATOR;
 }
 -keep class com.google.android.gms.ads.identifier.** { *; }
 -dontwarn com.tapjoy.**
 -keep class  com.moat.** { *; }
 #ChartBoost
 -dontwarn com.amazon.**
 -dontwarn org.apache.http.**
 -dontwarn com.chartboost.sdk.impl.**
 -keep class com.chartboost.sdk.** { *; }
 -keep class com.amazon.** {*;}
 -keepattributes *Annotation*

  #Unity
  -keep class android.webkit.JavascriptInterface {
     *;
  }
  -keep class com.unity3d.ads.** {
     *;
  }

   #Pingyin4j
 -dontwarn net.soureceforge.pinyin4j.**
 -dontwarn demo.**
 -keep class com.hp.** { *;}
 -keep class net.sourceforge.pinyin4j.** { *;}
 -keep class demo.** { *;}

 -dontwarn com.taobao.**


 # Thanos
 -keep class org.thanos.**{
    public *;
 }
 -keep class thanos.**{
    public *;
 }


-keep class com.powerdischarge.** { *;}
-keep class com.baselib.ui.views.CustomFontTextView
-keep class com.baselib.ui.views.SwitchButton
 -ignorewarnings

 #billing
 -keep class com.android.vending.billing.**