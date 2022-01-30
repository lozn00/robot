package cn.qssq666.robot.http.api;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QRCode {
    // URL query string "a=fy&f=auto&t=auto&w={word}" must not have replace block. For dynamic query
//    Call<Translation> getCallByWorld(@Path("word") String word);//错误的用法，这个是用来处理路径的， 而不是？后面的查询
    @GET("?")
    Observable<String> query(@Query("type") int type,@Query("money") double money,@Query("token") String token);

}