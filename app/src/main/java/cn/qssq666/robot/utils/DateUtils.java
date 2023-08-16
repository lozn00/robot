package cn.qssq666.robot.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by luozheng on 2017/4/23.  qssq.space
 */

public class DateUtils {


    /**
     * 毫秒
     */
    public static final int TYPE_MS = 1;
    /**
     * 秒
     */
    public static final int TYPE_SECOND = 2;
//    public static final int TYPE_SECOND = 2;
    /**
     * 分钟
     */
    public static final int TYPE_MINUTE = 3;
    /**
     * 小时
     */
    public static final int TYPE_HOUR = 4;
    public static final int TYPE_DAY = 5;

    public static long getTimeDistance(int type, long startBigTime, long endTime) {
//        long day=l/(24*60*60*1000);
//        long hour=(l/(60*60*1000)-day*24);
//        long min=((l/(60*1000))-day*24*60-hour*60);
//        long s=(l/1000-day*24*60*60-hour*60*60-min*60);
   /*     long ms = startTime - endTime;
        long day = ms / (3600 * 24 * 1000);
        long hour = (ms / (3600 * 1000) - day * 24);
        long min = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (ms / 1000 - day * 86400 - hour * 3600 - min * 60);*/
        long ms = startBigTime - endTime;
//        long day = ms / (3600 * 24 * 1000);
//        long hour = (ms / (3600 * 1000) - day * 24);
//        long min = ((ms / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = ms / 10000;
        long min = ms / 10000 / 60;
        long hour = ms / 10000 / 60 / 60;
        long day = ms / 10000 / 60 / 60 / 24;
//        System.out.println("day" + day + "hour" + hour + ",min" + min + ",s:" + s + "ms:" + ms);

//
//        long day=l/(24*60*60*1000);
//        long hour=(l/(60*60*1000)-day*24);
//        long min=((l/(60*1000))-day*24*60-hour*60);
//        long s=(l/1000-day*24*60*60-hour*60*60-min*60);
        switch (type) {
            case TYPE_MS:
                return ms;
            case TYPE_SECOND:
                return s;
            case TYPE_MINUTE:
                return min;
            case TYPE_HOUR:
                return hour;
            case TYPE_DAY:
                return day;
            default:
                return -0;
        }
    }


    public static String getCurrentHour() {
        return new SimpleDateFormat("HH").format(System.currentTimeMillis());
    }

    public static String getTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
    }
    public static String getShortTime(long time) {
        return new SimpleDateFormat("MM-dd HH:mm:ss").format(time);
    }

    public static String getTimeEightFormatStr(Date date) {
        return new SimpleDateFormat("yyyyMMdd").format(date);
    }

    public static String getTimeYmd(long date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";
    public static final String STR_SECOND = "秒";
    public static final String STR_MILLISECOND = "毫秒";
    public static final String STR_MINUTE = "分钟";
    public static final String STR_HOUR = "小时";
    public static final String STR_DAY = "天";


    public static String getGagTime(long secondCurrent) {
        long day = secondCurrent / (86400);//24*60*60

        long hour = secondCurrent % (86400) / (3600); //60*60
        long minute = secondCurrent % (86400) % (3600) / 60;

        long second = secondCurrent % (86400) % (3600) % 60;
        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + STR_DAY);
        }
        if (hour > 0) {
            sb.append(hour + STR_HOUR);

        }

        if (minute > 0) {
            sb.append(minute + STR_MINUTE);
        }

        if (second > 0) {
            sb.append(second + STR_SECOND);
        }
        return sb.toString();
    }


    public static String getTimeDetailDistance(long second) {

        //        Context applicationContext = this.a.getApplication().getApplicationContext();
        String minuteStr = STR_MINUTE;//applicationContext.getString(2131430448);
        String hourStr = STR_HOUR;//""+ applicationContext.getString(2131430449);
        String dayStr = STR_DAY;//applicationContext.getString(2131430450);
        if (second < 60) {
            return second + STR_SECOND;
        }
        long muite = 59 + second;

        long day = muite / 86400;//86400 等于1天 3600 等于60分钟 1小时
        long hour = (muite - (86400 * day)) / 3600;
        muite = ((muite - (86400 * day)) - (3600 * hour)) / 60;
        String str = "";
        if (day > 0) {
            str = str + day + dayStr;
        }
        if (hour > 0) {
            str = str + hour + hourStr;
        }
        if (muite > 0) {
            return str + muite + minuteStr + second + STR_SECOND;
        }
        return str;

    }


    /**
     * 1天内 昨天**,如果1天则显示天之后看了。
     *
     * @return
     */
    public static String getAboutTimeStr(long javaTime) {
        long time = javaTime;
        time = new Date().getTime() - time;
        if (time < 1L * ONE_MINUTE) {
            long seconds = toSeconds(time);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (time < 45L * ONE_MINUTE) {
            long minutes = toMinutes(time);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (time < 24L * ONE_HOUR) {
            long hours = toHours(time);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (time < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (time < 30L * ONE_DAY) {
            long days = toDays(time);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (time < 12L * 4L * ONE_WEEK) {
            long months = toMonths(time);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(time);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }

    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }


    /**
     * @param
     * @return
     */
    public static String getAboutTimeDistance(long nowTime) {
        return getAboutTimeDistance(nowTime, new Date().getTime());
    }


    public static String getAboutTimeDistance(long nowTime, long futuretime) {
        return getAboutTimeDistanceNotPostfix(nowTime, futuretime, "前");

    }

    public static String getAboutTimeDistanceNotPostfix(long nowTime, long futuretime, String postfix) {
        long temp = futuretime - nowTime;
        futuretime = Math.abs(temp);
        String prefix = temp < 0 ? "" : "";
        if (futuretime < 1L * ONE_MINUTE) {
            long seconds = toSeconds(futuretime);
            return prefix + (seconds <= 0 ? 1 : seconds) + "秒" + postfix;
        } else if (futuretime < 45L * ONE_MINUTE) {
            long minutes = toMinutes(futuretime);
            return prefix + (minutes <= 0 ? 1 : minutes) + "分钟" + postfix;
        } else if (futuretime < 24L * ONE_HOUR) {
            long hours = toHours(futuretime);
            return (hours <= 0 ? 1 : hours) + "小时" + postfix;
        } else if (futuretime < 48L * ONE_HOUR) {
            return "昨天";
        } else if (futuretime < 30L * ONE_DAY) {
            long days = toDays(futuretime);
            return (days <= 0 ? 1 : days) + "天" + postfix;
        } else if (futuretime < 12L * 4L * ONE_WEEK) {
            long months = toMonths(futuretime);
            return (months <= 0 ? 1 : months) + "月" + postfix;
        } else {
            long years = toYears(futuretime);
            return (years <= 0 ? 1 : years) + "年" + postfix;
        }

    }


    /**
     * 精确到毫秒  不是时间戳  new Date().getTime()-new Date().getTime()的时间 比如。
     *
     * @param millisecond
     * @return
     */
    public static String generateTime(long millisecond) {
        if (millisecond <= 0) {
            return "00:00";
        }
        int totalSeconds = (int) (millisecond / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
        }
    }

    public static String generateTimeDetail(long millisecond) {
        if (millisecond <= 0) {
            return "0秒";
        }
        int totalSeconds = (int) (millisecond / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;
        int day = totalSeconds / 86400;

        if (day > 0) {
            return String.format(Locale.US, "%02d天%02d时%02d分%02d秒", day, hours, minutes,
                    seconds).toString();
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d时%02d分%02d秒", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d分%02d秒", minutes, seconds)
                    .toString();
        }
    }


    public static String generateTimeSymbolSecond(long second) {
        return generateTimeSymbol(second * 1000);
    }

    /**
     * 仿QQ录音的时分秒
     *
     * @param millisecond
     * @return
     */

    //ignore_start
    public static String generateTimeSymbol(long millisecond) {
        if (millisecond <= 0) {
            return "00:00";
        }
        int totalSeconds = (int) (millisecond / 1000);

        int seconds = totalSeconds % 60;
//        int minutes = (totalSeconds / 60) % 60;
        int minutesCount = (totalSeconds / 60);
//        int hours = totalSeconds / 3600;

        if (minutesCount > 0) {
            return String.format(Locale.US, "%d'%02d\"", minutesCount, seconds).toString();
        } else {
            return String.format(Locale.US, "%d\"", seconds)
                    .toString();
        }

    }

    public static String formatTimeDistance(long ms) {


        StringBuilder builder = new StringBuilder();
        if (ms < 60) {
            return ms + "ms";
        } else {
            long seconds = ms / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            if (days > 0) {
                builder.append(days);
                builder.append("天");
            }
            if (hours > 0) {
                builder.append(hours % 24);
                builder.append("小时");
            }
            if (minutes > 0) {
                builder.append(minutes % 60);
                builder.append("分钟");
            }
            if (seconds > 0) {
                builder.append(seconds % 60);
                builder.append("秒");
            }
            return builder.toString();
        }
    }

    public static String getNowWeek() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return getWeekday(dayOfWeek);
    }
    public static int getNowWeekInt() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }
    private static String getWeekday(int dayOfWeek) {
        switch (dayOfWeek) {
            case 0:
                return "Sunday";
            case 1:
                return "Monday";
            case 2:
                return "Tuesday";
            case 3:
                return "Wednesday";
            case 4:
                return "Thursday";
            case 5:
                return "Friday";
            case 6:
                return "Saturday";
            default:
                return "";
        }
    }


    //ignore_send
}
