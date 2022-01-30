package cn.qssq666.robot.business;
import cn.qssq666.CoreLibrary0;import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import cn.qssq666.robot.interfaces.INotify;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by qssq on 2018/1/24 qssq666@foxmail.com
 */

public class OkHttpUtil {


    /**
     * 耗时 非线程
     *
     * @param url
     * @return
     */
    public static String syncRequest(String url) throws IOException {
        {
            return syncRequest(url, null);
        }
    }

    public static class ReceivedCookiesInterceptor implements Interceptor {
        private final INotify<HashSet<String>> chookieNotify;

        public ReceivedCookiesInterceptor(INotify<HashSet<String>> chookieNotify) {
            this.chookieNotify = chookieNotify;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());

            if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                HashSet<String> cookies = new HashSet<>();

                for (String header : originalResponse.headers("Set-Cookie")) {
                    cookies.add(header);

                }
                if (chookieNotify != null) {
                    chookieNotify.onNotify(cookies);
                }


            }

            return originalResponse;
        }
    }

    public static String syncRequest(String url, INotify<OkHttpClient.Builder> okHttpClientCreateINotify) throws IOException {
//    http://songsearch.kugou.com/song_search_v2?&keyword=%E8%80%81%E7%94%B7%E5%AD%A9&page=1&pagesize=1&showtype=1&iscorrection=1&platform=WebFilter

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (okHttpClientCreateINotify != null) {
            okHttpClientCreateINotify.onNotify(builder);
        }

        CacheControl cacheControl = new CacheControl.Builder().build();
        final Request request = new Request.Builder()
                .url(url)

                .cacheControl(cacheControl)
                .get()
                .build();
        OkHttpClient okHttpClient = builder.build();
        okhttp3.Response temp = okHttpClient.newCall(request).execute();
        ResponseBody body = temp.body();
        if (temp.isSuccessful()) {
            //call string auto close body
            String content = body.string();
            return content;

        } else {
            temp.body().close();
            return null;
        }


    }

    public static String syncPostRequest(String url, RequestBody requestBody, INotify<OkHttpClient.Builder> okHttpClientCreateINotify) throws IOException {
//    http://songsearch.kugou.com/song_search_v2?&keyword=%E8%80%81%E7%94%B7%E5%AD%A9&page=1&pagesize=1&showtype=1&iscorrection=1&platform=WebFilter

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (okHttpClientCreateINotify != null) {
            okHttpClientCreateINotify.onNotify(builder);
        }

        CacheControl cacheControl = new CacheControl.Builder().build();

        final Request request = new Request.Builder()
                .url(url)
                .cacheControl(cacheControl)
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = builder.build();
        okhttp3.Response temp = okHttpClient.newCall(request).execute();
        ResponseBody body = temp.body();
        if (temp.isSuccessful() || temp.code() == 200) {
            //call string auto close body
            String content = body.string();
            return content;

        } else {
            temp.body().close();
            Log.e("okhttp","requestFail:"+temp.code()+"");
            return null;
        }


    }


}
