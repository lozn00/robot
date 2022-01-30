package cn.qssq666.robot.http;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.qssq666.robot.utils.CookieUtil;
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
            String cookie = encodeCookie(cookies);
            int port = request.url().port();
            String domain;
            if(port!=80){
                domain=request.url().host()+":"+port;
            }else{
                domain=request.url().host();
            }
            CookieUtil.saveCookie(request.url().toString(), domain, cookie);
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