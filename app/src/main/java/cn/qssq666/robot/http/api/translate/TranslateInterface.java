package cn.qssq666.robot.http.api.translate;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;

public interface TranslateInterface {
        // URL query string "a=fy&f=auto&t=auto&w={word}" must not have replace block. For dynamic query
//    Call<Translation> getCallByWorld(@Path("word") String word);//错误的用法，这个是用来处理路径的， 而不是？后面的查询
//        @Headers("apikey:b86c2269fe6588bbe3b41924bb2f2da2")
    @GET("translate?&doctype=json&type=")
//    @GET("ajax.php?a=fy&f=auto&t=auto")
    Call<String> getCallByWord(@HeaderMap Map<String, String> headers, @Query("i") String word);
    @GET("translate?&doctype=json&type=EN2ZH_CN")
    Call<String> getEnglish2Word(@HeaderMap Map<String, String> headers, @Query("i") String word);
}