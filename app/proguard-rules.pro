-dontoptimize
-dontpreverify

#-assumenosideeffects class cn.qssq666.robot.utils.LogUtil {
#    public *;
#}
#删除日志类 必须http://blog.csdn.net/a2241076850/article/details/78391536 -dontoptimize
 -assumenosideeffects class cn.qssq666.robot.utils.LogUtil {
     public static void writeLog(...);
 }

-keep class cn.qssq666.robot.utils.LogUtil {
    public *;
}
-keep class  java.io.PrintStream {
      public *** println(...);
      public *** print(...);
  }

#指定class混淆字典
-classobfuscationdictionary  classzhaocha.txt
#指定模糊字典 包名全部没混淆shit
#-packageobfuscationdictionary packagezhaocha.txt

#-printmapping mapping.txt 自定外部混淆字典
-obfuscationdictionary zhaocha.txt

#混淆的时候大量使用重载，多个方法名使用同一个混淆名，但
#-overloadaggressively 但是导致出现了问题。
#指定相同的混淆名对应相同的方法名，不同的混淆名对应不同的方法名。如果不设置这个选项，
-useuniqueclassmembernames
#有重新命名的包都重新打包，并把所有的类移动到packagename包下面。如果没有指定packagename或者packagename为""，那么所有的类都会被移动到根目录下
#-flatternpackagehierarchy cn.qssq666.util  Unknown option '-flatternpackage
#所有包移动到指定位置
-repackageclasses cn.qssq666.robot.business
#-repackageclasses cn.qssq666.robot
#-repackageclasses cn.qssq666.robot

#*    任意多个字符，不含package分隔符(.)
#**   任意多个字符，含package分隔符(.)    匹配非基本数据类型和非数组类型
# ***  匹配任意类型 ...biaoshi canshu renyi leix

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\dev\sdk/tools/proguard/proguard-android.txt
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

#-keep class * {
#@BWN <fields>;
#}
#-keepclassmembers class * {
#@BWN <methods>;
#

# 并保留源文件名为"Proguard"字符串，而非原始的类名 并保留行号 // blog from sodino.com
#-keepattributes LineNumberTable
-keepattributes SourceFile,LineNumberTable, Singature ,*Annotation # //不混淆注释
#-renamesourcefileattribute Proguard
-optimizations !field/*
-optimizationpasses 7
-keep class cn.qssq666.CoreLibrary{
native <methods>;
 public static *** getNewClass();
 }
 -keep class androidx.core.content.FileProvider{*;}
-keep class cn.qssq666.robot.activity.MainActivity{
 public static *** hasHook();
}
 #ote: there were 23 duplicate class definitions.
 #混淆时不会产生大小写混合的类名

 -dontusemixedcaseclassnames
 ## 不混淆第三方引用的库
 -dontskipnonpubliclibraryclasses
 -verbose
 -ignorewarnings

-keep public class java.nio.**{*;}
-keep class android.support.test.espresso.core.deps.guava.cache.**{*;}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class cn.qssq666.robot.R$*{
public static final int *;
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

 -keep class  sun.misc.** {*;}
 -dontwarn sun.mis.**
 -keep class  android.** {*;}
 -keep class com.umeng.analytics.**{*;}
  -dontwarn android.**
 -keep class  com.squareup.** {*;}
   -dontwarn com.squareup.**
 -keep class  org.hamcrest.** {*;}
    -dontwarn org.hamcrest.**
 -keep class  junit.rules.** {*;}
    -dontwarn junit.rules.**
 -keep class  org.easymock.** {*;}
    -dontwarn  org.easymock.**

-keep class  java.** {*;}
-keep class  dev.robv.** {*;}
-keep class  org.apache.commons.logging.impl.** {*;}
-keep class  dalvik.system.** {*;}
-keep class  com.android.internal.** {*;}
-keep class  com.android.internal.http.multipart.** {*;}
#    关闭混淆
#-dontobfuscate

#解决 duplicateclass 然并卵 Error:Uncaught translation error: com.android.dex.util.ExceptionWithContext: name already added: string{"ooooooo000"}
#-dontusemixedcaseclassnames
#-dontskipnonpubliclibraryclasses
#-verbose
#-ignorewarnings






-dontwarn org.hamcrest.integration.**
-dontwarn com.tencent.**
-dontwarn android.test.**
#-keep class cn.qssq666.robot.business.RobotContentProvider{ *; }
#不预校验 加快编译速度
-dontpreverify
-ignorewarnings

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
#不混淆android-async-http(这里的与你用的httpClient框架决定)
-keep class com.loopj.android.http.**{*;}

# not static method asAttributeSet  oirg/xmlpull/v1/oooooOo

# 保留support下的所有类及其内部类
#-keep class android.support.** {*;}
#-keep class cn.qssq666.robot.BR.** {*;}
#-keep class com.androidx.databinding.library.baseAdapters.BR.** {*;}


# 保留继承的
-keep public class * extends android.support.v4.**
-keep class android.support.v4.**{*;}
-keep public class * extends android.support.v7.**
#-keep public class * extends androidx.support.annotation.**
#
#-keep public class *implements java.lang.annotation.Annotation {
#  *;
#}

-keep class android.**{*;}
-keep class java.**{*;}
-keep class java.awt**{*;}
-keep class java.applet**{*;}
-keep class javax.**{*;}
-keep class javax.swing.**{*;}
-keep class org.xmlpull.**{*;}

# 保留R下面的资源
-keep class **.R$* {*;}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

-keepclassmembers class * extends android.preference.PreferenceActivity{
   *** isValidFragment(...);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# 保留Parcelable序列化类不被混淆
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# 保留Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
#-keepclassmembers class * {
#    void *(**On*Event);
#    void *(**On*Listener);
#}

# 移除Log类打印各个等级日志的代码，打正式包的时候可以做为禁log使用，这里可以作为禁止log打印的功能使用
# 记得proguard-android.txt中一定不要加-dontoptimize才起作用
# 另外的一种实现方案是通过BuildConfig.DEBUG的变量来控制
#-assumenosideeffects class android.util.Log {
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}e

#-----------处理实体类---------------
# 在开发的时候我们可以将所有的实体类放在一个包内，这样我们写一次混淆就行了。
-keep public class cn.qssq666.robot.bean.**{*;}

#-keepclassmembers public class cn.qssq666.robot.bean.**{*;}
#cn.qssq666.robot.plugin.sdk.interfaces
#cn.qssq666.robot.plugin.sdk.interfaces
#-keepclasseswithmembers class cn.qssq666.robot.plugin.sdk.cn.qssq666.robot.selfplugin.**{*;}
#-keep  class cn.qssq666.robot.plugin.sdk.cn.qssq666.robot.selfplugin.IRobotContentProvider{*;}S
#-keep class  cn.qssq666.robot.selfplugin.IRobotContentProvider{*;}
-keep class cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface{*;}
-keep class cn.qssq666.plugindemo.**{*;}
-keepclassmembers class *  implements cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface {*;}
-keep  class cn.qssq666.robot.selfplugin.**{*;}
-keep class  cn.qssq666.robot.plugin.sdk.interfaces.**{*;}
#-keep public class cn.qssq666.robot.selfplugin.IRobotContentProvider{*;}#实际上只报货类自己不被混淆 除非写的是  直接 ？？非继承?
#-keep class  cn.qssq666.robot.selfplugin.IRobotContentProvider$IContentProviderNotify{*;}
-keep  class cn.qssq666.robot.plugin.js.lib.**{*;}


#-keep class * extends cn.qssq666.robot.selfplugin.IRobotContentProvider {
#     void addObserver(...);
#     void clearObserver();
#     Context getProxyContext();
#     void setProxyContext(...);
#     void interceptNotifyChanage(...);
#     public ** query(...);
#     public ** getType(...);
#     public Uri insert(...);
#     public boolean onCreate();
#     public void notifyChange(...);
#     public int delete(...);
#     public int update(...);
#     public String getPluginInfo();
#     public void setProxyResources(...);
#     public void reload();
#       public ** getPluginList();
#         public ** getResources();
#         public ** getRobotContext();
#     public void testApi();
#}

-keepclasseswithmembers class cn.qssq666.redpacket.Main {
    public *** handleLoadPackage(...);
#免重启调用机制实现,就是通过调用这个然后再调用hook
    public *** handleLoadPackageFromCache(...);
    public *** initZygote(...);
}
#-keep public class cn.qssq666.robot.bean.** {
#    public void set*(***);
#     *** get*();
#    public *** is*();
#}
#----event模型忽略

#-keep public class cn.qssq666.robot.event.**{*;}


#=============eventbus+===========
# org.greenrobot.eventbus
#-keep class org.greenrobot.eventbus.** {*;}
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep class org.greenrobot.eventbus.**{*;}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(Java.lang.Throwable);
}
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}
#@===================okhttp
# OkHttp3

-dontwarn okhttp3.**
-keep class okhttp3.** { *;}
-dontwarn okio.**
-keep class okio.** { *;}
#aused by: java.lang.RuntimeException: Stub! at cn.qssq666.robot.business.o0ooooo00.getDefault(Proguard:5)
-keep class javax.**{*;}

# =====================fastjson
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**
#-dontnote
-keep class org.apache.common.** { *;}
-dontwarn org.apache.common.**
-keep class javax.net.ssl.** { *;}
-dontwarn javax.net.ssl.**
-keep class java.utill.** { *;}
-dontwarn java.util.**
-keep class com.google.android.gms.ads.** { *;}
-dontwarn com.google.android.gms.ads.**
#fastjson
#-libraryjars libs/fastjson-1.1.46.android.jar


-dontwarn com.alibaba.fastjson.**En
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses

-keep class com.alibaba.fastjson.**{*;}
-keep class * implements java.io.Serializable { *; }
#Warning:Exception while processing task java.io.IOException: java.lang.IllegalArgumentException: Stack size becomes negative after instruction [0] pop in [android/support/v4/os/CancellationSignal.waitForCancelFinishedLocked()V]
-dontwarn android.support.v4.os.**
-keep class android.support.v4.os.**{*;}


#友盟统计
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
#修复友盟的某个bug
-keep class org.json.**{*;}
# 修复广告类 business getDefault广告抛出异常问题
-keep class com.google.android.gms.**{*;}

-keep public class cn.qssq666.robot.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#org.mozilla.javascript
-keep class org.mozilla.**{*;}
-keep class org.luaj.***{*;}
-keep class kotlin.**{*;}
-keep class com.squareup.**{*;}
-keep class io.reactivex.**{*;}
-keep class rx.**{*;}

-keepclassmembers class cn.qssq666.robot.ui.ClassloaderContext{
<methods>;
}
#Stack size becomes negative after instruction [0] pop in [android/support/v4/os/CancellationSignal.waitForCancelFinishedLocked()V]

-dontshrink
#-dontoptimize
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify