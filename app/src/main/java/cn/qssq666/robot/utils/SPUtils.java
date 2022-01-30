package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.BuildConfig;

public class SPUtils {
    //commit是安全的但是效率低下 没有返回值，  apply 用于单个线程。  不关心返回值，并且在应用程序的主线程中使用这个，请考虑apply()
    public static String fileName = "config";

    public static void switchDefaultSystemConfig() {
        fileName = BuildConfig.APPLICATION_ID + "_preferences";

    }

    public static void switchAppConfig() {
        fileName = "config";

    }

    private static SharedPreferences getSp(Context context) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
    }


    private static Editor getSpEidtor(Context context) {
        return getSp(context).edit();
    }

    public static String getValue(Context context, String key, String defaultValue) {
        return getSp(context).getString(key, defaultValue);
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getSp(context).getString(key, defaultValue);

    }

    public static String getString(Context context, String key) {
        return getSp(context).getString(key, "");

    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getSp(context).getInt(key, defaultValue);

    }

    public static int getInt(Context context, String key) {
        return getSp(context).getInt(key, 0);

    }

    public static int getValue(Context context, String key, int defaultValue) {
        return getSp(context).getInt(key, defaultValue);
    }

    public static boolean getValue(Context context, String key, boolean defaultValue) {
        return getSp(context).getBoolean(key, defaultValue);
    }

    public static Long getValue(Context context, String key, Long defaultValue) {
        return getSp(context).getLong(key, defaultValue);
    }

    public static boolean isExist(Context context, String key) {
        return getSp(context).contains(key);
    }

    /*
     * ==============
     */
    public static void setValue(Context context, String key, String value) {
        getSpEidtor(context).putString(key, value).commit();
//		return getSpEidtor.(key, defaultValue);
    }

    public static void setValues(Context context, String key, List<Integer> values) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            sb.append(values.get(i) + ",");
        }

        sb.setLength(sb.length() > 0 ? sb.length() - 1 : 0);//如果大于0 那么砍掉一个长度 也就是砍掉,

//			sb.append(values.size()>0?values.get(values.size()-1):"");
        //;=sb.toString().substring(1);
//		sb.setLength(sb.length()-1?)
        getSpEidtor(context).putString(key, sb.toString()).commit();
//		return getSpEidtor.(key, defaultValue);
    }

    public static List<Integer> getValues(Context context, String key) {


        List<Integer> mReadIds = new ArrayList<Integer>();
        String value = SPUtils.getValue(context, key, "");//如果是空咋办呢，但是还是可以分割的

        if (!TextUtils.isEmpty(value)) {
            String[] reads = value.split(",");
            for (String string : reads) {
                mReadIds.add(Integer.parseInt(string));
            }
        }
        return mReadIds;
    }

    public static void setValue(Context context, String key, int value) {
        getSpEidtor(context).putInt(key, value).commit();
    }

    public static void setValue(Context context, String key, boolean value) {
        getSpEidtor(context).putBoolean(key, value).commit();
    }

    public static void setValue(Context context, String key, Long value) {
        getSpEidtor(context).putLong(key, value).commit();
    }

    public static String TEXT_SIZE = "TEXT_SIZE";
    public static String READ_IDS = "READ_IDS";
    public static String SHOW_COLUMN = "SHOW_COLUMN";

    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getSp(context).getBoolean(key, defaultValue);
    }

    public static boolean getBoolean(Context context, String key) {
        return getSp(context).getBoolean(key, false);
    }
}
