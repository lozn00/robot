package cn.qssq666.robot.utils;
import cn.qssq666.CoreLibrary0;import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Pair;

/**
 * Created by qssq on 2018/8/3 qssq666@foxmail.com
 */
public class BatteryUtil {

    /**
     * 忽略电池优化
     */
    public static boolean ignoreBatteryOptimization(Context activity) {

        PowerManager powerManager = (PowerManager) activity.getSystemService(Context.POWER_SERVICE);

        boolean hasIgnored = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            return hasIgnored;
        } else {
            return true;
        }


    }

    public static Pair<Boolean, String> add(Context context) {
        try {
            Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            intent.setData(Uri.parse("package:" + context.getPackageName()));
            ComponentName componentName = intent.resolveActivity(context.getPackageManager());
            if (componentName == null) {
                return Pair.create(false, "无法添加到电量优化");
            }

            context.startActivity(intent);
            return Pair.create(true, "请点击确定");
        } catch (Exception e) {
            return Pair.create(false, "无法添加到电量优化" + e.toString());
        }
    }


}
