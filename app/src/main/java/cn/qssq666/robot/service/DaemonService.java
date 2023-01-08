package cn.qssq666.robot.service;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import cn.qssq666.robot.R;
import cn.qssq666.robot.utils.AppUtils;

public class DaemonService extends Service {
    private static final String TAG = "DaemonService";
    private static final String CHANNEL_ONE_ID = "back_service";
//    private static boolean showNotification;


    public static void startup(Context context) {
 /*       File flagFile = context.getFileStreamPath(Cns.NO_NOTIFICATION_FLAG);
        if (Build.VERSION.SDK_INT >= 25 && flagFile.exists()) {
            showNotification = false;
        }*/

        context.startService(new Intent(context, DaemonService.class));
        DaemonJobService.scheduleJob(context);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Notification notification = null;
        Log.w(TAG, "onStartCommand()");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    "常驻", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(false);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }
// 在API11之后构建Notification的方式
        Notification.Builder builder = null; //获取一个Notification构造器
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this.getApplicationContext(), CHANNEL_ONE_ID);
        } else {

            builder = new Notification.Builder(this.getApplicationContext());

        }
        //上面的通知渠道解决崩溃 StartForeground Bad Notification Error 问题。
        Intent intent = null;
//        nfIntent  = new Intent(this, MainActivity.class);

        intent = AppUtils.getAppDetail(this.getPackageName());
        Intent clickNotificationIntent = AppUtils.getClickNotificationIntent(this.getPackageName());
//        Intent nfIntent = new Intent(this, MainActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        builder.setContentIntent(PendingIntent.getActivities(this, 0, new Intent[]{
                intent,clickNotificationIntent
        }, 0)); // 设置PendingIntent



//        builder.setContentIntent(PendingIntent. getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
        builder .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.xiaobing)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("情迁聊天机器人服务") // 设置下拉列表里的标题
                .setSmallIcon(R.drawable.xiaobing) // 设置状态栏内的小图标
                .setContentText("可见即可用/机器人正常运行中。。") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间


        // 获取构建好的Notification
        notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        } else {
        }

        try {
            startForeground(NOTIFY_ID, notification == null ? new Notification() : notification);// 开始前台服务
//                startForeground(110, notification);// 开始前台服务

        } catch (Exception e) {
            e.printStackTrace();
            Log.w(TAG, e.getMessage());
        }

        Log.w(TAG, "service create");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.w(TAG, "service start id=" + startId);

    }


    @Override
    public void onDestroy() {
        Log.w(TAG, "service on destroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.w(TAG, "service on unbind");
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
        Log.w(TAG, "service on rebind");
        super.onRebind(intent);
    }

    private static final int NOTIFY_ID = 101;


}
