package cn.qssq666.javatest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public interface PathFormatTest {
    @GET("users/{user}/repos")
    Call<ResponseBody> getBlog(@Path("user") String user);
    // 访问的API是：https://api.github.com/users/{user}/repos
    // 在发起请求时， {user} 会被替换为方法的第一个参数 user（被@Path注解作用）
}
