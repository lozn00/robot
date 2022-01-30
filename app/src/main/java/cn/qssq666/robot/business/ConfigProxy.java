package cn.qssq666.robot.business;
import cn.qssq666.CoreLibrary0;import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by qssq on 2018/1/18 qssq666@foxmail.com
 */

public class ConfigProxy implements InvocationHandler {
    private static final String TAG = "ConfigProxy";

    public ConfigProxy(Object object) {
        this.object = object;
    }

    Object object;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.w(TAG,"method:"+method.getName()+",values:"+ Arrays.toString(args));
        return method.invoke(object, args);
    }
}
