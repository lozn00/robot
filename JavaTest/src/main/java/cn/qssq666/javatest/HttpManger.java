package cn.qssq666.javatest;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
//https://blog.csdn.net/chenjie0932/article/details/79558050
public class HttpManger {
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

}
