package lozn;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;

import cn.qssq666.robot.activity.MiscConfigActivity;

public class FloatingPermissionUtils {
    public static boolean hasPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            return hasPermissionForO(context);
        }
//6.0以下　只有MUI会修改权限
/*        if (MIUI.rom()) {
            if (FloatingPermissionUtils.hasFloatingDialogPermissionOnActivityResult(context)) {
//                initLiveWindow();
                return true;
            } else {
                return hasFloatingDialogPermission(context);
//                MIUI.req(getContext());
            }
        } else {
           return hasFloatingDialogPermission(context);
        }*/
    }

    private static boolean hasFloatingDialogPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return hasPermissionBelowMarshmallow(context);
            } else {
                return false;
            }
        }

    }

    private static boolean hasFloatingDialogPermissionOnActivityResult(Context context) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            return hasPermissionForO(context);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(context);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                return hasPermissionBelowMarshmallow(context);
            } else {
                return true;
            }
        }
    }

    /**
     * 6.0以下判断是否有权限
     * 理论上6.0以上才需处理权限，但有的国内rom在6.0以下就添加了权限
     * 其实此方式也可以用于判断6.0以上版本，只不过有更简单的canDrawOverlays代替
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    static boolean hasPermissionBelowMarshmallow(Context context) {
        try {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            Method dispatchMethod = AppOpsManager.class.getMethod("checkOp", int.class, int.class, String.class);
            //AppOpsManager.OP_SYSTEM_ALERT_WINDOW = 24
            return AppOpsManager.MODE_ALLOWED == (Integer) dispatchMethod.invoke(
                    manager, 24, Binder.getCallingUid(), context.getApplicationContext().getPackageName());
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 用于判断8.0时是否有权限，仅用于OnActivityResult
     * 针对8.0官方bug:在用户授予权限后Settings.canDrawOverlays或checkOp方法判断仍然返回false
     */
    private static boolean hasPermissionForO(Context context) {
        try {
            WindowManager mgr = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (mgr == null) return false;
            View viewToAdd = new View(context);
            WindowManager.LayoutParams params = new WindowManager.LayoutParams(0, 0,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ?
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSPARENT);
            viewToAdd.setLayoutParams(params);
            mgr.addView(viewToAdd, params);
            mgr.removeView(viewToAdd);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void requestPermission(Context cxt) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        cxt.startActivity(intent);

  /*      Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.getPackageName()));
        activity.startActivityForResult(intent, code);*/
    }
}