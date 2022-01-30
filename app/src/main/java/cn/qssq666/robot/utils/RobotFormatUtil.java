package cn.qssq666.robot.utils;

import android.text.TextUtils;

import org.json.JSONObject;

public class RobotFormatUtil {
    public static void appendIfNotNull(String name, String key, JSONObject jsonObject, StringBuffer stringBuffer) {
        String s = jsonObject.optString(key);
        if (!TextUtils.isEmpty(s)) {
            stringBuffer.append("\n"+name+":" + s);
        }
    }
}
