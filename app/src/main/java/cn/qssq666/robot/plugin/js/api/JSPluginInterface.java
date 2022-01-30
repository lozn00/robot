package cn.qssq666.robot.plugin.js.api;
import cn.qssq666.CoreLibrary0;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.qssq666.robot.plugin.js.cns.JSCns;
import cn.qssq666.robot.plugin.js.util.JSUtil;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginControlInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.RobotConfigInterface;

/**
 * Created by qssq on 2018/11/11 qssq666@foxmail.com
 */
public class JSPluginInterface implements PluginInterface {

    private final File file;
    private final String _InitThread;
    //    private Scriptable getWrapScope();

    final ThreadLocal<Scriptable> sScope = new ThreadLocal<Scriptable>();
    final ThreadLocal<org.mozilla.javascript.Context> sContext = new ThreadLocal<org.mozilla.javascript.Context>();

    public void createThreadLocalObj() {
        org.mozilla.javascript.Context context = JSUtil.createJSContext();
        Scriptable scriptable = context.initStandardObjects();
        sContext.set(context);
        sScope.set(scriptable);
    }

    public JSPluginInterface(File file) throws Exception, Error {
        _InitThread = Thread.currentThread().getName();

        this.file = file;
        getWrapContextInner();

        // BuildConfig.this


//
// getWrapContext().getErrorReporter().g

    }

    protected void initEnvModule() throws IOException {
        JSUtil.initModule(getWrapScope(), JSUtil.getDefaultModules());
        getWrapContext().evaluateReader(getWrapScope(), new java.io.FileReader(file), "<" + file.getName() + ">", 1, null);
    }

    public int getPluginSDKVersion() {
        return JSUtil.getIntMethodResult(getWrapContext(), getWrapScope(), "getPluginSDKVersion");
    }

    public int getMinRobotSdk() {
        return JSUtil.getIntMethodResult(getWrapContext(), getWrapScope(), "getMinRobotSdk");
    }

    public Scriptable getWrapScope() {
        try {
            return getWrapScopeInner();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public org.mozilla.javascript.Context getWrapContext() {
        try {
            return getWrapContextInner();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Scriptable getWrapScopeInner() throws IOException {
        if (sScope.get() == null) {
            try {
                createThreadLocalObj();
                initEnvModule();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sScope.get();
    }

    private org.mozilla.javascript.Context getWrapContextInner() throws IOException {
        if (sContext.get() == null) {
            createThreadLocalObj();
            initEnvModule();
        }
        return sContext.get();
    }

    @Override
    public boolean onReceiveRobotFinalCallMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean aite,
                                                             boolean haisaiteme) {
        Object obj = getWrapScope().get("onReceiveRobotFinalCallMsgIsNeedIntercept", getWrapScope());
        if (obj instanceof Function) {
            boolean result = (boolean) ((Function) obj).call(getWrapContext(), getWrapScope(), getWrapScope(),
                    new Object[]{item, list, aite, haisaiteme});
            return result;
        } else {

            return false;
        }
    }

    public String getAuthorName() {
        return JSUtil.getStringMethodResult(getWrapContext(), getWrapScope(), "getAuthorName");
    }

    @Override
    public void onReceiveControlApi(PluginControlInterface instance) {
        JSUtil.callJavaOneArgVoidMethod(getWrapContext(), getWrapScope(), "onReceiveControlApi", instance);
    }

    public int getVersionCode() {
        return JSUtil.getIntMethodResult(getWrapContext(), getWrapScope(), "getVersionCode");
    }

    public String getBuildTime() {
        return JSUtil.getStringMethodResult(getWrapContext(), getWrapScope(), "getBuildTime");
    }

    public String getVersionName() {
        return JSUtil.getStringMethodResult(getWrapContext(), getWrapScope(), "getVersionName");
    }

    public String getPackageName() {
        return JSUtil.getStringMethodResult(getWrapContext(), getWrapScope(), "getPackageName");
    }

    public String getDescript() {
        return JSUtil.getStringMethodResult(getWrapContext(), getWrapScope(), "getDescript");
    }

    public String getPluginName() {
        String getPluginName = JSUtil.getStringMethodResult(getWrapContext(), getWrapScope(), "getPluginName");
        if (TextUtils.isEmpty(getPluginName)) {
            String name = file.getName();
            return name;
        }
        return getPluginName;
    }

    public boolean isDisable() {

        return false;
    }

    @Override
    public void setDisable(boolean disable) {

    }

    @Override
    public boolean onReceiveMsgIsNeedIntercept(IMsgModel item) {
        return JSUtil.callJavaOneArgBooleanMethod(getWrapContext(), getWrapScope(), "onReceiveMsgIsNeedIntercept", item);
    }

    @Override
    public boolean onReceiveOtherIntercept(IMsgModel item, int type) {

        try {

            Object pfunction = getWrapScope().get("onReceiveMsgIsNeedIntercept", getWrapScope());
            if (pfunction instanceof Function) {
                Function func = (Function) pfunction;
                Log.w(JSCns.LOG_TAG, "call onReceiveOtherIntercept");
                return (boolean) func.call(getWrapContext(), getWrapScope(), getWrapScope(), new Object[]{item, type});
            }
            Log.w(JSCns.LOG_TAG, "call onReceiveOtherIntercept fail :" + pfunction);
        } catch (Throwable e) {
            Log.e(JSCns.LOG_TAG, "call onReceiveOtherIntercept fail :", e);
        }
        return false;
    }

    @Override
    public void onCreate(Context context) {

        /*
         * LuaValue voidFuncLuaValue = getWrapScope().get(LuaValue.valueOf("onCreate"));
         * if (voidFuncLuaValue.isfunction()) {
         * voidFuncLuaValue.invoke(JSUtil.createJsObjByObj(context));
         * Log.w(JSCns.LOG_TAG,"call onCreate"); } else { Log.d(JSCns.LOG_TAG,
         * "call onCreate fail ,method not define"); }
         */
        Object pfunction = getWrapScope().get("onCreate", getWrapScope());

        if (pfunction instanceof Function) {
            Function func = (Function) pfunction;

            Log.d(JSCns.LOG_TAG, "call onCreate succ");
            func.call(getWrapContext(), getWrapScope(), getWrapScope(), new Object[]{context});

        } else {
            Log.d(JSCns.LOG_TAG, "call onCreate  ,method not define " + pfunction);
        }
    }
    /*
     * public boolean onReceiveMsgIsNeedIntercept(Object item) { return
     * callJavaOneArgBooleanMethod(getWrapScope(), "onReceiveMsgIsNeedIntercept", item);
     * }
     */

    public void onReceiveErrorMsg(String str) {

        /*
         * LuaValue funcValue =
         * getWrapScope().get(LuaValue.valueOf("onReceiveErrorMsg")); if
         * (funcValue.isfunction()) { funcValue.invoke(LuaValue.valueOf(str)); }
         * else { LuaValue print = getWrapScope().get(LuaValue.valueOf("error"));
         * print.invoke(LuaValue.valueOf(str));
         *
         * }
         */

        Object pfunction = getWrapScope().get("onReceiveErrorMsg", getWrapScope());
        if (pfunction instanceof Function) {
            Function func = (Function) pfunction;
            Log.w(JSCns.LOG_TAG, "call onReceiveErrorMsg");
            func.call(getWrapContext(), getWrapScope(), getWrapScope(), new Object[]{});

        } else {
            // TODO

        }

    }

    @Override
    public boolean onReceiveMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean hasAite,
                                               boolean hasAiteMe) {

        Object pfunction = getWrapScope().get("onReceiveMsgIsNeedIntercept", getWrapScope());
        if (pfunction instanceof Function) {
            Function func = (Function) pfunction;
            Log.w(JSCns.LOG_TAG, "call onReceiveMsgIsNeedIntercept ");
            boolean result = (boolean) func.call(getWrapContext(), getWrapScope(), getWrapScope(),
                    new Object[]{item, list, hasAite, hasAiteMe});
            return result;

        } else {
            Log.d(JSCns.LOG_TAG, " call onReceiveMsgIsNeedIntercept fail ,method not define " + pfunction);
            return false;
        }

    }

    @Override
    public void onDestory() {
        Object pfunction = getWrapScope().get("onDestory", getWrapScope());
        if (pfunction instanceof Function) {
            Function func = (Function) pfunction;
            Log.d(JSCns.LOG_TAG, "call onDestory succ ");
            func.call(getWrapContext(), getWrapScope(), getWrapScope(), new Object[]{});

        } else {
            Log.d(JSCns.LOG_TAG, " call onDestory fail ,method not define " + pfunction);
        }
        getWrapContext().exit();
//		getWrapContext().
//		getWrapScope().
//		getWrapContext().clo

    }

    @Override
    public void onReceiveRobotConfig(RobotConfigInterface robotConfigInterface) {
        JSUtil.callJavaOneArgVoidMethod(getWrapContext(), getWrapScope(), "onReceiveRobotConfig", robotConfigInterface);
    }

    @Override
    public View onConfigUi(ViewGroup viewGroup) {

        Object pfunction = getWrapScope().get("onConfigUi", getWrapScope());
        if (pfunction instanceof Function) {
            Function func = (Function) pfunction;
            Log.w(JSCns.LOG_TAG, "call onConfigUi ");
            return (View) func.call(getWrapContext(), getWrapScope(), getWrapScope(), new Object[]{viewGroup});

        } else {
        }

        return null;
    }

}
