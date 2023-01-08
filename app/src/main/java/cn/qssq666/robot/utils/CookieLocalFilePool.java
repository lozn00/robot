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

import java.util.Map;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.http.newcache.MyCookieManager;

public class CookieLocalFilePool {
    private static final String TAG = "CookieUtil";
    public static final String COOKIE_PREF = "cookies_prefs";
    public static void setCookie5X(String[] url, com.tencent.smtt.sdk.WebView webView) {
        String domainRemoveSchame = CookieLocalFilePool.getDomainRemoveSchame(url[0]);
        domainRemoveSchame=getDomainRemoveSchame(domainRemoveSchame);
        String cookie = getCookie(url[0], domainRemoveSchame);
        if(TextUtils.isEmpty(cookie)){
            return;
        }
        for (String s : url) {
            setWebViewCookie5X(s, webView, cookie);
        }
    }
    public static void setWebViewCookie5X(String url, com.tencent.smtt.sdk.WebView webView, String cookie) {
        if (BuildConfig.DEBUG) {
            String beforeCookie = com.tencent.smtt.sdk.CookieManager.getInstance().getCookie(url);
            Log.w(TAG, "webview_cookie:" + cookie + ",beforeCookie:" + beforeCookie);
        }
        com.tencent.smtt.sdk.CookieSyncManager.createInstance(AppContext.getContext());
        com.tencent.smtt.sdk.CookieManager cookieManager = com.tencent.smtt.sdk.CookieManager.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies(null);
            cookieManager.flush();
        } else {
            cookieManager.removeSessionCookie();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&webView!=null) {
            com.tencent.smtt.sdk.CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        }
        com.tencent.smtt.sdk.CookieSyncManager.getInstance().sync();
        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        if (cookie != null && cookie.indexOf(";") != -1) {
            String[] split = cookie.split(";");
            for (String currentCookie : split) {
                com.tencent.smtt.sdk.CookieManager.getInstance().setCookie(url, currentCookie);// 设置 Cookie
            }
        } else {
            com.tencent.smtt.sdk.CookieManager.getInstance().setCookie(url, cookie);// 设置 Cookie

        }

    }
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

    public static void setWebviewCookie(String[] url, WebView webView) {
        String cookie = getCookie(url[0], CookieLocalFilePool.getDomainRemoveSchame(url[0]));
        for (String s : url) {

            setWebViewCookie(s, webView, cookie);

        }
    }

    public static void clear() {

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

        LogUtil.writeLog(url + ",setWebViewCookie-setCookie:" + cookie);
        MyCookieManager myCookieManager = new MyCookieManager();
//        if (BuildConfig.DEBUG) {
            String beforeCookie = CookieManager.getInstance().getCookie(url);
            if (!TextUtils.isEmpty(beforeCookie)) {
                myCookieManager.addCookies(beforeCookie);
                myCookieManager.addCookies(cookie);
                cookie = myCookieManager.getCookies();
            } else {
                myCookieManager.addCookies(cookie);
            }
            Log.w(TAG, "webview_cookie:" + cookie + ",beforeCookie:" + beforeCookie);
//        }
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

//        cookieManager.removeAllCookie();
        cookieManager.setAcceptCookie(true);
        /*        if (cookie != null && cookie.indexOf(";") != -1) {

         *//*     String[] split = cookie.split("\\;");
            for (String currentCookie : split) {
                CookieManager.getInstance().setCookie(url, currentCookie);// 设置 Cookie
            }*//*
            CookieManager.getInstance().setCookie(url, cookie);// 设置 Cookie
        } else {
            CookieManager.getInstance().setCookie(url, cookie);// 设置 Cookie
        }*/
            String domainRemoveSchame = getDomainRemoveSchame(url);
        for (Map.Entry<String, String> entry : myCookieManager.cookiesMap.entrySet()) {
            cookieManager.setCookie(url, entry.getKey()+"=" + entry.getValue());
/*
            cookieManager.setCookie(url, entry.getKey()+"=" + entry.getValue()
                    + ";Domain="+ domainRemoveSchame +""
                    + ";Path=/");
*/
        }
        CookieSyncManager.getInstance().sync();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            cookieManager.flush();
//        }


    }

    public static String getCookie(String url) {
        return getCookie(url, getDomainRemoveSchame(url));
    }

    public static String getCookie(String url, String domain) {
        SharedPreferences sp = AppContext.getContext().getSharedPreferences(COOKIE_PREF, Context.MODE_PRIVATE);
        if (!TextUtils.isEmpty(url) && sp.contains(url) && !TextUtils.isEmpty(sp.getString(url, ""))) {
            return sp.getString(url, "");
        }
        if (domain.startsWith("http")) {
            domain = CookieLocalFilePool.getDomainRemoveSchame(domain);
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
