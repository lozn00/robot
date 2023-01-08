package cn.qssq666.robot.http;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.qssq666.robot.http.newcache.CookieMemoryPool;
import cn.qssq666.robot.http.newcache.MyCookieManager;
import cn.qssq666.robot.http.newcache.MyLog;
import cn.qssq666.robot.openai.OpenAIBiz;
import cn.qssq666.robot.utils.CookieLocalFilePool;
import cn.qssq666.robot.utils.RobotUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SaveCookiesInterceptor implements Interceptor {

    private static final String COOKIE_PREF = "cookies_prefs";
    private static String TAG = "SaveCookiesInterceptor";

    public SaveCookiesInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        //set-cookie可能为多个
        if (!response.headers("Set-Cookie").isEmpty()) {
            List<String> cookies = response.headers("Set-Cookie");
            String cookieCurrent = encodeCookie(cookies);
            int port = request.url().port();
            String domain;
            if (port != 80) {
                domain = request.url().host();
//                domain=request.url().host()+":"+port;
            } else {
                domain = request.url().host();
            }
            MyLog.Log("拦截器SetCookie", domain + ",cookie:" + cookies);
            try {
                String url = request.url().toString();
                MyCookieManager myCookieManager = new MyCookieManager();
                String cookie = CookieMemoryPool.getCookie(url, domain);

                if (TextUtils.isEmpty(cookie)) {
                    cookie = CookieLocalFilePool.getCookie(cookie, domain);
                }
                myCookieManager.addCookies(cookie);//先加入之前的
                myCookieManager.addCookies(cookieCurrent);//属于合并

                String cookiesMerge = myCookieManager.getCookies();
                if ("chat.openai.com".equals(domain)) {
                    OpenAIBiz.updateOpenAICookie(cookiesMerge);
                } else {
                    CookieLocalFilePool.saveCookie(url, domain, cookiesMerge);
                    CookieMemoryPool.saveCookie(url, domain, cookiesMerge);

                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    /**
     * 整合cookie为唯一字符串
     */
    private String encodeCookie(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (String cookie : cookies) {
            String[] arr = cookie.split(";");
            for (String s : arr) {
                if (set.contains(s)) {
                    continue;
                }
                set.add(s);

            }
        }

        for (String cookie : set) {
            sb.append(cookie).append(";");
        }

        int last = sb.lastIndexOf(";");
        if (sb.length() - 1 == last) {
            sb.deleteCharAt(last);
        }

        return sb.toString();
    }

}