package cn.qssq666.robot.http;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.qssq666.robot.http.api.ProjectAPI;
import cn.qssq666.robot.utils.HttpUtil;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

//https://blog.csdn.net/chenjie0932/article/details/79558050
public class HttpUtilRetrofit {
    /**
     * @param baseUrl  基础Url
     * @param url      附加Url
     * @param callback 添加请求回调，这里直接使用的是Retrofit自身的回调接口
     */
    public static void getMethod(String baseUrl, String url, final Callback<String> callback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(ScalarsConverterFactory.create()).build();

        ProjectAPI projectAPI = retrofit.create(ProjectAPI.class);

        Call<String> call = projectAPI.getMethod(url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //调用回调
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //调用回调
                callback.onFailure(call, t);
            }
        });
    }

    public static OkHttpClient.Builder buildOKHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.SECONDS);
        return builder;

//        builder.addInterceptor(new CookieInterceptor(basePar.isCache(), basePar.getUrl()));
    }

    /* */

    /**
     * 日志输出
     * 自行判定是否添加
     *
     * @return
     *//*
    private HttpLoggingInterceptor getHttpLoggingInterceptor(){
        //日志显示级别
        HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("RxRetrofit","Retrofit====Message:"+message);
            }
        });
        loggingInterceptor.setLevel(level);
        return loggingInterceptor;
    }*/
    public static void postMethod(String baseUrl, String url, Map<String, String> map, final Callback<String> callback) {
        //指定客户端
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
//                . client(httpClient)
                .addConverterFactory(ScalarsConverterFactory.create()).build();

        ProjectAPI projectAPI = retrofit.create(ProjectAPI.class);
        Call<String> call = projectAPI.postMethod(url, map);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });
    }


    public static <T> Retrofit buildRetrofit(String baseUrl) {

        OkHttpClient.Builder okHttpClientBuilder = HttpUtil.createOKHttpBuilder();
//        okHttpBuilder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS).
                readTimeout(10, TimeUnit.SECONDS).
                writeTimeout(10, TimeUnit.SECONDS);
        OkHttpClient build = okHttpClientBuilder.build();



        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(build)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }

    public static <T> Retrofit buildLongTimeRetrofit(String baseUrl,boolean isManager) {

        OkHttpClient.Builder okHttpClientBuilder = HttpUtil.createOKHttpBuilder();
//        okHttpBuilder.addHeader(stringStringEntry.getKey(), stringStringEntry.getValue());
        okHttpClientBuilder.connectTimeout(70, TimeUnit.SECONDS).
                readTimeout(isManager?400:70, TimeUnit.SECONDS).
                callTimeout(isManager?400:70, TimeUnit.SECONDS).
                writeTimeout(isManager?400:70, TimeUnit.SECONDS);
        OkHttpClient build = okHttpClientBuilder.build();



        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(build)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit;
    }
}
