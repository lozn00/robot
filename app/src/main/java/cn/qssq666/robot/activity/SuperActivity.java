package cn.qssq666.robot.activity;


import com.umeng.analytics.MobclickAgent;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by qssq on 2017/12/20 qssq666@foxmail.com
 */

public class SuperActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
