package cn.qssq666.robot.plugin.js.ui;
import cn.qssq666.CoreLibrary0;import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.util.Pair;

import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.List;

import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.interfaces.IResultNotify;
import cn.qssq666.robot.plugin.js.cns.JSCns;
import cn.qssq666.robot.plugin.js.util.JSUtil;
import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.utils.DialogUtils;
import cn.qssq666.robot.utils.QssqTaskFix;
import cn.qssq666.robot.utils.TestUtil;

/**
 * Created by qssq on 2018/12/23 qssq666@foxmail.com
 */
public class JSGUIUtil {
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

                    JSUtil.launchJS(fileOrContent, isFile, new INotify<String>() {
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
                    Log.w(JSCns.LOG_TAG, "错误:" + Log.getStackTraceString(e));
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

                ScriptableObject globals = null;
                org.mozilla.javascript.Context context = null;
                try {

                    Pair<org.mozilla.javascript.Context, ScriptableObject> pair = JSUtil.launchJS(fileOrContent, isFile, new INotify<String>() {
                        @Override
                        public void onNotify(String param) {
                            sb.append(TestUtil.blueWrapFont(param, "<br>"));
                        }
                    });
                    globals = pair.second;
                    context = pair.first;
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

//(msgitem, aiteList, hasAite, hasAiteMe)
                final ScriptableObject finalGlobals = globals;
                final org.mozilla.javascript.Context finalContext = context;
                JSGUIUtil.eachCallTest("下发消息通知", "onReceiveMsgIsNeedIntercept", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {

                        MsgItem testItem = TestUtil.createTestItem();
                        List<AtBeanModelI> aiteItem = TestUtil.createAiteItem();
                        Object result = luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{testItem, aiteItem, true, false});
                        return result;
                    }
                });

                JSGUIUtil.eachCallTest("拦截回调", "onReceiveRobotFinalCallMsgIsNeedIntercept", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {

                        MsgItem testItem = TestUtil.createTestItem();
                        List<AtBeanModelI> aiteItem = TestUtil.createAiteItem();
                        Object result = luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{testItem, aiteItem, true, false});
                        return result;

                        /*MsgItem testItem = TestUtil.createTestItem();
                        List<AtBeanModelI> aiteItem = TestUtil.createAiteItem();
                        boolean result = luaValue.invoke(new Function[]{JSGUIUtil.createFunctionByObj(testItem),
                                JSGUIUtil.createFunctionByObj(aiteItem), Function.valueOf(true), Function.valueOf(true)}).arg1()
                                .checkboolean();
                        return result;*/
                    }
                });


                JSGUIUtil.eachCallTest("插件创建通知", "onCreate", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        Object result = luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{RobotContentProvider.getInstance()});
                        return "成功";
                    }
                });

                JSGUIUtil.eachCallTest("插件销毁通知", "onDestory", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        Object result = luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{});
                        return "成功";
                    }
                });
                JSGUIUtil.eachCallTest("获取插件名", "getPluginName", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{});
                    }
                });
                JSGUIUtil.eachCallTest("获取插件作者", "getAuthorName", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{});
                    }
                });

                JSGUIUtil.eachCallTest("获取插件描述", "getDescript", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{});
                    }
                });

                JSGUIUtil.eachCallTest("获取插件包名", "getPackageName", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return org.mozilla.javascript.Context.toString(luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{}));
                    }
                });
                JSGUIUtil.eachCallTest("获取插件版本", "getVersionName", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return org.mozilla.javascript.Context.toString(luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{}));
                    }
                });
                JSGUIUtil.eachCallTest("获取插件版本号", "getVersionCode", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return org.mozilla.javascript.Context.toString(luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{}));
                    }
                });

                JSGUIUtil.eachCallTest("获取插件编译时间", "getBuildTime", globals, sb, new IResultNotify<Function, Object>() {
                    @Override
                    public Object onNotify(Function luaValue) {
                        return org.mozilla.javascript.Context.toString(luaValue.call(finalContext, finalGlobals, finalGlobals, new Object[]{}));
                    }
                });

                try {
                    Thread.sleep(150);
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


    private static String wrapMethodName(String zhName, String testFunctionName) {
        return zhName + "(" + testFunctionName + ")方法";
    }

    /*
    	private Scriptable scope;
	private org.mozilla.javascript.Context _cx;

     */
    public static void eachCallTest(String zhName, String testFunctionName, Scriptable globals, StringBuffer sb, IResultNotify<Function, Object> iNotify) {


        try {
            Object funcValue = globals.get(testFunctionName, globals);

            if (funcValue instanceof Function) {

                sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.greenWrapFont("通过:" + iNotify.onNotify((Function) funcValue), "<br>"));

            } else {

                if (testFunctionName.equals("onReceiveMsgIsNeedIntercept")) {
                    sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.redWrapFont("未定义(必须定义):" + (funcValue == null ? "null" : funcValue.getClass().getName()), "<br>"));

                } else {

                    sb.append(wrapMethodName(zhName, testFunctionName) + "-><br>" + TestUtil.warnYellowWrapFont("未定义:" + funcValue, "<br>"));
                }

            }
        } catch (Exception e) {
            String s = Log.getStackTraceString(e);

            Log.w(JSCns.LOG_TAG, "测试" + wrapMethodName(zhName, testFunctionName) + "失败" + s);
            String error = "<b>" + TestUtil.redWrapFont(wrapMethodName(zhName, testFunctionName), "") + "</>" + "-><br>" + "语法错误:" + TestUtil.redWrapFont(s, "<br>");
            sb.append(error);

        }

    }
}
