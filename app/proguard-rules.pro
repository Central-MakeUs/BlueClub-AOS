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

-keep class com.kakao.sdk.**.model.* { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep interface com.kakao.sdk.auth.*
-keep interface com.kakao.sdk.**.*Api
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

 -keep,allowobfuscation,allowshrinking interface retrofit2.Call
 -keep,allowobfuscation,allowshrinking class retrofit2.Response
 -keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

 -keepclasseswithmembers class kotlinx.serialization.json.** {
     kotlinx.serialization.KSerializer serializer(...);
 }
 -keepclassmembers class kotlinx.serialization.json.** {
      *** Companion;
  }
-keepnames class * extends android.os.Parcelable
-keepnames class * extends java.io.Serializable
-keep class org.blueclub.data.model.** { *; }

# To use naver login with proguard
-keep public class com.navercorp.nid.** { public *; }
-keep public class com.navercorp.nid.oauth.** {*;}

# To use naver login profile with proguard
-keep public class com.navercorp.nid.profile.data.** {*;}
