package cn.qssq666.robot.http.newcache;

import cn.qssq666.robot.utils.LogUtil;

public class MyLog {
    public static void Log(String s, String s1) {
        LogUtil.writeLog("["+s+"]"+s1);;
    }

    public static void e(String tag, String s, Throwable e) {
       LogUtil.writeLog("["+tag+"]"+s+":"+e);
    }
    public static void e(String tag, String s) {
        System.err.println("["+tag+"]:"+s);
    }

    public static void Log(String s) {
        LogUtil.writeLog(s);;
    }
}
