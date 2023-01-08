package cn.qssq666.robot.http.newcache;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.http.AddCookiesInterceptor;
import cn.qssq666.robot.http.SaveCookiesInterceptor;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

import javax.net.ssl.*;


public class HttpUtil {


    private static final String TAG = "HttpUtil"; //tag:UpdateService  tag:HttpUtil

    public interface RequestListener {
        void onSuccess(String str);

        void onFail(String str);

    }

    /**
     * 回调依然是异步线程非主线程
     *
     * @param url
     * @param listener
     * @return
     */
    public static PairX<Call, Request> queryData(String url, final RequestListener listener) {
        OkHttpClient okHttpClient = buildOkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                listener.onFail("code=" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    String str = response.body().string();
                    if (response.isSuccessful()) {
                        listener.onSuccess(str);

                    } else {
                        listener.onFail(str);
                    }

                } else {
                    listener.onFail("status error:" + code + "," + response.body().string());
                }

            }

        });
        return PairX.create(call, request);

    }

    public static PairX<Call, Request> queryPostData(String url, RequestBody requestBody, Callback callback) {
        return queryPostData(url, null, requestBody, callback);
    }

    public static PairX<Call, Request> queryPostData(String url, HashMap<String, String> headMap, RequestBody requestBody, Callback callback) {
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
        return PairX.create(call, request);

    }


    public static PairX<Call, Request> queryGetData(String url, Callback callback) {
        return queryGetData(url, null, callback);
    }

    public static PairX<Call, Request> queryGetData(String url, HashMap<String, String> headMap, Callback callback) {
        return queryGetData(url, headMap, true, null, callback);
    }

    public static PairX<Call, Request> queryGetData(String url, HashMap<String, String> headMap, boolean allowCookie, Proxy proxy, Callback callback) {
        OkHttpClient okHttpClient = buildOkHttpClient(allowCookie, proxy);


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

        return PairX.create(call, request);

    }


    /**
     * 同步发起请求
     *
     * @param url
     */
    public static Response querySyncGetData(String url, HashMap<String, String> headMap) throws IOException {
        return querySyncGetDataCancelable(url, headMap, null, null);
    }

    public static Response querySyncGetData(String url, HashMap<String, String> headMap, Proxy proxy) throws IOException {
        return querySyncGetDataCancelable(url, headMap, null, proxy);
    }

    public static Response querySyncGetDataCancelable(String url, HashMap<String, String> headMap, PairX<Call, Response> bodyPairX, Proxy proxy) throws IOException {

        OkHttpClient okhttpClient = buildOkHttpClient(true, proxy);
        return querySyncGetDataCancelable(url, okhttpClient, headMap, bodyPairX);
    }

    public static String getUserAgent(String userAgent) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static Response querySyncGetDataCancelable(String url, OkHttpClient okHttpClient, HashMap<String, String> headMap, PairX<Call, Response> bodyPairX) throws IOException {
        Request.Builder builder = new Request.Builder()

                .url(url);

        if (headMap != null) {
            for (Map.Entry<String, String> stringStringEntry : headMap.entrySet()) {

                builder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
            }

        }
//        builder.removeHeader("User-Agent").addHeader("User-Agent", getUserAgent());
        Request request = builder
                .build();
        Call call = okHttpClient.newCall(request);
        if (bodyPairX != null) {
            bodyPairX.first = call;
        }
        Response response = call.execute();
        if (bodyPairX != null) {
            bodyPairX.second = response;
        }
        return response;

    }

    /**
     * @param url
     * @param headMap
     * @param requestBody FormBody 为集成类
     * @return
     * @throws IOException
     */

    public static Response querySyncPostData(String url, HashMap<String, String> headMap, RequestBody requestBody) throws IOException {
        return querySyncPostData(url, headMap, requestBody, false);
    }

    public static Response querySyncPostData(String url, HashMap<String, String> headMap, RequestBody requestBody, Proxy proxy) throws IOException {
        return querySyncPostDataCancelable(url, headMap, requestBody, false, proxy, null);
    }

    public static Response querySyncPostData(String url, HashMap<String, String> headMap, RequestBody requestBody, boolean bigData) throws IOException {
        return querySyncPostDataCancelable(url, headMap, requestBody, bigData, null, null);
    }

    public static Response querySyncPostDataCancelable(String url, HashMap<String, String> headMap, RequestBody requestBody, boolean bigData, Proxy proxy, PairX<Call, Response> bodyPairX) throws IOException {

        OkHttpClient httpClient;
        if (bigData) {
            httpClient = createOKHttpBuilder(true, proxy).build();
        } else {
            httpClient = buildOkHttpClient(true, proxy);

        }
        return querySyncPostDataCancelable(url, httpClient, headMap, requestBody, bigData, bodyPairX);

    }

    public static Response querySyncPostDataCancelable(String url, OkHttpClient okHttpClient, HashMap<String, String> headMap, RequestBody requestBody, boolean bigData, PairX<Call, Response> bodyPairX) throws IOException {


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

        Call call = okHttpClient.newCall(request);
        if (bodyPairX != null) {
            bodyPairX.first = call;
        }
        Response response = call.execute();
        if (bodyPairX != null && bodyPairX.second != null) {
            bodyPairX.second = response;
        }
        return response;
    }

    public static OkHttpClient.Builder createOKHttpBuilder() {
        return createOKHttpBuilder(true, null);
    }

    public static OkHttpClient.Builder createOKHttpBuilder(boolean allowCookie, Proxy proxy) {
        return createOKHttpBuilder(allowCookie, false, proxy);
    }

    public static OkHttpClient.Builder createOKHttpBuilder(boolean allowCookie, boolean bigData, Proxy proxy) {
        OkHttpClient.Builder builder = null;
        if (proxy != null) {
            builder = new OkHttpClient.Builder()
                    .proxy(proxy);

        } else {
            builder = new OkHttpClient.Builder();

        }

        // Create a trust manager that does not validate certificate chains
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

        // Install the all-trusting trust manager
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManagers, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        // Create an ssl socket factory with our all-trusting manager

        builder.connectTimeout(15, TimeUnit.SECONDS);//设置连接超时时间
        builder.readTimeout(bigData ? 60 * 5 : 30, TimeUnit.SECONDS);//设置读取超时时间
        builder.writeTimeout(bigData ? 60 * 5 : 30, TimeUnit.SECONDS); //写超时
        return initBuilderParam(allowCookie, bigData, builder);
    }

    public static OkHttpClient.Builder initBuilderParam(boolean allowCookie, boolean bigData, OkHttpClient.Builder builder) {
        if (allowCookie) {

            builder.addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new SaveCookiesInterceptor());
        }


        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLog());
        if (BuildConfig.DEBUG) {
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        }
        builder.addNetworkInterceptor(logInterceptor);

        return builder;
    }


    public interface OnDownloadListener {

        void onDownloadFailed(Throwable e);

        void onDownloading(float progress, long current, long total, boolean done, String title);

        void onSuccess();

        void onWriteFile(float progress, long sum, long total, boolean b, String title);
    }


    /**
     * 添加进度监听的ResponseBody
     */
    public static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final OnDownloadListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, OnDownloadListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }


        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    long contentLength = responseBody.contentLength();
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    float progress = (totalBytesRead * 1.0f / contentLength * 100f);
                    progressListener.onDownloading(progress, totalBytesRead, contentLength, bytesRead == -1, "正在更新");
                    return bytesRead;
                }
            };
        }
    }


    /* interface ProgressListener {
     *//**
     * @param bytesRead     已下载字节数
     * @param contentLength 总字节数
     * @param done          是否下载完成
     *//*
        void update(long bytesRead, long contentLength, boolean done);
    }*/

    /**
     * @param url      下载连接
     * @param filePath 储存下载文件的SDCard目录
     * @param listener 下载监听
     * @return
     */
    public static PairX<Request, Call> download(final String url, final String filePath, final OnDownloadListener listener) {
//        MyLog.Log("Jason", "DownloadUtil download start");
        OkHttpClient.Builder okHttpBuilder = createOKHttpBuilder(true, null);
        okHttpBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder().body(
                                new ProgressResponseBody(originalResponse.body(), listener))
                        .build();

            }
        });
        OkHttpClient okhttpClient = okHttpBuilder.build();
        Request.Builder builder = new Request.Builder();

        Request request = builder
                .url(url)
                .build();
        //添加拦截器，自定义ResponseBody，添加下载进度

        Call call = okhttpClient.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.Log("", "DownloadUtil onFailure e:" + e.getMessage());
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MyLog.Log(TAG, "DownloadUtil onResponse start");
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                //String savePath = isExistDir(saveDir);
                MyLog.Log(TAG, "Download filePath:" + filePath);
                try {
                    if (!response.isSuccessful()) {
                        throw new RuntimeException(response.body().string());
                    }
                    is = response.body().byteStream();
                    long total = response.body().contentLength(); //这里其实已经下载完毕了
                    //File file = new File(savePath, getNameFromUrl(url));
                    File file = new File(filePath);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        float progress = (sum * 1.0f / total * 100f);
//                        int process= (int) (a*1.0/total*100f);
                        // 下载中
                        listener.onWriteFile(progress, sum, total, true, "正在写入");
                    }
                    fos.flush();
                    // 下载完成
                    listener.onSuccess();
                } catch (Throwable e) {
                    MyLog.e(TAG, "下载失败 e1:" + e.getMessage(), e);
                    listener.onDownloadFailed(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        MyLog.Log("Jason", "Download onResponse e2:" + e.getMessage());
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        MyLog.Log("Jason", "Download onResponse e3:" + e.getMessage());
                    }
                }
            }

        });
        return PairX.create(request, call);
    }

    /**
     * 并不是实时监听下载
     *
     * @param url
     * @param filePath
     * @param listener
     * @return
     */
    public static PairX<Request, Call> downloadOld(final String url, final String filePath, final OnDownloadListener listener) {
        MyLog.Log("Jason", "DownloadUtil download start");
//        Request request = new Request.Builder().url(url).build();
        OkHttpClient okhttpClient = buildOkHttpClient(true, null);


        Request.Builder builder = new Request.Builder();

        Request request = builder
                .url(url)
                .build();
        Call call = okhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                MyLog.Log("Jason", "DownloadUtil onFailure e:" + e.getMessage());
                listener.onDownloadFailed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                MyLog.Log(TAG, "DownloadUtil onResponse start");
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                //String savePath = isExistDir(saveDir);
                MyLog.Log(TAG, "Download filePath:" + filePath);
                try {
                    if (!response.isSuccessful()) {
                        throw new RuntimeException(response.body().string());
                    }
                    is = response.body().byteStream();
                    long total = response.body().contentLength(); //这里其实已经下载完毕了
                    //File file = new File(savePath, getNameFromUrl(url));
                    File file = new File(filePath);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100f);
//                        int process= (int) (a*1.0/total*100f);
                        // 下载中
                        listener.onDownloading(progress, sum, total, true, "正在更新");
                    }
                    fos.flush();
                    // 下载完成
                    listener.onSuccess();
                } catch (Throwable e) {
                    MyLog.e(TAG, "下载失败 e1:" + e.getMessage(), e);
                    listener.onDownloadFailed(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                    } catch (IOException e) {
                        MyLog.Log("Jason", "Download onResponse e2:" + e.getMessage());
                    }
                    try {
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        MyLog.Log("Jason", "Download onResponse e3:" + e.getMessage());
                    }
                }
            }

        });
        return PairX.create(request, call);
    }

    public static OkHttpClient buildOkHttpClient() {
        return buildOkHttpClient(false, null);
    }

    public static OkHttpClient buildOkHttpClient(boolean allowCookie) {
        return buildOkHttpClient(allowCookie, null);
    }

    public static OkHttpClient buildOkHttpClient(boolean allowCookie, Proxy proxy) {
        OkHttpClient.Builder okHttpBuilder = createOKHttpBuilder(allowCookie, proxy);

        OkHttpClient okHttpClient = okHttpBuilder.build();
        return okHttpClient;
    }
}
