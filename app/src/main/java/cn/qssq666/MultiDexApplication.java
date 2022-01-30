package cn.qssq666;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

public class MultiDexApplication extends Application {
    public MultiDexApplication() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}