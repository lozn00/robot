package cn.qssq666.robot.webview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.HashMap;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.http.newcache.MyCookieManager;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.openai.OpenAIBiz;
import cn.qssq666.robot.utils.CookieLocalFilePool;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.LogUtil;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "WebViewActivity";
    private WebView _webView;
    private JavaScriptBridge javascriptBridge;
    private ProgressBar progressBar;
    private String url;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        url = intent.getStringExtra("url");
        CookieLocalFilePool.setWebviewCookie(new String[]{url, CookieLocalFilePool.getDomainRemoveSchame(url)}, _webView);
        _webView.loadUrl(url, getRequestHeader(url));
    }

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
        url = getIntent().getStringExtra("url");

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
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        _webView.getSettings().setUserAgentString(OpenAIBiz.UserAgent);
        _webView.setWebChromeClient(new MyWebChromeClient());
        _webView.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url1 = request.getUrl().toString();
                Log.w(TAG, "shouldOverrideUrlLoading LOAD URL:" + url1);
                view.loadUrl(url1, getRequestHeader(url));
                return true;
            }

            @Override //防止点击其它页面的时候自动用系统浏览器等打开
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:") || url.startsWith("ftp:") || url.startsWith("rtmp:") || url.startsWith("rtsp:")) {
                    Log.w(TAG, "shouldOverrideUrlLoading LOAD url-:" + url);
                    view.loadUrl(url, getRequestHeader(url));
                    return true;
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
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
//                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                handler.proceed("", "");
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
//                super.onReceivedClientCertRequest(view, request);
//https://www.jianshu.com/p/793bba641d8a cookie相关
                try {
                    PrivateKey privateKey = null;
                    X509Certificate[] certificates = null;
                    InputStream certificateFileStream = new ByteArrayInputStream(Base64.decode("您的证书内容", Base64.DEFAULT));
                    KeyStore keyStore = KeyStore.getInstance("证书类型");
                    String password = "您的证书密码";
                    keyStore.load(certificateFileStream, password.toCharArray());

                    Enumeration<String> aliases = keyStore.aliases();
                    String alias = aliases.nextElement();

                    Key key = keyStore.getKey(alias, password.toCharArray());
                    if (key instanceof PrivateKey) {
                        privateKey = (PrivateKey) key;
                        Certificate cert = keyStore.getCertificate(alias);
                        certificates = new X509Certificate[1];
                        certificates[0] = (X509Certificate) cert;
                    }

                    final TrustManager[] trustManagers = new TrustManager[]{
                            new X509TrustManager() {
                                @Override
                                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                                }

                                @Override
                                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                    return new java.security.cert.X509Certificate[]{};
                                }
                            }
                    };
                    certificateFileStream.close();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        request.proceed(privateKey, certificates);
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //获取登陆后的cookie,看是否写入
                CookieManager cookiemanager = CookieManager.getInstance();
                String cookie = cookiemanager.getCookie(url);

                LogUtil.writeLog(TAG, "onPageFinishedcookie:" + cookie);
                MyCookieManager myCookieManager = new MyCookieManager();
                myCookieManager.addCookies(CookieLocalFilePool.getCookie(url));
                if (OpenAIBiz.isOpenApiuRL(url)) {
                    myCookieManager.addCookies(RobotContentProvider.getInstance().robotReplySecret);
                    OpenAIBiz.updateOpenAICookie(myCookieManager.getCookies());
                }
                myCookieManager.getCookies();
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        });
        CookieLocalFilePool.setWebviewCookie(new String[]{url, CookieLocalFilePool.getDomainRemoveSchame(url)}, _webView);
        _webView.loadUrl(url, getRequestHeader(url));
//        _webView.loadUrl("file:////android_asset/webview/test.html");


    }

    @NonNull
    public static HashMap<String, String> getRequestHeader(String url) {
        if (OpenAIBiz.isOpenApiuRL(url)) {
            HashMap<String, String> map = OpenAIBiz.genereateBaseHeader();
            map.put("Authorization","Bearer " + RobotContentProvider.getInstance().robotReplyKey);
            map.put("Cookie",RobotContentProvider.getInstance().robotReplySecret);
            return map;

        } else {
            return new HashMap<>();
        }
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
                CookieLocalFilePool.setWebviewCookie(new String[]{url, CookieLocalFilePool.getDomainRemoveSchame(url)}, _webView);
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
