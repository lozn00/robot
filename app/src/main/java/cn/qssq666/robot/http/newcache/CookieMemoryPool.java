package cn.qssq666.robot.http.newcache;

import java.util.HashMap;

public class CookieMemoryPool {
    private static final String TAG = "CookieUtil";
    public static final String COOKIE_PREF = "cookies_prefs";

    public static String getCookie(String url, String domain) {
        domain = clearSchame(domain);
        return cookies.get(domain);
    }

    private static String clearSchame(String domain) {
        if (domain.startsWith("https://")) {
            domain = domain.substring("https://".length());
        }
        if (domain.startsWith("http://")) {
            domain = domain.substring("http://".length());
        }
        return domain;
    }

    public static void saveCookie(String url, String domain, String cookie) {
        domain = clearSchame(domain);

        cookies.put(domain,cookie);
    }

    static  HashMap<String,String> cookies=new HashMap<>();

    public static void clear() {
        cookies.clear();
    }
    /**
     * 保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
  /*  public static void saveCookie(String url, String domain, String cookies) {
        domain = clearSchame(domain);
        SharedPreferences sp = SuperContext.getInstance().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (BuildConfig.DEBUG) {
            Log.w(TAG, "SAVE_COOKIE URL:" + url + ",domain:" + domain + ",cookie:" + cookies);
        }
        if (!TextUtils.isEmpty(url)) {
            editor.putString(url, cookies);
        } else {
        }
        if (!TextUtils.isEmpty(domain)) {
            editor.putString(domain, cookies);
        }
        editor.apply();
    }*/

   /* *//**
     * 清除本地Cookie
     *
     * @param context Context
     *//*
    public static void clearCookie(Context context) {
        SharedPreferences sp = context.getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {

                }
            });
            cookieManager.removeAllCookie();
            cookieManager.flush();

        } else {
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
        }
    }*/
}
