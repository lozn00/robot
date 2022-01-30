package cn.qssq666.robot.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import androidx.test.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.business.OkHttpUtil;
import cn.qssq666.robot.business.ext.DataService;
import cn.qssq666.robot.plugin.lua.util.LuaUtil;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static cn.qssq666.robot.activity.MainActivity.TEST;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private static final String TAG = "ExampleInstrumentedTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
//        Toast.makeText(appContext, "" + TEST, Toast.LENGTH_SHORT).show();
        final Object lock = new Object();
        synchronized (lock) {


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
                    Log.w(TAG, "onResponse:" + response.body());
                    Toast.makeText(AppContext.getInstance(), response.body(), Toast.LENGTH_SHORT).show();
                    lock.notify();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    lock.notify();
                    Log.w(TAG, "onFailure:" + t);
                    Toast.makeText(AppContext.getInstance(), "fail:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        synchronized (lock) {
            lock.wait();

        }

//        testBaidu();
        Log.w(TAG, "执行线程查询");
        assertEquals("cn.qssq666.robot", appContext.getPackageName());
        Log.w(TAG, "测试结束");


    }

    private void testBaidu() throws InterruptedException {
        final Object lock = new Object();
        synchronized (ExampleInstrumentedTest.class) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String s = OkHttpUtil.syncRequest("http://baidu.com");
                        Log.w(TAG, "查询结果:" + s);
                    } catch (IOException e) {
                        Log.w(TAG, "执行请求错误" + Log.getStackTraceString(e));
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(10);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock) {
                        lock.notify();
                    }
                }
            });
    /*
    : 执行请求错误java.net.UnknownServiceException: CLEARTEXT communication to baidu.com not permitted by network security policy
        at okhttp3.internal.connection.RealConnection.connect(RealConnection.java:146)

     */
            thread.start();
        }
        synchronized (lock) {
            lock.wait();

        }
    }
/// adb push hello.lua /sdcard/qssq666/robot_plugin_lua/hello.lua


    @Test
    public void testLuaCallBack() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Globals globals = LuaUtil.buildSelfGlobals();
        try {
            LuaValue luachuck = globals.load(new InputStreamReader(new FileInputStream(new File("/sdcard/qssq666/robot_plugin_lua/hello.lua"))), "chunkname").call();// 加载并编译
//                luachuck.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(appContext, "找不到文件Lua", Toast.LENGTH_SHORT).show();
            return;
        }
        // 在Globals获取全局函数hi,并传递参数调用
        // 在luaj中LuaValue映射了lua中的基本数据类型,包括函数
        // func.invoke(new LuaValue[] { LuaValue.valueOf(new Date().toString())
        // 没有参数传递给lua
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf("voidMethod"));
        voidFuncLuaValue.invoke(new LuaValue[]{});
        // 传递int给 lua
        LuaValue intFuncLuaValue = globals.get(LuaValue.valueOf("intMethod"));
        intFuncLuaValue.invoke(new LuaValue[]{LuaValue.valueOf(10)});
        // 传递字符串参数给lua
        LuaValue stringFuncLuaValue = globals.get(LuaValue.valueOf("stringMethod"));
        stringFuncLuaValue.invoke(new LuaValue[]{LuaValue.valueOf("hhhh")});
    }

    // 安卓测试类不能进行混淆。
    @Test
    public void testEncode() {
        StringBuffer sb = new StringBuffer();

        System.out.println(TEST + "result:" + sb.toString());
    }

    /**
     * 顺序流程执行
     */
    @Test
    public void execute() {
        Log.e("错误", "直接错误日志也认为test fail?");
        if (true) {
        }
        Context appContext = InstrumentationRegistry.getTargetContext();
        Globals globals = LuaUtil.buildSelfGlobals();

        String script = "/sdcard/qssq666/robot_plugin_lua/hello.lua";
        LuaValue chunk = globals.loadfile(script);
//        chunk.call()
        LuaValue call = chunk.call(LuaValue.valueOf(script));
        Log.w(TAG, "执行结果成功语法:" + call + ",具体流程查看System.out.prints");///System.out
    }
}
