package cn.qssq666.robot.utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.WindowManager;

import java.util.Arrays;

/**
 * Created by qssq on 2018/4/21 qssq666@foxmail.com
 */
public class PermissionUtil {

    private static final String TAG = "PermissionUtil";
    public static boolean debug=true;

    public static void onRequestPermissionsResult(Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {




        Log.w(TAG, "onRequestPermissionsResult:" + " requestCode ,result:" + getPermissionsResultMsg(permissions, grantResults)+",grantResultslength:"+grantResults.length);


    }

    private static String getPermissionsResultMsg(String[] permissions, int[] grantResults) {


        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < permissions.length; i++) {


            stringBuilder.append(printPermission(permissions[i]) + ":" + getResultMsg(grantResults[i]) + "\n");
        }


        return stringBuilder.toString();
    }

    public static void requestPermissions(Activity activity, String[] permissions, int requestCode) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(permissions,requestCode);
        Log.w(TAG, "requestPermissions:" + printPermissions(permissions) + " requestCode" + requestCode);
        }else{

        }

    }

    public static int checkSelfPermission(Context activity, String permission) {
        if(debug){
            Log.w(TAG,"=========================================");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = activity.checkSelfPermission(permission);

            if (debug) {
                Log.w(TAG, "checkSelfPermission:" + printPermission(permission) + ":" + getResultMsg(i));
            }

            return i;
        }
        if (debug) {
            Log.w(TAG, "Build.VERSION.SDK_INT <23 checkSelfPermission:" + printPermission(permission) + ":" + getResultMsg(PackageManager.PERMISSION_GRANTED));


        }
        return PackageManager.PERMISSION_GRANTED;
    }

    public static String printPermission(String permission) {
        return permission;

    }

    public static String printPermissions(String[] permission) {
        return Arrays.toString(permission);

    }

    /*
    是否应该显示 请求权限信息
     */
    public static boolean shouldShowRequestPermissionRationale(Activity activity, String permissionRationale) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            boolean result = activity.shouldShowRequestPermissionRationale(permissionRationale);
            if (debug) {
                Log.w(TAG, "shouldShowRequestPermissionRationale:" + printPermission(permissionRationale) + " result" + getRationaleResultMsg(result));
            }
        } else {
                Log.w(TAG, "shouldShowRequestPermissionRationale <23 false" );

        }

        return false;
    }

    private static String getRationaleResultMsg(boolean result) {
        return result ? "应该显示权限说明" : "不应该显示权限说明";
    }

    public static String getResultMsg(int permissionCode) {
        if (permissionCode == PackageManager.PERMISSION_GRANTED) {
            return "获得/成功";
        } else {
            return "未获得/拒绝";

        }
    }

    public static void showSystemDialog(Activity mainActivity) {

        if(debug){
            Log.w(TAG,"弹出系统对话框开始");
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

            builder.setMessage("我是系统对话框");
            Dialog dialog= builder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG);
//            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);


            dialog.show();
            Log.w(TAG,"弹出系统对话框结束");
        }
    }
}
