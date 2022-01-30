package cn.qssq666.javatest;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public class UseDemo {
    public void test(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fanyi.youdao.com/") // 设置网络请求的Url地址
//                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 支持RxJava平台
                .build();
    }

    public void synaandasync(){
        //发送网络请求(异步)
      /*  call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                //请求处理,输出结果
                response.body().show();
            }

            //请求失败时候的回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                System.out.println("连接失败");
            }
        });

// 发送网络请求（同步）
        Response<Reception> response = call.execute();*/
    }
    //http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=%E4%BD%A0%E5%A5%BD {"status":1,"content":{"from":"en-EU","to":"zh-CN","out":"\u793a\u4f8b","vendor":"ciba","err_no":0}}
    public void testTransLate(){

    }
}
