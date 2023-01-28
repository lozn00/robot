package cn.qssq666.robot.app;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.Toast;

//import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.MobclickAgent;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.inject.Inject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;
import cn.jpush.android.api.JPushInterface;
import cn.qssq666.MultiDexApplication;
import cn.qssq666.db.DBUtils;
import cn.qssq666.mymulti.QSSQMultiDex;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.activity.ErrorActivity;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.utils.AppUtils;
import cn.qssq666.robot.utils.InitUtils;
import cn.qssq666.robot.utils.LogUtil;
import cn.qssq666.robot.utils.SPUtils;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import io.github.kbiakov.codeview.classifier.CodeProcessor;

/**
 *
 *  implements HasActivityInjector
 * Created by luozheng on 2017/3/10.  qssq.space
 */

public class AppContext extends MultiDexApplication {

    private static final String TAG = "AppContext";


    @Inject
    DispatchingAndroidInjector<Activity> dispatchingActivityInjector;

    public static long getStartupTime() {
        return mStartupTime;
    }


    public static long mStartupTime;

    public static DBUtils getDbUtils() {
        if (dbUtils == null) {
            return RobotContentProvider.getDbUtils();
        }
        return dbUtils;
    }

    static {
        LogUtil.importPackage();

    }


    public static double getScore() {
        return 10000;
    }


    public static boolean isVip() {
        return true;
    }

    public static boolean isVip2() {
        return true;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        if (!BuildConfig.AS_PLUGIN) {
//            QSSQMultiDex.IS_VM_MULTIDEX_CAPABLE=false;
            QSSQMultiDex.install(this);
//            MultiDex.install(this);
        }
    }

//    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    public static DBUtils dbUtils;

    public static String getStr(int id) {
        return AppContext.getInstance().getString(id);
    }

    private static AppContext instance;

    public static AppContext getInstance() {
        return instance;
    }

    public static Context getContext() {
        if (instance == null) {
            return AppUtils.getApplication();
        } else {
            return instance;
        }
    }

    static Handler handler = new Handler(Looper.getMainLooper());

    public static Handler getHandler() {
        return handler;
    }

  /*  private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        if (AppUtils.runxposed()) {
            return;
        }
        instance = AppContext.this;
        CustomActivityOnCrash.setErrorActivityClass(ErrorActivity.class);
        CustomActivityOnCrash.install(AppContext.this);
        CodeProcessor.init(this);//某个开发工具

        doinit();
        if (this.getPackageName().equals(AppUtils.getCurProcessName(this))) {
            String username = SPUtils.getValue(AppContext.this, "username", "");
        }




    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {
        }
    }
    private void doinit() {
        dbUtils = new DBUtils(AppContext.this);
        initDbInfo(dbUtils);
      /*  if (result) {
            RobotUtil.insertDefaulWhiteNames(dbUtils);
        }
*/
        //LogUtil.writeLog("AppContext服务OnCreate");


        if (!BuildConfig.DEBUG) {


        }

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
        mStartupTime = System.currentTimeMillis();

        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        if (BuildConfig.DEBUG) {
//            initLeakCanary();

      /*      StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()//监测所有内容
                    .penaltyLog()//违规对log日志
                    .penaltyDeath()//违规Crash
                    .build());
*/
//            startStrictModeVmPolicy();

            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()/*磁盘读取操作检测*/
//                    .detectDiskWrites()/*检测磁盘写入操作*/
                    .detectNetwork() /*检测网络操作*/
                    /*也可以采用detectAll()来检测所有想检测的东西*/
                    .penaltyLog().build());
        }

//        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
//        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, "5ba725abf1f55659da0001b8");
    }

    public static void startStrictModeVmPolicy() {
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectActivityLeaks()/*检测Activity内存泄露*/
                .detectLeakedClosableObjects()/*检测未关闭的Closable对象*/
                .detectLeakedSqlLiteObjects() /*检测Sqlite对象是否关闭*/
                /*也可以采用detectAll()来检测所有想检测的东西*/
                .penaltyLog().build());
    }

    public static void startStrictModeThreadPolicy() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()/*磁盘读取操作检测*/
                .detectDiskWrites()/*检测磁盘写入操作*/
                .detectNetwork() /*检测网络操作*/
                /*也可以采用detectAll()来检测所有想检测的东西*/
                .penaltyLog().build());
    }

    public static void initDbInfo(DBUtils currentDbUtils) {
        currentDbUtils.setGetDeclared(true);
        InitUtils.initTableAndInsertDefaultValue(currentDbUtils);



    }


    public static void showToast(String toast) {
        showToast(instance, toast);
    }

    public static void showToast(Context context, String str) {
        if (toastObj == null) {
            toastObj = Toast.makeText(context, str, Toast.LENGTH_SHORT);

        } else {
            toastObj.setText(str);
        }


        toastObj.show();
    }

    public static Toast toastObj;
}
