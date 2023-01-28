package lozn;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import cn.qssq666.robot.app.AppContext;

public class FloatWindowUtil {

    private boolean hasBind = false;
    private long rangeTime;


    public void startRequestFlowWindow(AppCompatActivity context ) {
//        EventBus.getDefault().postSticky(new Event(1));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                Toast.makeText(context, "当前无悬浮窗权限，请授权", Toast.LENGTH_SHORT);
                new GlobalDialogSingle(context, "", "当前未获取悬浮窗权限", "去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        context.startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName())), 0);

                        ActivityResultLauncher<Intent> intentActivityResultLauncher = context.registerForActivityResult(
                                new ActivityResultContracts.StartActivityForResult(),
                                result -> {
                                    if (result.getResultCode() == Activity.RESULT_OK) {
                                        //获取返回的结果
                                        onActivityResult(context,0,result.getResultCode(),result.getData());
                                    }
                                });

                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
//                        intent.putExtra("userId", "xxx");
                        intentActivityResultLauncher.launch(intent);


                    }
                }).show();

            } else {
                context.moveTaskToBack(true);
                Intent intent = new Intent(context, FloatWinfowServices.class);
                hasBind = context.bindService(intent, mMyServiceConnection, Context.BIND_AUTO_CREATE);
            }
        }
    }

    ServiceConnection mMyServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 获取服务的操作对象
            FloatWinfowServices.MyBinder binder = (FloatWinfowServices.MyBinder) service;
            binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    protected void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
//        if (requestCode == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT).show();
                } else {

                  AppContext.getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(activity, FloatWinfowServices.class);
                            intent.putExtra("rangeTime", rangeTime);
                            hasBind = activity.bindService(intent, mMyServiceConnection, Context.BIND_AUTO_CREATE);
                            activity.moveTaskToBack(true);
                        }
                    }, 1000);

                }
            }
    }

/*
    protected void onRestart() {
        Log.d("RemoteView", "重新显示了");
        //不显示悬浮框
        if (hasBind) {
            conunbindService(mMyServiceConnection);
            hasBind = false;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("RemoteView", "重新显示了onNewIntent");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("RemoteView", "被销毁");
    }*/
}
