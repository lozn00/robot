package cn.qssq666.robot.activity.intent;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import cn.qssq666.robot.activity.SuperActivity;

/**
 * Created by qssq on 2018/11/16 qssq666@foxmail.com
 */
public class NotUiActivity extends SuperActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
