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
#-keep class com.jsongo.core_mini.common.ConstantsKt { *; }

# Android Gradle 插件3.4版本开始 支持R8
# 使用R8后，无需手动添加混淆的三方库： okhttp retrofit okio rxjava

# ok : Glide EasyPhotos

# 待确认  okdownload saf_log hutool ImagePreview RxPermissions androidx相关 kotlin wcdb gson qmui(ok)

# region androidx 相关混淆
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
# endregion

-dontwarn javax.annotation.**
-dontwarn javax.inject.**

#----------- gson ----------------
-keepattributes EnclosingMethod
-keep class com.google.gson.** {*;}
-keep class com.google.**{*;}
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.qiancheng.carsmangersystem.**{*;}

# EasyPhotos
-keep class com.huantansheng.easyphotos.models.** { *; }
# end EasyPhotos

# gilde
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
#  *** rewind();
#}
# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
# end gilde

-keep class * extends android.app.Activity{
    public *;
}

-keep class * extends androidx.fragment.app.Fragment{
    public *;
}

-keep class * extends android.view.View{
    public *;
}

# custom
-keep class * extends android.app.Application{
    *;
}
-keep class com.jsongo.core_mini.CoreMini{
    *;
}
## ipage
-keep class com.jsongo.core_mini.base_page.*{
    *;
}
## common
-keep class com.jsongo.core_mini.common.BusEvent{
*;
}
-keep class com.jsongo.core_mini.common.*{
    public *;
}
## widget 的ui接口
-keep interface com.jsongo.core_mini.widget.*{
    public *;
}
## RxToast
-keep class com.jsongo.core_mini.widget.RxToast{
    public *;
}
## imagepreview
-keep class com.jsongo.core_mini.widget.imagepreview.*{
    public *;
}
-keep class com.jsongo.core_mini.widget.imagepreview.ThumbViewInfo{
    *;
}
## util中所有public
-keep class com.jsongo.core_mini.util.**{
    public *;
}
# end custom