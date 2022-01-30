package cn.qssq666.robot.service;
import cn.qssq666.CoreLibrary0;
import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import java.util.concurrent.TimeUnit;

import cn.qssq666.robot.utils.LogUtil;

/**
 * author: weishu on 2018/4/10.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class DaemonJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.writeLog("onStartJob");
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public static void scheduleJob(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        try {
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);

            if (jobScheduler == null) {
                return;
            }

            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(context, DaemonJobService.class))
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPeriodic(TimeUnit.MINUTES.toMillis(15))
                    .build();

            jobScheduler.schedule(jobInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        /*
        JobInfo.Builder builder = new JobInfo.Builder(i, mServiceComponent)
     .setMinimumLatency(2000) // 设置任务允许最少延迟时间
     .setOverrideDeadline(50000) // 设置deadline,若到期还没有到达规定的条件也会执行
     .setRequireNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) //设置网络条件，非蜂窝数据的
     .setRequiresCharging(true) // 设置充电条件
     .setRequiresDeviceIdle(false) // 设置手机是否idle状态
     .build();
    作者：Skywalker_Yang
    链接：https://www.jianshu.com/p/1d4ebae39263
    來源：简书
    简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
         */
    }
}
