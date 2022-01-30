package cn.qssq666.javatest;

import com.google.gson.Gson;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyClass {
    //简书参考 转换器 https://www.jianshu.com/p/e438594d9c93
    public static void main(String[] args) throws IOException {
        System.out.println("....");
//        test1();



        Observable.just(1, 5,2)
                .scan(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(@NonNull Integer integer, @NonNull Integer integer2) throws Exception {
                        System.out.println("arg1:"+integer+",arg2:"+integer2);
                        return integer + integer2;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                System.out.println( "accept: scan " + integer + "\n");
            }
        });


    }

    private static void test1() throws IOException {

        //: http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(5, TimeUnit.SECONDS);
//add log record
//打印网络请求日志
        LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor.Builder()
                .loggable(true)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build();
        httpClientBuilder.addInterceptor(httpLoggingInterceptor);
//        httpClientBuilder.addInterceptor(chain -> addAuthIntercepter(chain))
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/")
                .client(httpClientBuilder.build()) //自定义okhttp . 不写也是可以的。
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson())) //使用这个拦截器可以实现注解返回的对象直接是一个bean ,
                .build();

        TranslateInterface translateInterface = retrofit.create(TranslateInterface.class);


//        translateInterface.getCall().enqueue();//异步
//        Response<Translation> execute = translateInterface.getCall().execute();
        Response<Translation> execute = translateInterface.getCallByWorld("good").execute();

        execute.body().show();
    }

    private static void test2() throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://fy.iciba.com/")
//                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build();

        TranslateInterface translateInterface = retrofit.create(TranslateInterface.class);


//        translateInterface.getCall().enqueue();//异步
        Response<Translation> execute = translateInterface.getCall().execute();

        execute.body().show();
    }
}
