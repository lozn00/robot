package cn.qssq666.robot.http;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLog implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(String message) {
        Log.w("HttpLogInfo", message);
    }
}