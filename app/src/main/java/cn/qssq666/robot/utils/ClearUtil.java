package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;import androidx.annotation.NonNull;
import android.text.TextUtils;

import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by qssq on 2018/8/18 qssq666@foxmail.com
 */


public class ClearUtil {
public static String wordSplit="‖";

    public static HashSet<String> word2HashSet(String splitFlag,String word) {

        String[] split = word.split(splitFlag);

        LinkedHashSet<String> hashSet = new LinkedHashSet();

        for (String s : split) {

            hashSet.add(s);
        }


        return hashSet;

    }

    public static String removeRepeat(String splitFlag,String word) {

        HashSet<String> hashSet = word2HashSet(splitFlag,word);

        return hashSet2String(splitFlag,hashSet);

    }

    @NonNull
    public static String hashSet2String(String splitFlag,HashSet<String> hashSet) {
        StringBuilder sb = new StringBuilder();
        int start = 0;
        for (String s : hashSet) {

            if (!TextUtils.isEmpty(s)) {
                if(!TextUtils.isEmpty(sb.toString())&&!sb.toString().endsWith(splitFlag)){//拼接的方式

                    sb.append(splitFlag);
                }
                sb.append(s);
            }
            start++;
        }

        return sb.toString();
    }
}
