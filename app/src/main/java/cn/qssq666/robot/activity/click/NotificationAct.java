package cn.qssq666.robot.activity.click;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import cn.qssq666.robot.utils.ProxySendAlertUtil;

public class NotificationAct  extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProxySendAlertUtil.cancel();
        finish();
    }
}
