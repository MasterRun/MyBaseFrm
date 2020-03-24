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



#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------

-keep class com.jsongo.ui.component.SettingListFragment.** { *; }
-keep class com.jsongo.core.mvp.base.** { *; }

#-------------------------------------------------------------------------

#---------------------------------2.第三方包-------------------------------

# aspectjtr
-keep class org.aspectj.** { *; }


# 保留Google GSON相关API:
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *;}

#EasyPhoto
-keep class com.huantansheng.easyphotos.models.** { *; }


#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#wcdb
-keep class com.tencent.wcdb.** { *; }

#okio
-dontwarn org.codehaus.mojo.animal_sniffer.*
# okhttp
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-dontwarn okhttp3.internal.platform.ConscryptPlatform
#retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#liulishuo okdienload
-keepnames class com.liulishuo.okdownload.core.connection.DownloadOkHttp3Connection
-keep class com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite {
        public com.liulishuo.okdownload.core.breakpoint.DownloadStore createRemitSelf();
        public com.liulishuo.okdownload.core.breakpoint.BreakpointStoreOnSQLite(android.content.Context);
}

#-------------------------------------------------------------------------

#---------------------------------3.与js互相调用的类------------------------

#ajs
-keep public class com.jsongo.ajs.** { *; }


#-------------------------------------------------------------------------

#---------------------------------4.反射相关的类和方法-----------------------

-keepattributes *Annotation*
-keep class kotlin.** { *; }
-keep class org.jetbrains.** { *; }

-keep class com.vondear.** { *; }

-keep class com.qmuiteam.qmui.widget.QMUITopBarLayout { *; }

# aop注解
-keep class com.jsongo.mybasefrm.aop.** { *; }
-adaptclassstrings
-keepattributes InnerClasses, EnclosingMethod, Signature, *Annotation*
-keepnames @org.aspectj.lang.annotation.Aspect class * {
    ajc* <methods>;
}

#core
-keep class com.jsongo.core.annotations.** { *; }

#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------


#-------------------------------------------基本不用动区域--------------------------------------------
#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity { *; }
-keep class * extends android.support.v4.app.Fragment { *; }
-keep class * extends androidx.fragment.app.Fragment { *; }
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep class androidx.** {*;}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
#----------------------------------------------------------------------------

#---------------------------------webview------------------------------------
-keepclassmembers class android.webkit.WebView {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------