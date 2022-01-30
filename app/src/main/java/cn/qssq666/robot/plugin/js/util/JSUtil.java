package cn.qssq666.robot.plugin.js.util;
import cn.qssq666.CoreLibrary0;
import android.util.Pair;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.plugin.js.lib.BuildConfigLib;
import cn.qssq666.robot.plugin.js.lib.LogLib;

/**
 * Created by qssq on 2018/11/11 qssq666@foxmail.com
 */
public class JSUtil {

    private static final int FLAG_DEFINED_SUCC = 1;
    private static final int FLAG_UNDEFINE = 0;
    private static String TAG = "LuaUtil";

    public static HashMap<String, Object> getDefaultModules() {
        HashMap<String, Object> map = new HashMap();
        map.put("robot", new BuildConfigLib());
        map.put("log", new LogLib());
        map.put("api", RobotContentProvider.getInstance().getPluginControlInterface());
        map.put("config", RobotContentProvider.getInstance().getConfigQueryImpl());
        /*
         * ScriptableObject.putProperty(scope, "api",
         * JsUtil.createJsObjByObj(RobotContentProvider.getInstance().
         * getPluginControlInterface(), scope));
         * ScriptableObject.putProperty(scope, "log",
         * JsUtil.createJsObjByObj(new LogLib(), scope));
         */
        return map;
    }

    public static void initModule(Scriptable scriptable, HashMap<String, Object> map) {
        Set<Entry<String, Object>> entryset = map.entrySet();
        Iterator<Entry<String, Object>> iterator = entryset.iterator();
        while (iterator.hasNext()) {
            Entry<String, Object> temp = iterator.next();
            ScriptableObject.putProperty(scriptable, "" + temp.getKey(),
                    JSUtil.createJsObjByObj(temp.getValue(), scriptable));
        }
    }

    public static String getStringMethodResult(Context context, Scriptable scripttable, String methodName) {
        return getStringMethodResultPair(context, scripttable, methodName).second;
    }

    public static Pair<Integer, String> getStringMethodResultPair(Context context, Scriptable scripttable,
                                                                  String methodName) {

        Object fObj = scripttable.get(methodName, scripttable);
        if ((fObj instanceof Function)) {
            Function f = (Function) fObj;
            Object result = f.call(context, scripttable, scripttable, new Object[0]);
            if (result == null) {
                result = false;
            } else {
            }
            // System.out.println("返回结果：" + Context.toString(result));
            return Pair.create(FLAG_DEFINED_SUCC, result + "");

        } else {
            System.out.println("找不到方法" + methodName + " " + fObj);
        }

        return Pair.create(FLAG_UNDEFINE, null);
    }

    public static boolean getBooleanMethodResult(Context context, Scriptable scripttable, String methodName) {
        return getBooleanMethodResultPair(context, scripttable, methodName).second;

    }

    public static Pair<Integer, Boolean> getBooleanMethodResultPair(Context context, Scriptable scripttable,
                                                                    String methodName) {
        /*
         * LuaValue voidFuncLuaValue =
         * org.mozilla.javascript.Scriptable.get(LuaValue.valueOf(methodName));
         * if (voidFuncLuaValue.isfunction()) { return
         * Pair.create(FLAG_DEFINED_SUCC,
         * voidFuncLuaValue.call().arg1().checkboolean()); } else {
         * System.err.println("call " + methodName + " fail ,method not define"
         * ); } return Pair.create(FLAG_UNDEFINE, false);
         */

        Object fObj = scripttable.get(methodName, scripttable);
        if ((fObj instanceof Function)) {
            Function f = (Function) fObj;
            Object result = f.call(context, scripttable, scripttable, new Object[0]);
            if (result == null) {
                result = false;
            } else {
            }
            // System.out.println("返回结果：" + Context.toString(result));
            return Pair.create(FLAG_DEFINED_SUCC, (Boolean) result);

        } else {
            System.out.println("找不到方法" + methodName);
        }
        return Pair.create(FLAG_UNDEFINE, false);
    }

    public static int getIntMethodResult(Context context, Scriptable scripttable, String methodName) {

        return getIntMethodResultPair(context, scripttable, methodName).second;
    }

    public static Pair<Integer, Integer> getIntMethodResultPair(Context context, Scriptable scripttable,
                                                                String methodName) {

        Object fObj = scripttable.get(methodName, scripttable);
        if ((fObj instanceof Function)) {
            Function f = (Function) fObj;
            Object result = f.call(context, scripttable, scripttable, new Object[0]);
            if (result == null) {
                result = false;
            } else {
            }
            // System.out.println("返回结果：" + result);
            if (result instanceof Double) {

            }
            return Pair.create(FLAG_DEFINED_SUCC, new Integer(Context.toString(result)));

        } else {
            System.out.println("找不到方法" + methodName);
        }
        return Pair.create(FLAG_UNDEFINE, -1);
    }

    public static boolean callJavaOneArgBooleanMethod(Context context, Scriptable scripttable, String methodName,
                                                      Object arg) {
        return callJavaOneArgBooleanMethodPair(context, scripttable, methodName, arg).second;
    }

    public static Pair<Integer, Boolean> callJavaOneArgBooleanMethodPair(Context context, Scriptable scripttable,
                                                                         String methodName, Object arg) {
        /*
         * LuaValue voidFuncLuaValue =
         * org.mozilla.javascript.Scriptable.get(LuaValue.valueOf(methodName));
         * if (voidFuncLuaValue.isfunction()) { Varargs invoke =
         * voidFuncLuaValue.invoke(createJsObjByObj(arg)); return
         * Pair.create(FLAG_DEFINED_SUCC, invoke.checkboolean(0)); } else {
         * System.err.println("call " + methodName + " fail ,method not define"
         * );
         *
         * return Pair.create(FLAG_UNDEFINE, false); }
         */

        Object fObj = scripttable.get(methodName, scripttable);
        if ((fObj instanceof Function)) {
            Object functionArgs[] = {arg};
            Function f = (Function) fObj;
            Object result = f.call(context, scripttable, scripttable, functionArgs);
            if (result == null) {
                result = false;
            } else {
            }
            // System.out.println("返回结果：" + Context.toString(result));
            return Pair.create(FLAG_DEFINED_SUCC, (boolean) result);

        } else {
            System.out.println("找不到方法" + methodName);
        }
        return Pair.create(FLAG_UNDEFINE, false);

    }

    public static void callJavaOneArgVoidMethod(Context context, Scriptable scripttable,
                                                String methodName, Object arg) {
        callJavaOneArgVoidMethodPair(context, scripttable, methodName, arg);
    }

    public static String formatCode(String code) throws IOException {
        InputStream beautify = AppContext.getInstance().getResources().getAssets().open("beautify.js");
        Context _cx = createJSContext();
        ScriptableObject scope = _cx.initStandardObjects();
        InputStreamReader in = new InputStreamReader(beautify);
//        BufferedReader bufferedReader = new BufferedReader(in);
        _cx.evaluateReader(scope, in, "robotformat", 1, null);
        Object o = scope.get("js_beautify", scope);
        return Context.toString(((Function) o).call(_cx, scope, scope, new Object[]{code}));
        // BuildConfig.this
       /* engine = new ScriptEngineManager().getEngineByName("nashorn");

        // this is needed to make self invoking function modules work
        // otherwise you won't be able to invoke your function
        engine.eval("var global = this;");
        engine.eval(new InputStreamReader(getClass().getResourceAsStream(BEAUTIFY_JS_RESOURCE)));*/
    }

    public static Pair<Integer, Integer> callJavaOneArgVoidMethodPair(Context context, Scriptable scripttable,
                                                                      String methodName, Object arg) {

        // Object fObj = scripttable.get(jsFunction, scripttable);
        // JSPluginInterface jsPluginInterface = new JSPluginInterface(new
        // File("a.js"));
        // ScriptableObject.putProperty(scripttable, "api",
        // Context.javaToJS(jsPluginInterface, scripttable));

        Object fObj = scripttable.get(methodName, scripttable);
        if ((fObj instanceof Function)) {
            Object functionArgs[] = {arg};
            Function f = (Function) fObj;
            f.call(context, scripttable, scripttable, functionArgs);
            return Pair.create(FLAG_DEFINED_SUCC, FLAG_DEFINED_SUCC);

        } else {
            System.out.println("找不到方法" + methodName);
        }
        return Pair.create(FLAG_UNDEFINE, FLAG_UNDEFINE);
    }

    public static Object createJsObjByObj(Object item, Scriptable scripttable) {

        return Context.javaToJS(item, scripttable);
        // return CoerceJavaToLua.coerce(item);
    }

    public static Pair<Context, ScriptableObject> launchJS(String sourceOrFile, boolean isFile, final INotify<String> iCallBack)
            throws FileNotFoundException, IOException {
        HashMap<String, Object> map = new HashMap();
        map.put("robot", new BuildConfigLib());
        map.put("log", new LogLib() {
            @Override
            public void callback(String tag, String msg, Throwable... e) {
                String error = e == null || e.length == 0 ? "" : "_stackinfo:" + e[0].getMessage();
                iCallBack.onNotify("[" + tag + "]:" + msg + error);
            }
        });
        map.put("api", RobotContentProvider.getInstance().getPluginControlInterface());
        map.put("config", RobotContentProvider.getInstance().getConfigQueryImpl());

        Context _cx = createJSContext();
        ScriptableObject scope = _cx.initStandardObjects();
        // BuildConfig.this
        JSUtil.initModule(scope, map);
        if (isFile) {
            File file = new File(sourceOrFile);
            _cx.evaluateReader(scope, new java.io.FileReader(file), "<" + file.getName() + ">", 1, null);

        } else {

            _cx.evaluateString(scope, sourceOrFile, "fakecode", 1, null);
        }
        return Pair.create(_cx, scope);

        // Object result = cx.evaluateString(scope, str, null, 1, null);

        /*
         * ScriptEngineManager sem = new ScriptEngineManager(); ScriptEngine
         * engine = sem.getEngineByName("javascript");
         *
         * //执行javascript文件a.js URL url =
         * Demo01.class.getClassLoader().getResource("a.js"); FileReader reader;
         * try { reader = new FileReader(url.getPath()); engine.eval(reader);
         * reader.close(); } catch (Exception e) { e.printStackTrace(); }
         */

    }

    public static Context createJSContext() {
        Context context = Context.enter();
        context.setOptimizationLevel(-1);
        context.setLanguageVersion(Context.VERSION_ES6);
        context.setLocale(Locale.getDefault());
        return context;
    }
}
