package cn.qssq666.robot.business.ext;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public interface DataService {
    @GET
    Call<String> baidu(@Url String url);

    @GET("/qqevaluate/qq")
    Call<QQData> getQQData(@Query("key") String appkey, @Query("qq") String qq);

}
