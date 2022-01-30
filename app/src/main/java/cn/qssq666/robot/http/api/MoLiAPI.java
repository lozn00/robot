package cn.qssq666.robot.http.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MoLiAPI {
    //http://www.baidu.com/aaa?key=123&qq=aaa


    @FormUrlEncoded
    @POST("/api.php")
    Call<String> query( @FieldMap Map<String, String> map);
}
