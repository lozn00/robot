package cn.qssq666.robot.utils;

import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.http.AddCookiesInterceptor;
import cn.qssq666.robot.http.HttpLog;
import cn.qssq666.robot.http.SaveCookiesInterceptor;
import cn.qssq666.robot.interfaces.RequestListener;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by luozheng on 2017/3/12.  qssq.space
 */

public class HttpUtil {

    public static void queryData(String url, final RequestListener listener) {
        OkHttpClient okHttpClient = buildOkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (BuildConfig.DEBUG) {
                    Log.w("HttpUtils", call + "," + Log.getStackTraceString(e));

                }
                listener.onFail(Log.getStackTraceString(e));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String str = response.body().string();
                    if (BuildConfig.DEBUG) {

                        Log.w("HttpUtils", call + ",succ[" + str + "]" + ",   response.isSuccessful()" + response.isSuccessful() + ",");

                    }
                    if (response.isSuccessful()) {
                        listener.onSuccess(str);

                    } else {
                        listener.onFail(str);
                    }

                } else {
                    listener.onFail("状态码错误:" + code + "," + response.body().string());
                }

            }

        });


    }

    public static void queryPostData(String url, RequestBody requestBody, okhttp3.Callback callback) {
        queryPostData(url, null, requestBody, callback);
    }

    public static void queryPostData(String url, HashMap<String, String> headMap, RequestBody requestBody, okhttp3.Callback callback) {
        OkHttpClient okHttpClient = buildOkHttpClient();

        Request.Builder requestBuilder = new Request.Builder();
        if (headMap != null) {
            for (Map.Entry<String, String> stringStringEntry : headMap.entrySet()) {

                requestBuilder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }

        Request request = requestBuilder
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);


    }


    public static void queryGetData(String url, okhttp3.Callback callback) {
        queryGetData(url, null, callback);
    }

    public static void queryGetData(String url, HashMap<String, String> headMap, okhttp3.Callback callback) {
        queryGetData(url,headMap,true,callback);
    }

    public static void queryGetData(String url, HashMap<String, String> headMap, boolean allowCookie,okhttp3.Callback callback) {
        OkHttpClient okHttpClient = buildOkHttpClient(allowCookie);
        Request.Builder requestBuilder = new Request.Builder();

        if (headMap != null) {
            for (Map.Entry<String, String> stringStringEntry : headMap.entrySet()) {

                requestBuilder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }
        Request request = requestBuilder
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);


    }


    /**
     * 同步发起请求
     *
     * @param url
     */
    public static Response querySyncGetData(String url, HashMap<String, String> headMap) throws IOException {
        OkHttpClient mOkHttpClient = buildOkHttpClient();
        Request.Builder builder = new Request.Builder()

                .url(url);

        if (headMap != null) {
            for (Map.Entry<String, String> stringStringEntry : headMap.entrySet()) {

                builder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }
        Request request = builder
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;

    }


    public static Response querySyncPostData(String url, HashMap<String, String> headMap, RequestBody requestBody) throws IOException {
        OkHttpClient mOkHttpClient = buildOkHttpClient();
        Request.Builder builder = new Request.Builder();

        if (headMap != null) {
            for (Map.Entry<String, String> stringStringEntry : headMap.entrySet()) {

                builder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }
        }
        Request request = builder
                .url(url)
                .post(requestBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return response;
    }
    public static OkHttpClient.Builder createOKHttpBuilder() {
        return createOKHttpBuilder(true);
    }
    public static OkHttpClient.Builder createOKHttpBuilder(boolean allowCookie) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(allowCookie){

        builder.addInterceptor(new AddCookiesInterceptor())
                .addInterceptor(new SaveCookiesInterceptor());
        }

        if (BuildConfig.DEBUG&&allowCookie) {
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLog());
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(logInterceptor);
        }
        return builder;
    }

    public static OkHttpClient buildOkHttpClient() {
        return buildOkHttpClient(true);
    }
    public static OkHttpClient buildOkHttpClient(boolean allowCookie) {
        OkHttpClient okHttpClient = createOKHttpBuilder(allowCookie).build();
        return okHttpClient;
    }
}
