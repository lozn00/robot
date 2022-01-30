package cn.qssq666.robot.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;

/**
 * Created by qssq on 2017/6/8 qssq666@foxmail.com
 */

public class AboutActivity extends SuperActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("关于(" + BuildConfig.VERSION_NAME + "_" + BuildConfig.VERSION_CODE + ")");
        setContentView(R.layout.activity_about);


    }
}
