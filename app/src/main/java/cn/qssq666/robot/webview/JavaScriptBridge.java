package cn.qssq666.robot.webview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.Random;

import cn.qssq666.robot.BuildConfig;


public class JavaScriptBridge {
    protected Handler _handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
    public static final String TAG = "JavascriptBridge";
    protected Context context;
    protected WebView webView;


    /* Access modifiers changed, original: protected */
    public Context getContext() {
        return this.context;
    }

    public static JavaScriptBridge bind(Context context, WebView webView) {
        return bind(webView, new JavaScriptBridge(context, webView));
    }

    @SuppressLint("JavascriptInterface")
    public static JavaScriptBridge bind(WebView webView, JavaScriptBridge jsInterface) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(jsInterface, "MyPayNative");
        return jsInterface;
    }

    public JavaScriptBridge(Context context, WebView webView) {
        this.context = context;
        this.webView = webView;
    }


    @JavascriptInterface
    public void onLoginSucc(String json) {
        Log.w(TAG, "onLoginSucc:" + json);
    }




    @JavascriptInterface
    public String getNativeConfigByAction(String action, String[] args) {
        Log.w(TAG, "getNativeConfigByAction action:" + action + ",arg:" + Arrays.toString(args));
        return "_" + new Random().nextInt() + "_";
    }

    public void execJsCode(final String javascript) {
        this._handler.post(new Runnable() {
            public void run() {

                if (Build.VERSION.SDK_INT >= 19) {
                    JavaScriptBridge.this.webView.evaluateJavascript(javascript, new ValueCallback<String>() {
                        public void onReceiveValue(String value) {
                            if (BuildConfig.DEBUG) {
                                Log.w(TAG, "exec_js:" + value);
                            }
                        }
                    });
                    return;
                }
                WebView webView = JavaScriptBridge.this.webView;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("javascript:");
                stringBuilder.append(javascript);
                webView.loadUrl(stringBuilder.toString());
            }
        });
    }


    public void onDestory() {
        this.webView = null;
        this.context = null;
    }


}
