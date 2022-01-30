package cn.qssq666.robot.http.api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ProjectAPI {
    //http://www.baidu.com/aaa?key=123&qq=aaa
 
    @GET
    Call<String> getMethod(@Url String url);
 
    @FormUrlEncoded
    @POST
    Call<String> postMethod(@Url String url, @FieldMap Map<String, String> map);


}
