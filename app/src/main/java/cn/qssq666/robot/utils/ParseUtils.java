package cn.qssq666.robot.utils;
import android.text.TextUtils;

/**
 * Created by qssq on 2017/12/3 qssq666@foxmail.com
 */

public class ParseUtils {
    public static long parseLong(Object str) {

        return parseLong(str, 0);
    }

    public static long parseLong(Object str, long defaultValue) {
        if (str instanceof Long) {
            return (long) str;
        } else if (str instanceof String) {
            try {
                return Long.parseLong((String) str);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        } else {
            return 0;
        }
    }


    public static int parseInt(Object str) {
        return parseInt(str, 60);
    }

    public static int parseInt(Object str, int defaultVaut) {
        if (str instanceof Integer) {
            return (int) str;
        } else if (str instanceof String) {
            try {
                return Integer.parseInt((String) str);
            } catch (NumberFormatException e) {
                return defaultVaut;
            }
        }
        return defaultVaut;

    }

    public static int parseShort(Object str) {
        if (str instanceof Short) {
            return (int) str;
        } else if (str instanceof Short) {
            try {
                return Short.parseShort((String) str);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;

    }

    public static boolean parseBoolean(String argByArgArr) {

        if (argByArgArr == null) {
            return false;
        }
        if (argByArgArr.equals("是") || argByArgArr.equals("真") || argByArgArr.equals("yes") || argByArgArr.equals("1") || argByArgArr.equals("true")) {
            return true;
        }

        return false;

    }


    public static int parseBooleanInt(String argByArgArr) {

        if (argByArgArr.equals("是") || argByArgArr.equals("真") || argByArgArr.equals("yes") || argByArgArr.equals("1") || argByArgArr.equals("true")) {
            return 1;
        }

        return 0;


    }

    public static boolean isFalse(String argByArgArr) {
        if (argByArgArr == null || argByArgArr.equals("") || argByArgArr.equals("null") || argByArgArr.equals("nil") || argByArgArr.equals("否") || argByArgArr.equals("假") || argByArgArr.equals("no") || argByArgArr.equals("0") || argByArgArr.equals("false")) {
            return true;
        }
        return false;


    }

    public static int parseDistanceInt(String argByArgArr) {

        if (argByArgArr.equals("是") || argByArgArr.equals("真") || argByArgArr.equals("yes") || argByArgArr.equals("true")) {
            return 1;
        }
        if (isFalse(argByArgArr)) {
            return 0;
        }
        if (RegexUtils.checkDigit(argByArgArr)) {
            return Integer.parseInt(argByArgArr);
        }
        return 0;
    }


    /**
     * 返回秒
     *
     * @param gag
     * @return
     */
    public static long parseGagStr2Secound(String gag) {
        String result = null;
        long second = 0;

        if (TextUtils.isEmpty(gag)) {//孔字符串
            return 0;//x 50 =second
        }

        if (RegexUtils.checkNoSignDigit(gag)) {
            second = parseLong(gag);


        } else {

            result = StringUtils.getStrLeft(gag, DateUtils.STR_DAY);
            if (!TextUtils.isEmpty(result)) {
                gag = gag.replace(result + DateUtils.STR_DAY, "");
                second = parseLong(result) * 24 * 60 * 60;//x 50 =second
            } else {
            }

            result = StringUtils.getStrLeft(gag, DateUtils.STR_HOUR);
            if (!TextUtils.isEmpty(result)) {
                gag = gag.replace(result + DateUtils.STR_HOUR, "");
                second = second + parseLong(result) * 60 * 60;//x 50 =second
            } else {

            }
            result = StringUtils.getStrLeft(gag, DateUtils.STR_MINUTE);

            if (!TextUtils.isEmpty(result)) {
                gag = gag.replace(result + DateUtils.STR_MINUTE, "");
                second = second + (parseLong(result) * 60);//x 50 =second
            }


            result = StringUtils.getStrLeft(gag, DateUtils.STR_MILLISECOND);

            if (!TextUtils.isEmpty(result)) {
                gag = gag.replace(result + DateUtils.STR_MILLISECOND, "");
                second = second + (parseLong(result) / 1000l);//x 50 =second
                if (second < 0) {
                    second = 0;
                }
            }


            result = StringUtils.getStrLeft(gag, DateUtils.STR_SECOND);

            if (!TextUtils.isEmpty(result)) {
                gag = gag.replace(result + DateUtils.STR_SECOND, "");
                second = second + parseLong(result);//x 50 =second
            }


        }


        if (second == 0) {
//            second = parseLong(gag);
        }

 /*       if (second > 0 && second < 10) {
            second = second * 60;  //腾讯禁言必须是1分钟，0 则代表取消 所以直接把它改成 特定的东西。
        }*/

        return second;

    }

    public static String parseString(Object o) {
        if (o == null) {
            return "";
        } else if (o instanceof String) {
            return o + "";

        } else {
            return o + "";

        }
    }

    /**
     * 转换为毫秒
     *
     * @param minute
     * @return
     */
    public static long parseMinuteToMs(long minute) {
        return 1000 * 60 * minute;
    }

    /**
     * qq禁言是秒
     *
     * @param second
     * @return
     */
    public static long parseSecondToMs(long second) {
        return second * 1000;
    }

    public static float parseFloat(String s) {
        try {
            return Float.parseFloat(s);

        } catch (Exception e) {
            return 1.0f;
        }

    }

    public static double parseDouble(String s) {
        try {
            return Double.parseDouble(s);

        } catch (Exception e) {
            return 1.0;
        }

    }

    public static String parseMaxLengStr(String key, int fontMaxWidth) {
        if (key == null) {
            return "";
        }
        if (key.length() > fontMaxWidth) {
            return key.substring(0, fontMaxWidth);
        }
        return key;
    }

    @Deprecated
    public static String getXmlErrorLineStr(int i1,String currentLine, String message) {


            if (i1 > 1 && i1 < message.length()) {

                int endPositon = 0;
                int startPosition = 0;
                if (i1 > 5) {
                    startPosition = 5-1;
                    endPositon = startPosition + 15 >= currentLine.length() ? startPosition + 13 : currentLine.length() - 1;
                    String temp = currentLine.substring(startPosition, endPositon);
                    message += message + "[从错误开始位置到最后面的字符:" + temp + "]";

                } else {

                    startPosition = 0;
                    endPositon = currentLine.length() - 1;
                    String temp = currentLine.substring(startPosition, endPositon);
                    message += message + "[从错误结束为止到之前字符:" + temp + "]";

                }
                return message;

            }


        return message;
    }

    public static String parseBoolean2ChineseBooleanStr(boolean value) {
        if(value){
            return "是";
        }
        return "否";
    }
}
