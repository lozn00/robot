package cn.qssq666.robot.business.ext;
import cn.qssq666.CoreLibrary0;
import android.widget.Toast;

import cn.qssq666.robot.app.AppContext;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by qssq on 2019/1/10 qssq666@foxmail.com
 */
public class RetrofitTestUtil {

    public static void test() {
        Retrofit retrofit = new Retrofit.Builder()
                //指定baseurl，这里有坑，最后后缀出带着“/”
                .baseUrl("http://www.baidu.com/")
                //设置内容格式,这种对应的数据返回值是String类型
                .addConverterFactory(ScalarsConverterFactory.create())
                //定义client类型
                .client(new OkHttpClient())
                //创建
                .build();
        //通过retrofit和定义的有网络访问方法的接口关联
        DataService dataService = retrofit.create(DataService.class);
        //在这里又重新设定了一下baidu的地址，是因为Retrofit要求传入具体，如果是决定路径的话，路径会将baseUrl覆盖掉
        Call<String> baidu = dataService.baidu("http://wwww.baidu.com");
        //执行异步请求
        baidu.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(AppContext.getInstance(), response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(AppContext.getInstance(), "fail:" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
