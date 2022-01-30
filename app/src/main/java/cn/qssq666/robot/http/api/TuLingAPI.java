package cn.qssq666.robot.http.api;

import cn.qssq666.robot.constants.KeepConstant;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by qssq on 2019/1/14 qssq666@foxmail.com
 */
public interface TuLingAPI {
    //TULING_DOMAIN
    @FormUrlEncoded
    @POST("/openapi/api")
    Observable<String> query(@Field(KeepConstant.key) String key, @Field(KeepConstant.info) String word, @Field(KeepConstant.userid) String userId);
}
