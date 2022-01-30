package cn.qssq666.javatest;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetRequest_Interface3 {
    /**
     *表明是一个表单格式的请求（Content-Type:application/x-www-form-urlencoded）
     * <code>Field("username")</code> 表示将后面的 <code>String name</code> 中name的取值作为 username 的值
     */
    @POST("/form")
    @FormUrlEncoded
    Call<ResponseBody> testFormUrlEncoded1(@Field("username") String name, @Field("age") int age);

    /**
     * Map的key作为表单的键
     */
    @POST("/form")
    @FormUrlEncoded
    Call<ResponseBody> testFormUrlEncoded2(@FieldMap Map<String, Object> map);

}
/*

    // 具体使用
    // @Field
    Call<ResponseBody> call1 = service.testFormUrlEncoded1("Carson", 24);

    // @FieldMap
    // 实现的效果与上面相同，但要传入Map
    Map<String, Object> map = new HashMap<>();
        map.put("username", "Carson");
                map.put("age", 24);
                Call<ResponseBody> call2 = service.testFormUrlEncoded2(map);
        原文：https://blog.csdn.net/carson_ho/article/details/73732076
        版权声明：本文为博主原创文章，转载请附上博文链接！
 */