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


#Oath
-keepclassmembers class com.millennialmedia.** {
public *;
}
-keep class com.millennialmedia.**

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

