package cn.qssq666.robot.business.dagger;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;

/**
 * Created by qssq on 2019/1/11 qssq666@foxmail.com
 */
public class MyActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//build->rebuild 就会生成DaggerMainConponent类了。
// DaggerMainConponent.create().inject(this);

    }
}
