package cn.qssq666.robot.utils;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.business.RobotContentProvider;

/**
 * adb logcat adb logcat *:E 打印错误
 * Created by qssq on 2018/1/30 qssq666@foxmail.com  cn.qssq666.robot.utils.LogUtil
 */

public class LogUtil {

    public static String INTENT_RECEIVER_LOG = "intent_receiver_log";

    public static String filterTag = "";
    public static String filterKey = "";
    public static boolean showTime;

    public static File getLogFile() {
        return new File("/sdcard/qssq666/log/cn.qssq666.robot.txt");
    }

    public static void importPackage() {

    }

    public static final String TAG = "[RobotI]";//ignore_include

    //g]不支持的处理消息类型，您可以反馈作者增加友好支持提示-3006,MsgItem{nickname='腾讯课堂', istroop=1008, senderuin='2029033910', frienduin='2029033910', message='<?xml version="1.0" encoding="utf-8"?><msg><appmsg><item><cover>http://10.url.cn/qqcourse_logo_ng/ajNVdqHZLLBk3UAd8FV7E3CUvtFYdnufxSjichjsrItnZTEWyRRv1Z9Uz9tXaQBlep0LlwFfemWg/</cover><digest>Android开发/安卓/NDK/架构/React Native/性能优化【动脑学院】
    public static void writeLog(String log) {
        writeLog("_", log);
    }

    public static void writeLog(String TAG1, String log) {
        if (BuildConfig.DEBUG && log.contains("strack")) {
            Log.w(TAG1, "[log]状态堆栈" + Log.getStackTraceString(new Throwable()));

        }
        Log.w(TAG, TAG1 + "" + getConsumeTime() + log);
   /*     if (EventBus.getDefault().hasSubscriberForEvent(LogEvent.class)) {
            LogEvent logEvent = new LogEvent();
            logEvent.setLog("" + log);
            logEvent.setTime(new Date().getTime());
            EventBus.getDefault().post(logEvent);
        } else {
//            Log.d(TAG1, "尚未打开日志界面");
        }*/
    }

    public static void writeLoge(String s) {
        Log.e(TAG, getConsumeTime() + "" + s);
    }
    public static void writeLoge(String tag,Throwable s) {
        Log.e(TAG, getConsumeTime()+ tag+ "" + Log.getStackTraceString(s));
    }
    public static String getConsumeTime() {
        return String.format("[%sms]", System.currentTimeMillis() - RobotContentProvider.mInsertTime);
    }


    public static boolean isLogRuning() {
        return isLogRuning;
    }

    public static boolean isLogRuning;

    public static void stopRecordLog() {

        stop = true;
    }


    private static boolean stop = false;


    public static void startRecordLog() {
        stop = false;
        new Thread() {
            @Override
            public void run() {
                FileOutputStream os = null;
                try {
                    //新建一个路径信息
                    File logFile = getLogFile();
                    if (!logFile.getParentFile().exists()) {
                        logFile.getParentFile().mkdirs();

                    }

                    if (!logFile.exists()) {
                        logFile.createNewFile();
                    }
                    os = new FileOutputStream(logFile);


                    int len = 0;
                    byte[] buf = new byte[1024];
                    List<String> list = new ArrayList<>();
                    list.add("logcat");
                    list.add("-v");
                    if (showTime) {

                        list.add("time");
                    } else {
                        list.add("tag");
                    }
                    if (!TextUtils.isEmpty(filterTag)) {
                        list.add("-s");
                        list.add(filterTag);

                    }
                    if (!TextUtils.isEmpty(filterKey)) {
                        list.add("|grep");
                        list.add(filterKey);

                    }

                    Log.w(TAG, "cmd:" + list.toString());
                    String[] arr = new String[list.size()];
                    for (int i = 0; i < list.size(); i++) {
                        arr[i] = list.get(i);
                    }
//                    new String[]{"logcat", filterTag}
//                    String[] running = new String[]{"logcat", "-s", TAG};
//                    String[] running = new String[]{"logcat", "-s", TAG + " *: W"};
                    Process exec = Runtime.getRuntime().exec(arr);

                    final InputStream is = exec.getInputStream();
                    while (true) {

                        if (-1 != (len = is.read(buf))) {
                            String current = new String(buf, 0, len);

                            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(RobotContentProvider.getInstance().getProxyContext());
                            Intent intent = new Intent();
                            intent.putExtra("log", current);
                            intent.setAction("" + INTENT_RECEIVER_LOG);
                            localBroadcastManager.sendBroadcast(intent);


                            os.write(buf, 0, len);
                            os.flush();
                        } else {

                            int i = exec.exitValue();


                            Thread.sleep(200);
                        }

                        if (stop) {
                            break;
                        }


                    }
                    isLogRuning = false;

                    os.write(new String("\nwrite over").getBytes());
                    Log.d("writelog", "exec over " + exec.exitValue());
                    exec.destroy();
                } catch (Exception e) {
                    isLogRuning = false;
                    Log.d("writelog",
                            "read logcat process failed. message: "
                                    + e.getMessage());
                } finally {
                    if (null != os) {
                        try {
                            os.close();
                            os = null;
                        } catch (IOException e) {
                            // Do nothing
                        }
                    }
                }
            }
        }.start();
    }

    public String readLog() {
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(new FileInputStream(getLogFile()));
            BufferedReader br = new BufferedReader(isr);
            String lineTxt = null;
            while ((lineTxt = br.readLine()) != null) {
                if (!"".equals(lineTxt)) {
                    String reds = lineTxt.split("\\+")[0];  //java 正则表达式
                    stringBuilder.append(reds);
                    stringBuilder.append("\n");

                }
            }
            isr.close();
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
