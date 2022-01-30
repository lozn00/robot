package cn.qssq666.robot.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import cn.qssq666.robot.utils.CookieUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {


    public AddCookiesInterceptor() {
        Log.w("ADDCOOKIE","_______");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        int port = request.url().port();
        String domain;
        if(port!=80){
            domain=request.url().host()+":"+port;
        }else{
            domain=request.url().host();
        }
        String cookie = CookieUtil.getCookie(request.url().toString(),domain);
        if (!TextUtils.isEmpty(cookie)) {
            builder.addHeader("Cookie", cookie);
        }

        return chain.proceed(builder.build());
    }

}