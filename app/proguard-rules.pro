# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/fookwood/android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#---------------------------------基本指令区----------------------------------
-dontwarn org.apache.commons.logging.**
#忽略警告
-ignorewarnings

# 代码混淆压缩比，在0~7之间
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
#指定不忽略包可见的库类成员(字段和方法)。
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
#指定处理期间打印更多相关信息
-verbose
#指定要启用和禁用的优化，在更精细的水平。只有当优化适用。 混淆时采用的算法(google推荐，一般不改变)
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
# 避免混淆Annotation、内部类、泛型、匿名类
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod
# 避免混淆泛型
-keepattributes Signature
# 重命名抛出异常时的文件名称
-renamesourcefileattribute SourceFile
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
# 保留R下面的资源
-keep class **.R$* {*;}
# 保留四大组件，自定义的Application等这些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
# 保留support包
-keep class android.support.** {*;}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}
# 保留在Activity中的方法参数是view的方法，这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
public void *(android.view.View);
}
# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
public static **[] values();
public static ** valueOf(java.lang.String);
}

-keep public class * extends android.view.View{
*** get*();
void set*(***);
public void (android.content.Context);
public void (android.content.Context, android.util.AttributeSet);
public void (android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
public void (android.content.Context, android.util.AttributeSet);
public void (android.content.Context, android.util.AttributeSet, int);
}
# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
public static final android.os.Parcelable$Creator *;
}
# 保留Serializable序列化类不被混淆
-keepclassmembers class * implements java.io.Serializable {
static final long serialVersionUID;
private static final java.io.ObjectStreamField[] serialPersistentFields;
private void writeObject(java.io.ObjectOutputStream);
private void readObject(java.io.ObjectInputStream);
java.lang.Object writeReplace();
java.lang.Object readResolve();
}
#assume no side effects:删除android.util.Log输出的日志
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    #public static *** d(...);
    public static *** i(...);
    public static *** w(...);
    #public static *** e(...);
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

#-------------------------------------------定制化区域----------------------------------------------
#---------------------------------1.实体类---------------------------------
# Gson
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.jiuyue.user.entity.** { *; }
-keep class com.jiuyue.user.net.** { *; }
-keep class com.jiuyue.user.enums.** { *; }
-keep class com.tencent.qcloud.tuikit.tuichat.bean.message.** { *; }

#-------------------------------------------------------------------------

#---------------------------------2.与js互相调用的类------------------------

#-------------------------------------------------------------------------

#---------------------------------3.反射相关的类和方法-----------------------
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
public static ** bind(***);
public static ** inflate(...);
}
#----------------------------------------------------------------------------

#---------------------------------4.第三方包-------------------------------
# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrolambda
-dontwarn java.lang.invoke.*

# Universal-Image-Loader-v1.9.5
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *; }
-keep class androidx.viewpager.widget.** { *; }

#LiveEventBus
-dontwarn com.jeremyliao.liveeventbus.**
-keep class com.jeremyliao.liveeventbus.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.arch.core.** { *; }

#高德地图
#导航 V8.1.0及以后：
-keep class com.amap.api.navi.**{*;}
-keep class com.alibaba.idst.nui.* {*;}
-keep class com.google.**{*;}
#搜索：
-keep   class com.amap.api.services.**{*;}
#3D地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
#定位：
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}


# banner 的混淆代码
-keep class com.youth.banner.** {
    *;
 }

# xpopup 的混淆代码
-dontwarn com.lxj.xpopup.widget.**
-keep class com.lxj.xpopup.widget.**{*;}

# 状态栏混淆
-keep class com.zackratos.ultimatebarx.ultimatebarx.** { *; }
-keep public class * extends androidx.fragment.app.Fragment { *; }

#极光推送
-dontoptimize
-dontpreverify
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

#Tencent IM
-keep class com.tencent.imsdk.** { *; }

#-------------------------------------------------------------------------

