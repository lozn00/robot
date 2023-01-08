package cn.qssq666.robot.http.newcache;

import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;

public class HttpLog implements HttpLoggingInterceptor.Logger {
    @Override
    public void log(@NotNull String s) {
       MyLog.Log("请求信息",s);
    }
}
