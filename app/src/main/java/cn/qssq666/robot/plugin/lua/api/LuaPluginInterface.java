package cn.qssq666.robot.plugin.lua.api;
import cn.qssq666.CoreLibrary0;import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import cn.qssq666.robot.plugin.lua.util.LuaUtil;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginControlInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.RobotConfigInterface;

/**
 * Created by qssq on 2018/11/11 qssq666@foxmail.com
 */
public class LuaPluginInterface implements PluginInterface {

    private final Globals globals;
    private final File luaFile;

    public LuaPluginInterface(File luaFile) throws Exception {
        this.luaFile = luaFile;
        globals = LuaUtil.buildSelfGlobals();
        LuaValue luachuck = globals.load(new InputStreamReader(new FileInputStream(luaFile)), "chunkname").call();// 锟斤拷锟截诧拷锟斤拷锟斤拷

    }

    public int getPluginSDKVersion() {
        return LuaUtil.getIntMethodResult(globals, "getPluginSDKVersion");
    }

    public int getMinRobotSdk() {
        return LuaUtil.getIntMethodResult(globals, "getMinRobotSdk");
    }

    @Override
    public boolean onReceiveRobotFinalCallMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean aite,
                                                             boolean haisaiteme) {
        LuaValue funcValue = globals.get(LuaValue.valueOf("onReceiveRobotFinalCallMsgIsNeedIntercept"));
        if (funcValue.isfunction()) {
            return funcValue.invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(item),
                    LuaUtil.createLuaValueByObj(list), LuaValue.valueOf(aite), LuaValue.valueOf(haisaiteme)}).arg1()
                    .checkboolean();
        }

        return false;
    }

    public String getAuthorName() {
        return LuaUtil.getStringMethodResult(globals, "getAuthorName");
    }

    @Override
    public void onReceiveControlApi(PluginControlInterface instance) {
        LuaUtil.callJavaOneArgVoidMethod(globals, "onReceiveControlApi", instance);
    }

    public int getVersionCode() {
        return LuaUtil.getIntMethodResult(globals, "getVersionCode");
    }

    public String getBuildTime() {
        return LuaUtil.getStringMethodResult(globals, "getBuildTime");
    }

    public String getVersionName() {
        return LuaUtil.getStringMethodResult(globals, "getVersionName");
    }

    public String getPackageName() {
        return LuaUtil.getStringMethodResult(globals, "getPackageName");
    }

    public String getDescript() {
        return LuaUtil.getStringMethodResult(globals, "getDescript");
    }

    public String getPluginName() {
        String getPluginName = LuaUtil.getStringMethodResult(globals, "getPluginName");
        if (TextUtils.isEmpty(getPluginName)) {
            String name = luaFile.getName();
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
        return LuaUtil.callJavaOneArgBooleanMethod(globals, "onReceiveMsgIsNeedIntercept", item);
    }

    @Override
    public boolean onReceiveOtherIntercept(IMsgModel item, int type) {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf("onReceiveMsgIsNeedIntercept"));
        if (voidFuncLuaValue.isfunction()) {
            Varargs invoke = voidFuncLuaValue
                    .invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(item), LuaValue.valueOf(type)});
            return invoke.checkboolean(0);

        } else {
            return false;
        }
    }

    @Override
    public void onCreate(Context context) {

        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf("onCreate"));
        if (voidFuncLuaValue.isfunction()) {
            voidFuncLuaValue.invoke(LuaUtil.createLuaValueByObj(context));
            System.out.println("call onCreate");
        } else {
            System.err.println("call onCreate fail ,method not define");
        }
    }

    /*
     * public boolean onReceiveMsgIsNeedIntercept(Object item) { return
     * callJavaOneArgBooleanMethod(globals, "onReceiveMsgIsNeedIntercept",
     * item); }
     */

    public void onReceiveErrorMsg(String str) {

        LuaValue funcValue = globals.get(LuaValue.valueOf("onReceiveErrorMsg"));
        if (funcValue.isfunction()) {
            funcValue.invoke(LuaValue.valueOf(str));
        } else {
            LuaValue print = globals.get(LuaValue.valueOf("error"));
            print.invoke(LuaValue.valueOf(str));

        }
    }

    @Override
    public boolean onReceiveMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean hasAite,
                                               boolean hasAiteMe) {


//            item.getIstroop()
        LuaValue funcValue = globals.get(LuaValue.valueOf("onReceiveMsgIsNeedIntercept"));
        if (funcValue.isfunction()) {

            if (funcValue.isfunction()) {
                // if (funcValue.narg() == 3) {
                try {

                    Varargs invoke = funcValue.invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(item),
                            LuaUtil.createLuaValueByObj(list), LuaValue.valueOf(hasAite), LuaValue.valueOf(hasAiteMe)});

                    return invoke.arg1().checkboolean();// 锟斤拷锟斤拷锟绞撅拷锟斤拷锟街碉拷锟斤拷锟?
                } catch (Throwable e) {
                    onReceiveErrorMsg(Log.getStackTraceString(e));
                    return false;

                }
                // return invoke.checkboolean(0);//锟斤拷锟斤拷锟?
                // 锟斤拷锟斤拷锟斤拷鼙锟绞狙帮拷业锟斤拷硕锟斤拷俑锟斤拷锟斤拷锟?

                /*
                 * } else if (funcValue.narg() == 1) { return
                 * funcValue.invoke(new LuaValue[] {
                 * LuaUtil.createLuaValueByObj(item) }).checkboolean(0); } else
                 * { System.err.println(
                 * " call onReceiveMsgIsNeedIntercept fail ,arg count aspect 3 or 1"
                 * ); return false; }
                 */

            } else {
                return funcValue.invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(item)}).checkboolean(0);
            }

        } else {

            System.err.println(" call onReceiveMsgIsNeedIntercept fail ,method not define");
            return false;
        }
    }

    @Override
    public void onDestory() {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf("onDestory"));
        if (voidFuncLuaValue.isfunction()) {
            voidFuncLuaValue.invoke();
        } else {
            System.err.println(" call onDestory fail ,method not define");
        }
    }

    @Override
    public void onReceiveRobotConfig(RobotConfigInterface robotConfigInterface) {
        LuaUtil.callJavaOneArgVoidMethod(globals, "onReceiveRobotConfig", robotConfigInterface);
    }

    @Override
    public View onConfigUi(ViewGroup viewGroup) {

        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf("onConfigUi"));
        if (voidFuncLuaValue != null) {
            Varargs invoke = voidFuncLuaValue.invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(viewGroup)});
            return ((View) invoke.checkuserdata(0, View.class));
        }
        return null;
    }
}
