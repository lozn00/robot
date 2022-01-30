package cn.qssq666.robot.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.loopj.android.http.PersistentCookieStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.utils.CookieUtil;
import cn.qssq666.robot.utils.DialogUtils;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private WebView _webView;
    private JavaScriptBridge javascriptBridge;
    private ProgressBar progressBar;


    class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //显示进度条
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                //加载完毕隐藏进度条
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            String str = "line:" + consoleMessage.lineNumber() + ":" + consoleMessage.message();
            if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.ERROR) {
                Log.e(TAG, str);

            } else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.WARNING) {

                Log.w(TAG, str);

            } else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.DEBUG) {

                Log.d(TAG, str);

            } else if (consoleMessage.messageLevel() == ConsoleMessage.MessageLevel.TIP) {
                Log.i(TAG, str);
            } else {
                Log.e(TAG, "[" + consoleMessage.messageLevel().name() + "]" + str);
            }
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            setTitle(title);
            super.onReceivedTitle(view, title);
        }


        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            super.onShowCustomView(view, callback);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
            b.setTitle("Alert");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setCancelable(false);
            b.create().show();
            return true;
        }


        //设置响应js 的Confirm()函数
        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
            AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
            b.setTitle("Confirm");
            b.setMessage(message);
            b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            });
            b.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            });
            b.create().show();
            return true;
        }

        //设置响应js 的Prompt()函数
        @Override
        public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
            DialogUtils.showEditDialog(WebViewActivity.this, message, defaultValue, new INotify<String>() {
                @Override
                public void onNotify(String param) {
                    result.confirm(param);
                }
            });
            return true;

        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        _webView = (WebView) findViewById(R.id.webview);
        String url = getIntent().getStringExtra("url");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//      WebView _webView = (WebView) findViewById(R.id.webview);
        WebSettings settings = _webView.getSettings();
        //默认是false 设置true允许和js交互

//        settings.setJavaScriptEnabled(true);
        //  优先加载本地缓存数据
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //开启 DOM storage API 功能 较大存储空间，使用简单
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT > 8) {//<8setPluginsEnabled 没法调用

            settings.setPluginState(WebSettings.PluginState.ON);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        _webView.setWebChromeClient(new MyWebChromeClient());
        _webView.setWebViewClient(new WebViewClient() {
            @Override //防止点击其它页面的时候自动用系统浏览器等打开
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp:") || url.startsWith("rtmp:") || url.startsWith("rtsp:")) {
                    return false;
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } catch (Exception e) {
                        String protocol;
                        int i = url.indexOf("://");
                        if (i != -1) {
                            protocol = url.substring(0, i);
                        } else {
                            protocol = "(协议无法获取)action:" + url;
                        }
                    /*    if (BuildConfig.DEBUG) {
                            Log.e(TAG, "open url exception:" + Log.getStackTraceString(e) + ",url:" + url);
                        }*/
                        AppContext.showToast("无法打开协议:" + protocol + ",如果您认识此协议,请安装对应的APP客户端");
                    }
                    return false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });

//        javascriptBridge = JavaScriptBridge.bind(this, _webView);
//        AddCookiesInterceptor.getCookie()
    /*    String[] cookieArray = cookie.split(";");// 多个Cookie是使用分号分隔的
        for (int i = 0; i < cookieArray.length; i++) {
            int position = cookieArray[i].indexOf("=");// 在Cookie中键值使用等号分隔
            String cookieName = cookieArray[i].substring(0, position);// 获取键
            String cookieValue = cookieArray[i].substring(position + 1);// 获取值

            String value = cookieName + "=" + cookieValue;// 键值对拼接成 value
            Log.i("cookie", value);
//            CookieManager.getInstance().setWebViewCookie(getDomainRemoveSchame(cookiesPath), value);// 设置 Cookie
        }*/


        CookieUtil.setCookie(new String[]{url, Cns.ROBOT_DOMAIN}, _webView);
        _webView.loadUrl(url);
//        _webView.loadUrl("file:////android_asset/webview/test.html");


    }
    /*
       CookieManager.getInstance().removeAllCookie();
                CookieSyncManager.createInstance(this);
                PersistentCookieStore myCookieStore = new PersistentCookieStore(this);

                Map<String, String> cookieMap = new HashMap<>();
                String cookie = getSharedPreferences("cookie", Context.MODE_PRIVATE).getString("cookies", "");// 从SharedPreferences中获取整个Cookie串
                if (!TextUtils.isEmpty(cookie)) {
                    String[] cookieArray = cookie.split(";");// 多个Cookie是使用分号分隔的
                    for (int i = 0; i < cookieArray.length; i++) {
                        int position = cookieArray[i].indexOf("=");// 在Cookie中键值使用等号分隔
                        String cookieName = cookieArray[i].substring(0, position);// 获取键
                        String cookieValue = cookieArray[i].substring(position + 1);// 获取值

                        String value = cookieName + "=" + cookieValue;// 键值对拼接成 value
                        Log.i("cookie", value);
                        CookieManager.getInstance().setWebViewCookie(getDomainRemoveSchame(cookiesPath), value);// 设置 Cookie
                    }
                }
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();

        menuInflater.inflate(R.menu.menu_webview_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_menu_debug).setVisible(BuildConfig.DEBUG);
        return super.onPrepareOptionsMenu(menu);
    }


    public void clearWebViewCache() {
// 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();

// 清除cookie
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);
        myCookieStore.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_menu_refresh:
                _webView.reload();
                break;
            case R.id.action_menu_debug:
             /*   Intent intent=new Intent(this,TestActivity.class);
                startActivity(intent);*/
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return super.getMenuInflater();
    }

    @Override
    public void onBackPressed() {
        if (_webView.canGoBack()) {
            _webView.goBack();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        javascriptBridge.onDestory();
        _webView.destroy();
//        EventBus.getDefault().unregister(this);

    }

}
