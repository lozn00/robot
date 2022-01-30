package cn.qssq666.javatest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public interface QueryGet {
    @GET("/")
    Call<String> cate(@Query("cate") String cate);
//    url = http://www.println.net/?cate=android，其中，Query = cate
// 其使用方式同 @Field与@FieldMap，这里不作过多描述
}
