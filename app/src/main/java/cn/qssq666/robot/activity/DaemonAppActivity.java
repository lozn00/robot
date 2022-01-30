package cn.qssq666.robot.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import cn.qssq666.robot.R;

/**
 * Created by qssq on 2018/8/3 qssq666@foxmail.com
 */
public class DaemonAppActivity extends SuperActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("提升稳定性");
        setContentView(R.layout.activity_daemon_app);


    }
}
