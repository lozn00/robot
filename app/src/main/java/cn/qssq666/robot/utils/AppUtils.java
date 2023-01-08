package cn.qssq666.robot.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.activity.MainActivity;
import cn.qssq666.robot.activity.click.NotificationAct;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.UpdateBean;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.constants.NetCns;
import cn.qssq666.robot.interfaces.DownloadListener;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.proguardx.user.UserInfo;
import cn.qssq666.robot.webview.WebViewActivity;
import cn.qssq666.tencent5x.X5WebViewActivity;
import cz.msebera.android.httpclient.Header;

/**
 * Created by luozheng on 2017/3/12.  qssq.space
 */

public class AppUtils {
    private static final String TAG = "aPPU";

    public static String removePort(String url) {

        int index = url.lastIndexOf(":");
        int urlIndex = url.indexOf("://");//http://xx:8080 这种情况取后面的
        if (index != -1 && index != urlIndex) {
            return url.substring(0, index);
        }
        return url;
    }

    public static String url2Domain(String url) {
//        String domain = GlobalSettingModel.getInstance().getDomain();
        String domain = "";
        int schameIndex = url.indexOf("//");
        String schame = "";
        int endIndex;
        if (schameIndex != -1) {
            endIndex = url.indexOf("/", schameIndex + 2);
        } else {
            schame = "http://";
            endIndex = url.indexOf("/");
        }

        if (endIndex == -1) {
            endIndex = url.indexOf("?");
        }


        if (endIndex != -1) {
            domain = schame + url.substring(0, endIndex);
        } else {
            domain = schame + url;
        }
        return domain;
    }

    public static Context getApplication() {

        try {
            Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("android.app.ActivityThread");
            Method currentApplication = aClass.getMethod("currentApplication");
            return (Context) currentApplication.invoke(null);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void toWebView(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void toWebViewTX(Context context, String url, String title) {
        Intent intent = new Intent(context, X5WebViewActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("url", url);
        intent.putExtra("title", title == null ? "加载中..." : title);
        context.startActivity(intent);

    }

    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        return queryProcessNameByPid(context, pid);
    }

    public static String queryProcessNameByPid(Context context, int pid) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    public static String getRobotReplyKey(int index) {


        return Cns.ROBOT_KEY + "_" + index;
    }

    public static String getRobotReplySecret(int index) {
        return Cns.ROBOT_SECRET + "_" + index;
    }

    public static String getRobotReplyDefaultRequest(int index) {
        return "request_header" + "_" + index;
    }

    public static String getRealFilePath(Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    public static Uri fixFromFile(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//24
            uri = FileProvider.getUriForFile(AppContext.getInstance(), BuildConfig.APPLICATION_ID + ".fileprovider", file);


        } else {
            uri = Uri.fromFile(file);
        }

        return uri;
    }

    public static Intent getShareFileIntent(File file) {
        Intent intent = new Intent();

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//24
            uri = FileProvider.getUriForFile(AppContext.getInstance(), BuildConfig.APPLICATION_ID + ".fileprovider", file);


        } else {
            uri = Uri.fromFile(file);
        }
        intent.setAction(Intent.ACTION_SEND);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        intent.setType("*/*");
//        intent.setType("video/*;image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setType("application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return intent;
    }


    public static void lauchApp(Context context, String packageName) throws Exception {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        context.startActivity(intent);
    }

    /**
     * /**
     *
     * @param fragment    object or activity 传递错误将抛出异常
     * @param requestCode requestcode 将 会调用
     */
    public static void chooseFile(Object fragment, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        /*
        ntent.setType(“image/*”);
//intent.setType(“audio/*”); //选择音频
//intent.setType(“video/*”); //选择视频 （mp4 3gp 是android支持的视频格式）
//intent.setType(“video/*;image/*”);//同时选择视频和图片
         */
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            if (fragment instanceof Fragment) {
                ((Fragment) fragment).startActivityForResult(Intent.createChooser(intent, "请选择文件"), requestCode);
            } else if (fragment instanceof Activity) {
                ((Activity) fragment).startActivityForResult(Intent.createChooser(intent, "请选择文件"), requestCode);
            } else {
                throw new RuntimeException("参数必须为fragment 或者activity");
            }

        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            AppContext.showToast("无法选择文件,坑呢是文件管理器未安装");
        }
    }

    public static void copy(Context context, String content) {
//   // 从API11开始android推荐使用android.content.ClipboardManager
        // 为了兼容低版本我们这里使用旧版的android.text.ClipboardManager，虽然提示deprecated，但不影响使用。
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        cm.setText(content);
    }


    public static boolean isJSONObject(String str) {
        if (str != null && str.trim().startsWith("{") && str.endsWith("}")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isJSONArray(String str) {
        if (str != null && str.trim().startsWith("[") && str.endsWith("]")) {
            return true;
        } else {
            return false;
        }
    }


    public static String getPath(Context context, Uri uri) throws FileNotFoundException {//https://developer.android.com/guide/topics/providers/document-provider
        if ("file".equalsIgnoreCase(uri.getScheme())) {
            String path = uri.getPath();
            if (path == null) {

                throw new FileNotFoundException("文件不存在,from path: " + uri);
            }
            return path;
        } else {
//        }else if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {"_data"};
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    String string = cursor.getString(column_index);
                    cursor.close();
                    if (TextUtils.isEmpty(string)) {
                        throw new FileNotFoundException("文件不存在,file: " + string);
                    }
                    return string;
                } else {
                    throw new FileNotFoundException("文件不存在,cannot move first: ");


                }
            } catch (Exception e) {
                throw new FileNotFoundException("文件不存在, " + e.toString());
                // Eat it
            }
        }

    }


    public static long getNowTime() {
        return new Date().getTime();
    }

    public final static String appDir = "" + BuildConfig.APPLICATION_ID;
    public final static String cacheStr = appDir + "/temp";

    public static File getTempCacheApkFileName() {
        return new File(getCachePath(), productFileName(".apk"));
    }

    private static String productFileName(String postfix) {
        Date date = new Date(System.currentTimeMillis()); //2016-01-28 12:02:28  14位年月日
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date) + getRandom(3) + postfix;
    }

    public static int getRandom(int n) {
        int ans = 0;
        while (Math.log10(ans) + 1 < n)
            ans = (int) (Math.random() * Math.pow(10, n));
        return ans;
    }

    public static File getCachePath() {

        return getMeimiPath(cacheStr);
    }


    public static File getMeimiPath(String path) {
        File dir = productSystemCacheFolder(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File productSystemCacheFolder(String file) {
        File cacheDir = AppContext.getInstance().getCacheDir();
        return new File(cacheDir, file);
    }

    private static void requestDownloadFile(final Context context, String content, final String url) {
        DialogUtils.showConfirmDialog(context, content, new INotify<Void>() {
            @Override
            public void onNotify(Void param) {
                final ProgressDialog progressDialog = new ProgressDialog(context);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setMax(100);
                progressDialog.setCancelable(false);
                progressDialog.setTitle("正在下载");

                progressDialog.show();
//                progressDialog.getWindow().setTitleColor(ContextCompat.getColor(MainActivity.this, R.color.theme_color));
                final File tempApk = getTempCacheApkFileName();
                AppUtils.downloadFile(url, tempApk, new DownloadListener() {
                    @Override
                    public void onStart(int value) {
                        progressDialog.setTitle("已就绪");
                    }

                    @Override
                    public void onLoading(int process) {
                        progressDialog.setProgress(process);
                        progressDialog.setTitle("正在下载");//有时候锁屏不让下载

                    }

                    @Override
                    public void onFail(String value) {
                        progressDialog.dismiss();
                        AppContext.showToast("下载失败！" + value);
                    }

                    @Override
                    public void onSuccess(String value) {
                        progressDialog.setTitle("下载成功");
                        progressDialog.dismiss();
                        AppUtils.installApkFile(context, tempApk);
                    }
                });
            }
        }, new INotify<Void>() {
            @Override
            public void onNotify(Void param) {
            }
        });

    }

    public static void toQQGroup(Context activity) {
        if (NetCns.updateBean == null || NetCns.updateBean.getGroup() == null) {
            Toast.makeText(activity, "服务器不可用,请访问博客获取群号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (NetCns.updateBean.getGroup().length() <= 11) {
            Toast.makeText(activity, "群号是" + NetCns.updateBean.getGroup(), Toast.LENGTH_LONG).show();
        } else {
            if (BuildConfig.DEBUG) {
//                NetCns.updateBean.setQqgroup("1Eg7AKtjSt989YCYi3zGxj4eFugVO8IQ");
            }
            AppUtils.joinQQGroup(activity, NetCns.updateBean.getGroup());//EncryptUtilN.a7(new int[]{2663,3075,3059,3135,3091,2863,3007,3003,2991,2975,3135,2995,3059,2951,3011,3111,3107,2867,3055,3091,3059,2867,2959,3067,3115,3127,2967,2855,2963,2991})

        }
    }

    public static boolean joinQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static boolean openAlipaySHnag(Context context) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("alipays://platformapi/startapp?saId=10000007&lientVersion=3.7.0.0718&qrcode=https://qr.alipay.com/c1x09104vwt0xobp1vmrr63"));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            return false;
        }
    }

    public static void openWebView(Context activity, String url) {
        try {

            android.content.Intent intent = new android.content.Intent();
            intent.setAction("android.intent.action.VIEW");
            android.net.Uri content_url = android.net.Uri.parse(url);
            intent.setData(content_url);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        LogUtil.importPackage();

    }

    public static void showUpdateDialog(final UpdateBean updateInfo, final Context context) {
        if (updateInfo.isForce()) {
            if (context instanceof MainActivity) {


            }
        }
        AlertDialog dialog = DialogUtils.getYesOrNoDialog(context, "发现新版本:" + updateInfo.getVersion() + " build " + updateInfo.getVersioncode() + "\n" + updateInfo.getDescription(), "更新提示", "马上更新", "忽略更新", new INotify<Void>() {
            @Override
            public void onNotify(Void param) {
                //LogUtil.writeLog("需要更新");
                File updateFile = null;
                // 更新文件保存路径
                // 组成下载路径以及文件名
//				final File tempFile=new File(updateFile,DownloadUtils.getNetFileExtension(updateInfo.getUrl()));
                //生成在缓存目录无法执行安装
                if (TextUtils.isEmpty(updateInfo.getUrl()) || updateInfo.getUrl().equals("#")) {
                    AppUtils.toQQGroup(context);
                    if (updateInfo.isForce()) {
                        try {

                        } catch (Exception e) {
                        }
                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                        }

                    }
                    return;
                }
                if (!updateInfo.getUrl().endsWith("apk")) {
                    if (updateInfo.isForce()) {
                        AppUtils.openWebView((Activity) context, updateInfo.getUrl());
                        ((Activity) context).finish();
                    } else {
                        AppUtils.openWebView((Activity) context, updateInfo.getUrl());

                    }
                    return;
                }
                requestDownloadFile(context, "下载", updateInfo.getUrl());

            }


        }, new INotify<Void>() {
            @Override
            public void onNotify(Void param) {
                if (updateInfo.isForce()) {
                    ((Activity) context).finish();
                } else {
                }
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (updateInfo.isForce()) {
                    ((Activity) context).finish();
                } else {
                }
            }
        });
        dialog.show();
    }

    public static File getSdcardpApkFileName(String file) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return new File(AppContext.getInstance().getExternalCacheDir(), appDir + File.separator + file);//放到crashStr目录管理
        } else {
//            return null;
            throw new RuntimeException("请插入内存卡再进行更新!");
        }
    }

    public static void installApkFile(Context activity, File file) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(AppUtils.fixFromFile(file), "application/vnd.android.package-archive");
        activity.startActivity(intent);
    }


    public static void downloadFile(String url, final File filePath, @NonNull final cn.qssq666.robot.interfaces.DownloadListener downloadListener) {
        AsyncHttpClient client = new AsyncHttpClient();
// 指定文件类型
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg", "application/octet-stream", "tapplication/x-gzip", "application/zip", "application/vnd.android.package-archive"};
        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {


            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  byte[] binaryData) {
                Log.e("binaryData:", "共下载了：" + binaryData.length);
                try {
                    if (filePath.exists()) {
                        boolean delete = filePath.delete();
                        if (delete == false) {
                            downloadListener.onFail("下载失败,临时文件存在却被写保护！");
                            return;
                        }
                    }

                    filePath.getParentFile().mkdirs();
                    filePath.createNewFile();
                    filePath.setWritable(true);
                    filePath.setExecutable(true);
                    byte2File(binaryData, filePath);
                    downloadListener.onSuccess(filePath.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    downloadListener.onFail(e.toString());
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] binaryData, Throwable error) {
                downloadListener.onFail(error.toString());
                //  Auto-generated method stub
//				Toast.makeText(mContext, "下载失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                int count = (int) ((bytesWritten * 1.0 / totalSize) * 100);
                downloadListener.onLoading(count);
//                Log.d("下载 Progress>>>>>", bytesWritten + " / " + totalSize);
            }

            @Override
            public void onStart() {
                super.onStart();
                downloadListener.onStart(0);
            }

            @Override
            public void onRetry(int retryNo) {
                //  Auto-generated method stub
                super.onRetry(retryNo);
                // 返回重试次数
            }
        });
    }


    public static File byte2File(byte[] bytes, File file) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return file;
    }


    public static void skipActivity(Context context, Class<? extends Activity> classes) {
        Intent intent = new Intent(context, classes);
        context.startActivity(intent);
    }

    public static String encodeUrl(String str) {
        try {
            String encode = URLEncoder.encode(str, "utf-8");
            return encode;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String getStrContentByUri(Context context, Uri uri) throws IOException {
        int start = 0;
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
            if (start > 100000) {
                throw new RuntimeException("文件太大");
            }
            start++;
//            Log.w(TAG, "LINE:" + line);
        }
        inputStream.close();
        return sb.toString();
    }

    public static void openQQChat(Context context, String account) throws Exception {
        String view = "mqqwpa://im/chat?chat_type=wpa&uin=" + account + "&version=1";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(view));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("uin", account);
        intent.putExtra("uintype", 1);
        intent.putExtra("flag_action", "6b6a");
//                intent.setAction("com.tencent.mobileqq.action.MAINACTIVITY");
        intent.putExtra("open_chatfragment", true);

        context.startActivity(intent);
    }

    public static void openQQGroupChat(Context context, String account) throws Exception {
        String view = "mqqwpa://im/chat?chat_type=group&uin=" + account + "&version=1";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(view));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("uin", account);
        intent.putExtra("uintype", 1);
        intent.putExtra("flag_action", "6b6a");
//                intent.setAction("com.tencent.mobileqq.action.MAINACTIVITY");
        intent.putExtra("open_chatfragment", true);

        context.startActivity(intent);
    }

    public static SharedPreferences getConfigSharePreferences(Context proxyContext) {
        return proxyContext.getSharedPreferences("robot_config", Context.MODE_PRIVATE);
    }

    public static void openAppDetail(Context context, String packageName) {
        context.startActivity(getAppDetail(packageName));
    }

    public static Intent getAppDetail(String packageName) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", packageName, null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", packageName);
        }
        return intent;
    }

    public static Intent getClickNotificationIntent(String packageName) {
        Intent intent = new Intent("click.notification");
        intent.setClassName(packageName, NotificationAct.class.getName());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    public static String getLoginToken(String key) {
        try {
            AppUtils.class.getClassLoader().loadClass("de.robv.android.xposed.XposedHelpers");
            return "";
        } catch (Throwable e) {

        }
        try {
            ClassLoader.getSystemClassLoader().loadClass("de.robv.android.xposed.XposedHelpers");
            return "";
        } catch (Throwable e) {

        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String format = simpleDateFormat.format(new Date());
        return Md5Utils.encode(format + key + "_moody");
    }

    public static String formatDate(long vipendtime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(vipendtime);
    }


    public static void restartApp(Context context) {
        Intent intent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context, 0, intent, 0);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
        System.exit(0);
        if (context instanceof Activity) {
            ((Activity) context).finish();

        }
    }

    public static UserInfo parseUserInfo(String str) {
        /*
         String phone;
    String username;
    String nickname;
    long vipendtime;
    double score;
    int id;
    String email;
    int vip;
         */
        UserInfo userInfo = new UserInfo();
        try {
            JSONObject jsonObject = new JSONObject(str);
            userInfo.setUsername(jsonObject.optString("username"));
            userInfo.setNickname(jsonObject.optString("nickname"));
            userInfo.setEmail(jsonObject.optString("email"));
            userInfo.setPhone(jsonObject.optString("phone"));
            userInfo.setToken(jsonObject.optString("token"));
            userInfo.vip = jsonObject.optInt("vip");
            userInfo.setId(jsonObject.optInt("id"));
            userInfo.vipendtime = jsonObject.optLong("vipendtime") * 60 * 1000;
            userInfo.score = jsonObject.optDouble("score");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static boolean runxposed() {
        try {
            Class<?> aClass = Class.forName("de.robv.android.xposed.XposedHelpers");
            return true;
        } catch (ClassNotFoundException e) {
        }
        return false;
    }

    public static boolean runxposed1() {
        try {
            Class<?> aClass = Class.forName("de.robv.android.xposed.XposedHelpers");
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            try {
                Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass("de.robv.android.xposed.XposedHelpers");
                return true;
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();

            }
        }
        return false;
    }

    public static String getSimpleInfomation(String string) {
        if (string == null) {
            return "";
        }
        String title = "";
        if (string.startsWith("<html")) {
            title = StringUtils.getStrCenter(string, "<title>", "</title>");
        }

        if (string.length() > 100) {
            return title + " " + string.substring(0, 100);
        } else {
            return title + string;
        }
    }

    public static String getIP() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nextElement = networkInterfaces.nextElement();
                String name = nextElement.getName();
                if (name != null && (name.contains("wlan") || name.contains("eth"))) {
                    Enumeration<InetAddress> inetAddresses = nextElement.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress nextElement2 = inetAddresses.nextElement();
                        if (!nextElement2.isLoopbackAddress() && nextElement2.getAddress().length == 4) {
                            return nextElement2.getHostAddress();
                        }
                    }
                    continue;
                }
            }
            return null;
        } catch (NullPointerException | SocketException unused) {
            return null;
        }
    }

    public static String getWifiIP() {
        WifiInfo connectionInfo = ((WifiManager) AppContext.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
        if (connectionInfo == null) {
            return null;
        }
        int ipAddress = connectionInfo.getIpAddress();
        if (ipAddress == 0) {
            return getIP();
        }
        return String.format(Locale.US, "%d.%d.%d.%d", Integer.valueOf(ipAddress & 255), Integer.valueOf((ipAddress >> 8) & 255), Integer.valueOf((ipAddress >> 16) & 255), Integer.valueOf((ipAddress >> 24) & 255));
    }


    public static String getSSID() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) AppContext.getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected() || activeNetworkInfo.getType() != 1) {
            return null;
        }
        String ssid = ((WifiManager) AppContext.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getSSID();
        if (!"<unknown ssid>".equals(ssid)) {
            return ssid;
        }
        String extraInfo = activeNetworkInfo.getExtraInfo();
        Log.e(TAG, "getSSID() returns <unknown ssid>, let's try extraInfo={} as SSID " + extraInfo);
        return extraInfo;

    }
}
