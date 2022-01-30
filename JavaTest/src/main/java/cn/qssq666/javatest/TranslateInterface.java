package cn.qssq666.javatest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TranslateInterface {
    // http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world
    @GET("ajax.php?a=fy&f=auto&t=auto&w=hello%20world")
    Call<Translation> getCall();

    @GET("ajax.php?a=fy&f=auto&t=auto")  // URL query string "a=fy&f=auto&t=auto&w={word}" must not have replace block. For dynamic query
//    Call<Translation> getCallByWorld(@Path("word") String word);//错误的用法，这个是用来处理路径的， 而不是？后面的查询
    Call<Translation> getCallByWorld(@Query("w") String word);
    // 注解里传入 网络请求 的部分URL地址
    // Retrofit把网络请求的URL分成了两部分：一部分放在Retrofit对象里，另一部分放在网络请求接口里
    // 如果接口里的url是一个完整的网址，那么放在Retrofit对象里的URL可以忽略
    // getCall()是接受网络请求数据的方法
}