package cn.qssq666.robot.plugin.lua.util;
import cn.qssq666.CoreLibrary0;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.util.Pair;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.CoroutineLib;
import org.luaj.vm2.lib.MathLib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseIoLib;
import org.luaj.vm2.lib.jse.JseMathLib;
import org.luaj.vm2.lib.jse.JseOsLib;
import org.luaj.vm2.lib.jse.LuajavaLib;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.interfaces.IResultNotify;
import cn.qssq666.robot.plugin.lua.engine.LuajavaLibEx;
import cn.qssq666.robot.plugin.lua.lib.LUAEngineLib;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.QssqTaskFix;
import cn.qssq666.robot.utils.TestUtil;

/**
 * Created by qssq on 2018/11/11 qssq666@foxmail.com
 */
public class LuaUtil {


    private static final int FLAG_DEFINED_SUCC = 1;
    private static final int FLAG_UNDEFINE = 0;
    private static String TAG = "LuaUtil";

    public static Globals buildSelfGlobals() {

        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
//        globals.load(new IoLib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new MathLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new LuajavaLibEx());
        globals.load(new LUAEngineLib());
        LoadState.install(globals);
        LuaC.install(globals);

        return globals;
    }

    public static LuaValue createLuaValueByObj(Object item) {
        return CoerceJavaToLua.coerce(item);
    }

    public static void luanchLua(String str, boolean isFile, final INotify<String> iCallBack) {

        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
//        globals.load(new IoLib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new MathLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new LuajavaLib());
        LUAEngineLib library = new LUAEngineLib();
        library.setLogCallBack(new LUAEngineLib.LogCallBack() {
            @Override
            public void onPrint(int type, final String funcName, final String message) {
                if (iCallBack != null) {
                    RobotContentProvider.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            iCallBack.onNotify("[" + funcName + "]:" + message);
                        }
                    });
                }
            }
        });
        globals.load(library);
        LoadState.install(globals);
        LuaC.install(globals);

        // Use the convenience function on Globals to load a chunk.
        // 通过Globals加载lua脚本
//        String script = "hello.lua";

        // Use any of the "call()" or "invoke()" functions directly on the
        // chunk.
        // 运行lua脚本

        // 加载自定义库
        // 下面两个方法可以从 JsePlatform.standardGlobals()分析出原因。

        if (isFile) {
            LuaValue chunk = globals.loadfile(str);
            LuaValue result = chunk.call(LuaValue.valueOf(str));

        } else {
            LuaValue chunk = globals.load(str);
            LuaValue result = chunk.call(LuaValue.valueOf(str));

        }
        System.out.println("执行完毕");
    }

    public static Globals luanchLuaMethodFirstReturnGlobals(String str, boolean isFile, final INotify<String> iCallBack) throws FileNotFoundException, UnsupportedEncodingException {

        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
//        globals.load(new IoLib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new CoroutineLib());
        globals.load(new MathLib());
        globals.load(new JseMathLib());
        globals.load(new JseIoLib());
        globals.load(new JseOsLib());
        globals.load(new LuajavaLib());
        LUAEngineLib library = new LUAEngineLib();
        library.setLogCallBack(new LUAEngineLib.LogCallBack() {
            @Override
            public void onPrint(int type, final String funcName, final String message) {
                if (iCallBack != null) {
                    RobotContentProvider.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            iCallBack.onNotify("[" + funcName + "]:" + message);
                        }
                    });
                }
            }
        });
        globals.load(library);
        LoadState.install(globals);
        LuaC.install(globals);


        // Use the convenience function on Globals to load a chunk.
        // 通过Globals加载lua脚本
//        String script = "hello.lua";

        // Use any of the "call()" or "invoke()" functions directly on the
        // chunk.
        // 运行lua脚本

        // 加载自定义库
        // 下面两个方法可以从 JsePlatform.standardGlobals()分析出原因。

        if (isFile) {
//                   LuaValue luachuck = globals.load(new InputStreamReader(new FileInputStream(luaFile)), "chunkname").call();// 锟斤拷锟截诧拷锟斤拷锟斤拷

            globals.load(new InputStreamReader(new FileInputStream(str)), "chunkname").call();

        } else {
            globals.load(str, "chunkname").call();
//            globals.load(new InputStreamReader(new ByteArrayInputStream(str.getBytes())), "chunkname").call();

        }
        return globals;
    }


    public static void runReceiveMsgByGUI(final String fileOrContent, final boolean isFile, final Activity activity) {

        runReceiveMsgByGUI(fileOrContent, isFile, activity, null);
    }

    public static void runReceiveMsgByGUI(final String fileOrContent, final boolean isFile, final Activity activity, final INotify<Boolean> iNotify) {
        final StringBuffer sb = new StringBuffer();
        final ProgressDialog progressDialog = DialogUtils.getProgressDialog(activity);
        progressDialog.setMessage("请稍后..");
        progressDialog.show();
        new QssqTaskFix<Object, Object>(new QssqTaskFix.ICallBackImp<Object, Object>() {
            @Override
            public Object onRunBackgroundThread(Object... params) {

                Globals globals = null;
                try {

                    globals = LuaUtil.luanchLuaMethodFirstReturnGlobals(fileOrContent, isFile, new INotify<String>() {
                        @Override
                        public void onNotify(String param) {
                            sb.append(TestUtil.blueWrapFont(param, "<br>"));
                        }
                    });

                    int count = 0;
                    while (count < 5) {
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        count++;
                    }

                } catch (Exception e) {
                    return e;
                }

                final Globals finalGlobals = globals;

                LuaUtil.eachCallTest("下发消息通知", "onReceiveMsgIsNeedIntercept", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {

                        MsgItem testItem = TestUtil.createTestItem();
                        List<AtBeanModelI> aiteItem = TestUtil.createAiteItem();
                        boolean result = luaValue.invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(testItem),
                                LuaUtil.createLuaValueByObj(aiteItem), LuaValue.valueOf(true), LuaValue.valueOf(true)}).arg1()
                                .checkboolean();
                        return result;
                    }
                });

                LuaUtil.eachCallTest("拦截回调", "onReceiveRobotFinalCallMsgIsNeedIntercept", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {

                        MsgItem testItem = TestUtil.createTestItem();
                        List<AtBeanModelI> aiteItem = TestUtil.createAiteItem();
                        boolean result = luaValue.invoke(new LuaValue[]{LuaUtil.createLuaValueByObj(testItem),
                                LuaUtil.createLuaValueByObj(aiteItem), LuaValue.valueOf(true), LuaValue.valueOf(true)}).arg1()
                                .checkboolean();
                        return result;
                    }
                });


                LuaUtil.eachCallTest("插件创建通知", "onCreate", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        luaValue.invoke();
                        return "成功";
                    }
                });

                LuaUtil.eachCallTest("插件销毁通知", "onDestory", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        luaValue.invoke();
                        return "成功";
                    }
                });
                LuaUtil.eachCallTest("获取插件名", "getPluginName", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkjstring();
                    }
                });
                LuaUtil.eachCallTest("获取插件作者", "getAuthorName", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkjstring();
                    }
                });

                LuaUtil.eachCallTest("获取插件描述", "getDescript", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkjstring();
                    }
                });

                LuaUtil.eachCallTest("获取插件包名", "getPackageName", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkjstring();
                    }
                });
                LuaUtil.eachCallTest("获取插件版本", "getVersionName", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkjstring();
                    }
                });
                LuaUtil.eachCallTest("获取插件版本号", "getVersionCode", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkint();
                    }
                });

                LuaUtil.eachCallTest("获取插件编译时间", "getBuildTime", globals, sb, new IResultNotify<LuaValue, Object>() {
                    @Override
                    public Object onNotify(LuaValue luaValue) {
                        return luaValue.call().arg1().checkjstring();
                    }
                });

                try {
                    Thread.sleep(130);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return sb.toString();

            }

            @Override
            public void onRunFinish(Object s) {
                progressDialog.dismiss();

                if (s != null && s instanceof String) {
                    String str = (String) s;
                    AlertDialog alertDialog = DialogUtils.showCustromHtmlDialog(activity, "<html>" + str + "</html>", "模拟->检测插件语法", null);
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (iNotify != null) {
                                iNotify.onNotify(true);

                            }
                        }
                    });

                } else {
                    String msg = null;
                    if (s instanceof Exception) {
                        msg = Log.getStackTraceString((Throwable) s);
                    } else {
                        msg = "未知";
                    }
                    AlertDialog alertDialog = DialogUtils.showDialog(activity, "执行失败!" + (s));
                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (iNotify != null) {
                                iNotify.onNotify(false);

                            }
                        }
                    });
                }
            }
        }).execute();
    }


    public static void eachCallTest(String zhName, String testFunctionName, Globals globals, StringBuffer sb, IResultNotify<LuaValue, Object> iNotify) {


        try {
            LuaValue funcValue = globals.get(LuaValue.valueOf(testFunctionName));

            if (funcValue.isfunction()) {

                sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.greenWrapFont("通过:" + iNotify.onNotify(funcValue), "<br>"));

            } else if (funcValue.isnil()) {

                if (testFunctionName.equals("onReceiveMsgIsNeedIntercept")) {
                    sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.redWrapFont("未定义(必须定义):" + funcValue.typename(), "<br>"));

                } else {

                    sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.warnYellowWrapFont("未定义:" + funcValue.typename(), "<br>"));
                }

            } else {

                sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.redWrapFont("参数错误:" + funcValue.typename(), "<br>"));
            }

        } catch (Exception e) {
            String s = Log.getStackTraceString(e);

            Log.w(TAG, "测试" + wrapMethodName(zhName, testFunctionName) + "失败" + s);
            String error = "<b>" + TestUtil.redWrapFont(wrapMethodName(zhName, testFunctionName), "") + "</>" + "-><br>" + "语法错误:" + TestUtil.redWrapFont(s, "<br>");
            sb.append(error);

        }

    }

    private static String wrapMethodName(String zhName, String testFunctionName) {
        return zhName + "(" + testFunctionName + ")方法";
    }

    public static String getStringMethodResult(Globals globals, String methodName) {
        return getStringMethodResultPair(globals, methodName).second;
    }

    public static Pair<Integer, String> getStringMethodResultPair(Globals globals, String methodName) {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf(methodName));

        if (voidFuncLuaValue.isfunction()) {
            return Pair.create(FLAG_DEFINED_SUCC, voidFuncLuaValue.call().arg1().checkjstring());
        } else {
            System.err.println("call " + methodName + " fail ,method not define");
        }
        return Pair.create(FLAG_UNDEFINE, null);
    }

    public static boolean getBooleanMethodResult(Globals globals, String methodName) {
        return getBooleanMethodResultPair(globals, methodName).second;

    }

    public static Pair<Integer, Boolean> getBooleanMethodResultPair(Globals globals, String methodName) {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf(methodName));
        if (voidFuncLuaValue.isfunction()) {
            return Pair.create(FLAG_DEFINED_SUCC, voidFuncLuaValue.call().arg1().checkboolean());
        } else {
            System.err.println("call " + methodName + " fail ,method not define");
        }
        return Pair.create(FLAG_UNDEFINE, false);
    }

    public static int getIntMethodResult(Globals globals, String methodName) {

        return getIntMethodResultPair(globals, methodName).second;
    }

    public static Pair<Integer, Integer> getIntMethodResultPair(Globals globals, String methodName) {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf(methodName));
        if (voidFuncLuaValue.isfunction()) {
            return Pair.create(FLAG_DEFINED_SUCC, voidFuncLuaValue.call().arg1().checkint());
        } else {
            System.err.println("call " + methodName + " fail ,method not define");
            return Pair.create(FLAG_UNDEFINE, 0);
        }
    }

    public static boolean callJavaOneArgBooleanMethod(Globals globals, String methodName, Object arg) {
        return callJavaOneArgBooleanMethodPair(globals, methodName, arg).second;
    }

    public static Pair<Integer, Boolean> callJavaOneArgBooleanMethodPair(Globals globals, String methodName, Object arg) {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf(methodName));
        if (voidFuncLuaValue.isfunction()) {
            Varargs invoke = voidFuncLuaValue.invoke(createLuaValueByObj(arg));
            return Pair.create(FLAG_DEFINED_SUCC, invoke.checkboolean(0));
        } else {
            System.err.println("call " + methodName + " fail ,method not define");

            return Pair.create(FLAG_UNDEFINE, false);
        }
    }

    public static void callJavaOneArgVoidMethod(Globals globals, String methodName, Object arg) {

        callJavaOneArgVoidMethodPair(globals, methodName, arg);
    }

    public static Pair<Integer, Integer> callJavaOneArgVoidMethodPair(Globals globals, String methodName, Object arg) {
        LuaValue voidFuncLuaValue = globals.get(LuaValue.valueOf(methodName));
        if (voidFuncLuaValue.isfunction()) {
            Varargs invoke = voidFuncLuaValue.invoke(createLuaValueByObj(arg));
            return Pair.create(FLAG_DEFINED_SUCC, FLAG_DEFINED_SUCC);
        } else {
            System.err.println("call " + methodName + " fail ,method not define");
        }
        return Pair.create(FLAG_UNDEFINE, FLAG_UNDEFINE);
    }

    public static void runByGUI(final String fileOrContent, final boolean isFile, final Activity activity) {
        runByGUI(fileOrContent, isFile, activity, null);
    }

    public static void runByGUI(final String fileOrContent, final boolean isFile, final Activity activity, final INotify<Boolean> iNotify) {
        final ProgressDialog progressDialog = DialogUtils.getProgressDialog(activity);
        progressDialog.setMessage("请稍后..");
        progressDialog.show();
        new QssqTaskFix<Object, Object>(new QssqTaskFix.ICallBackImp<Object, Object>() {
            @Override
            public Object onRunBackgroundThread(Object... params) {

                final StringBuffer sb = new StringBuffer();

                try {

                    LuaUtil.luanchLua(fileOrContent, isFile, new INotify<String>() {
                        @Override
                        public void onNotify(String param) {
                            sb.append(param + "\n");
                        }
                    });

                    int count = 0;
                    while (count < 8) {
                        Thread.sleep(100);
                        count++;
                    }

                    return sb.toString();
                } catch (Exception e) {
                    Log.w(TAG, "错误:" + Log.getStackTraceString(e));
                    return e;
                }

            }

            @Override
            public void onRunFinish(Object s) {
                progressDialog.dismiss();

                if (s != null && s instanceof String) {
                    DialogUtils.showDialog(activity, "" + ((String) s).toLowerCase(), "执行结果", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (iNotify != null) {
                                iNotify.onNotify(true);
                            }
                        }
                    });

                } else {
                    String msg = null;
                    if (s instanceof Exception) {
                        msg = Log.getStackTraceString((Throwable) s);
                    } else {
                        msg = "未知";
                    }
                    DialogUtils.showDialog(activity, "执行失败!" + (s), "错误", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (iNotify != null) {
                                iNotify.onNotify(false);
                            }
                        }
                    });
                }
            }
        }).execute();
    }


}
