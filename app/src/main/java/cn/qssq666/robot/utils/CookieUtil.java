package cn.qssq666.robot.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.app.AppContext;

public class CookieUtil {
    private static final String TAG = "CookieUtil";
    public static final String COOKIE_PREF = "cookies_prefs";

    public static String getDomainRemoveSchame(String url) {
        url = url.replace("http://", "").replace("https://", "");
        if (url.contains("/")) {
            url = url.substring(0, url.indexOf('/'));
        }
   /*     int i = url.indexOf(":");
        if(i!=-1){
            return url.substring(0,i);
        }*/
        return url;
    }

    public static void setCookie(String[] url, WebView webView) {
        String cookie = getCookie(url[0], CookieUtil.getDomainRemoveSchame(url[0]));
        for (String s : url) {
            setWebViewCookie(s, webView, cookie);

        }
    }

    private void syncCookie(String url) {
        try {
//            CookieSyncManager.createInstance(AppContext.getInstance());
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeAllCookie();
//            Log.i(TAG, "cookie=" + cookies);
          /*  if (cookies != null) {
                for (Cookie cookie : cookies) {
                    //              String cookieString = cookie.getName() + "="
                    //                      + cookie.getValue() + ";domain=" + cookie.getDomain();
                    String cookieString = cookie.getName() + "=" + cookie.getValue()
                            + ";domain=" + cookie.getDomain() + ";path=/;";
                    cookieManager.setWebViewCookie(url, cookieString);
                }
                CookieSyncManager.getInstance().sync();
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWebViewCookie(String url, WebView webView, String cookie) {

        if (BuildConfig.DEBUG) {
            String beforeCookie = CookieManager.getInstance().getCookie(url);
            Log.w(TAG, "webview_cookie:" + cookie + ",beforeCookie:" + beforeCookie);
        }
        CookieSyncManager.createInstance(AppContext.getInstance());
        CookieManager cookieManager = CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();

        } else {
            cookieManager.removeSessionCookie();

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        CookieSyncManager.getInstance().sync();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        if (cookie != null && cookie.indexOf(";") != -1) {

            String[] split = cookie.split("\\;");
            for (String currentCookie : split) {
                CookieManager.getInstance().setCookie(url, currentCookie);// 设置 Cookie
            }
        } else {
            CookieManager.getInstance().setCookie(url, cookie);// 设置 Cookie

        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cookieManager.flush();
//        }


    }

    public static String getCookie(String url, String domain) {
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
            return sp.getString(url, "");
        }
        if (domain.startsWith("http") ) {
            domain = CookieUtil.getDomainRemoveSchame(domain);
        }
        if (!TextUtils.isEmpty(domain) && sp.contains(domain) && !TextUtils.isEmpty(sp.getString(domain, ""))) {
            return sp.getString(domain, "");
        }
        return "";
    }

    /**
     * 保存cookie到本地，这里我们分别为该url和host设置相同的cookie，其中host可选
     * 这样能使得该cookie的应用范围更广
     */
    public static void saveCookie(String url, String domain, String cookies) {

        SharedPreferences sp = AppContext.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
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
    }

    /**
     * 清除本地Cookie
     *
     * @param context Context
     */
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
    }
}
