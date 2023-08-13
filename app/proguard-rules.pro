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

-keep class com.startapp.** {
      *;
}

-keep class com.truenet.** {
      *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,
LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**

-dontwarn org.jetbrains.annotations.**
-keep class androidx.** { *; }
-keep class android.support.multidex.** { *; }
-keep class android.support.v4.app.** { *; }
-keep class com.google.android.gms.location.FusedLocationProviderApi { *; }
-keep class com.google.android.gms.location.LocationListener { *; }
-keep class io.fabric.sdk.android.** { *; }
-keep class okio.** { *; }
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keep class com.squareup.okhttp.** { *; }
-keep class com.android.volley.** { *; }
-keep class com.flurry.** { *; }
-keep class org.apache.** { *; }
-keep class com.applovin.** { *; }
-keep class com.google.android.gms.ads.** { *; }
-keep class com.ironsource.** { *; }
-keep class com.fyber.inneractive.** { *; }
-keep class com.vungle.** { *; }
-keep class com.unity3d.ads.** { *; }
-keep class com.unity3d.services.** { *; }
-keep class com.mintegral.msdk.** { *; }
-keep class com.adcolony.sdk.** { *; }
-keep class com.inmobi.** { *; }
-keep class com.safedk.** { *; }
-keep class com.applovin.quality.** { *; }