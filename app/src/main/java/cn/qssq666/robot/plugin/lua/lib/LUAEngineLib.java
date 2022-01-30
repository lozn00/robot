package cn.qssq666.robot.plugin.lua.lib;
import cn.qssq666.CoreLibrary0;
import android.util.Log;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.business.RobotContentProvider;

public class LUAEngineLib extends TwoArgFunction {

    public interface LogCallBack {
        public void onPrint(int type, String funcName, String message);
    }

    public void setLogCallBack(LogCallBack logCallBack) {
        this.logCallBack = logCallBack;
    }

    public static LogCallBack logCallBack;

    private static String TAG = "LUAEngine";


    /**
     * Public constructor. To be loaded via require(), the library class must
     * have a public constructor.
     */
    public LUAEngineLib() {

    }

    /**
     * The implementation of the TwoArgFunction interface. This will be called
     * once when the library is loaded via require().
     *
     * @param modname LuaString containing the name used in the call to require().
     * @param env     LuaValue containing the environment for this function.
     * @return Value that will be returned in the require() call. In this case,
     * it is the library itself.
     */
    public LuaValue call(LuaValue modname, LuaValue env) {
        System.out.println("modname:" + modname + ",length:" + modname.length());
        LuaValue utilLibrary = tableOf();
//		library.set("robotutil", PluginControlmpl.getInstance());
        utilLibrary.set("cosh", new cosh());
        LuaValue configLuaValue =CoerceJavaToLua.coerce(RobotContentProvider.getInstance().getConfigQueryImpl());
        LuaValue controlApiLuaValue =CoerceJavaToLua.coerce(RobotContentProvider.getInstance().getPluginControlInterface());
        utilLibrary.set("config", configLuaValue);
        utilLibrary.set("api", controlApiLuaValue);
        utilLibrary.set("toast", new toastFunction());
//        library.set("logw", new logw());
//        library.set("log", new logFunction());
        utilLibrary.set("error", new errorFunction());
        String importLibraryName = "cn.qssq666.robot.sdk";
        env.set("Util", utilLibrary);
        env.set("logw", new logw());
        env.set("log", new logFunction());
        env.set("error", new errorFunction());
        env.set("warn", new wranFunction());
        env.set("info", new infoFunction());
        env.set("debug", new debugfunction());
        env.set("toast", new toastFunction());
        env.set("config", configLuaValue);
        env.set("api", controlApiLuaValue);
        LuaValue libraryVersion = tableOf();
        libraryVersion.set("code", BuildConfig.VERSION_CODE);
        libraryVersion.set("name", BuildConfig.VERSION_NAME);
        libraryVersion.set("build", BuildConfig.BUILD_TIME_STR);
        libraryVersion.set("package", BuildConfig.APPLICATION_ID);
        env.set("robot", libraryVersion);
        env.set("print", new printfunc());
        env.get("package").get("loaded").set(importLibraryName, utilLibrary);
        return utilLibrary;
    }

    static final class abs extends OneArgFunction {
        public LuaValue call(LuaValue x) {

            return LuaValue.valueOf(Math.sinh(x.checkdouble()));


        }
    }


    /*    static class toastFunction extends VarArgFunction {
            public Varargs invoke(Varargs args) {
                args.checkstring(1)
    //            args.arg1().checktable().sort( args.arg(2).isnil() ? NIL : args.arg(2).checkfunction());
                return NONE;
            }
        }
        */
    static class toastFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String checkstring = arg.checkjstring();
            RobotContentProvider.getInstance().getPluginControlInterface().showDebugToast(checkstring);
            if (logCallBack != null) {
                logCallBack.onPrint(0, "toast", checkstring + "");
            }
            return NONE;
        }

    }

    static class errorFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String checkstring = arg.checkjstring();
            Log.e(TAG, checkstring + "");
            if (logCallBack != null) {
                logCallBack.onPrint(0, "error", checkstring + "");
            }
            return NONE;
        }
    }

    static class debugfunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String checkstring = arg.checkjstring();
            Log.d(TAG, checkstring + "");

            if (logCallBack != null) {
                logCallBack.onPrint(0, "debug", checkstring + "");
            }
            return NONE;
        }

    }


    static class wranFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String checkstring = arg.checkjstring();
            Log.w(TAG, checkstring + "");

            if (logCallBack != null) {
                logCallBack.onPrint(0, "wran", checkstring + "");
            }
            return NONE;
        }

    }


    static class infoFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String checkstring = arg.checkjstring();
            Log.i(TAG, checkstring + "");
            if (logCallBack != null) {
                logCallBack.onPrint(0, "info", checkstring + "");
            }
            return NONE;
        }

    }


    static class logFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue arg) {
            String checkstring = arg.checkjstring();
            Log.w(TAG, checkstring + "");

            if (logCallBack != null) {
                logCallBack.onPrint(0, "log", checkstring + "");
            }
            return NONE;
        }

    }
    /*
    call 2就代表2个参数。
    	public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2);
	}*/


    /**
     * Mathematical sinh function provided as a OneArgFunction.
     */
    static class sinh extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            return LuaValue.valueOf(Math.sinh(x.checkdouble()));
        }
    }


    static class logw extends TwoArgFunction {


        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            if (!arg2.isnil()) {


                String checkjstring = arg1.checkjstring();
                String checkjstring1 = arg2.checkjstring();
                if (logCallBack != null) {
                    logCallBack.onPrint(0, "logw", checkjstring + ":" + checkjstring1);
                }
                Log.w(checkjstring, checkjstring1);

            } else {


                Log.w(TAG, arg2.checkjstring());
            }


            return NONE;
        }

    }

    /**
     * Mathematical cosh function provided as a OneArgFunction.
     */
    static class cosh extends OneArgFunction {
        public LuaValue call(LuaValue x) {
            return LuaValue.valueOf(Math.cosh(x.checkdouble()));
        }
    }

    static class printfunc extends OneArgFunction {
        public LuaValue call(LuaValue x) {

            String checkjstring = x.checkjstring();
            if (logCallBack != null) {
                logCallBack.onPrint(0, "print", checkjstring + "");
            }
            Log.w(TAG, "[print]" + checkjstring);
            return NONE;
        }
    }
}
