package cn.qssq666.robot.http;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;

import cn.qssq666.robot.http.newcache.CookieMemoryPool;
import cn.qssq666.robot.http.newcache.MyLog;
import cn.qssq666.robot.utils.CookieLocalFilePool;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {


    public AddCookiesInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        int port = request.url().port();
        String domain;
        if (port != 80) {
            domain = request.url().host();
//            domain=request.url().host()+":"+port;
        } else {
            domain = request.url().host();
        }//不带http://
        String url = request.url().toString();
        String cookie = CookieMemoryPool.getCookie(url, domain);
        if (TextUtils.isEmpty(cookie)) {//如果内存中为空就从本地取
            cookie = CookieLocalFilePool.getCookie(cookie, domain);
            if (!TextUtils.isEmpty(cookie)) {
                CookieMemoryPool.saveCookie(url, domain, cookie);
            }
        }
        if (cookie != null && cookie.length() > 0) {
            try{

            builder.addHeader("Cookie", cookie);
            }catch (Throwable e){
                CookieMemoryPool.clear();
                CookieLocalFilePool.saveCookie(cookie, domain,"");

                MyLog.e("COOKIE错误", Log.getStackTraceString(e));
            }
        }
        MyLog.Log("拦截器AddCookie", domain + ",cookie:" + cookie);
        return chain.proceed(builder.build());
    }

}