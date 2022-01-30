package cn.qssq666.robot.plugin;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.interfaces.OnCareteNotify;
import cn.qssq666.robot.plugin.lua.util.LuaPluginUtil;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.util.PluginFileFilter;
import cn.qssq666.robot.plugin.util.QueryPluginModel;
import cn.qssq666.robot.utils.RobotUtil;
import dalvik.system.DexClassLoader;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class PluginUtils {
    /*
    DexClassLoader介绍

DexClassLoader是一个类加载器，可以用来从.jar和.apk文件中加载class。可以用来加载执行没用和应用程序一起安装的那部分代码。构造函数：DexClassLoader(String dexPath, String optimizedDirectory, String libraryPath, ClassLoader parent)

dexPath:被解压的apk路径，不能为空。

optimizedDirectory：解压后的.dex文件的存储路径，不能为空。这个路径强烈建议使用应用程序的私有路径，不要放到sdcard上，否则代码容易被注入攻击。

libraryPath：os库的存放路径，可以为空，若有os库，必须填写。

作者：大利猫
链接：https://www.jianshu.com/p/43a8a9b932de
來源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
DexClassLoader：能够加载未安装的jar/apk/dex
PathClassLoader：只能加载系统中已经安装过的apk
     */
    private static final String TAG = "PluginUtils";

    private static PluginFileFilter getPluginFileFilter() {
        return new PluginFileFilter();
    }

    public static boolean hasNewControlApiMethod(PluginInterface pluginInterface) {
        try {
            pluginInterface.getClass().getDeclaredMethod("onReceiveMsgIsNeedIntercept", IMsgModel.class, List.class, Boolean.TYPE, Boolean.TYPE);//父类实现的方法都不算，必须是当前类实现的。
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }



    public static List<QueryPluginModel> loadPluginFromPath(Context context, File path, ClassLoader classLoader) {
        return loadPluginFromPath(context, classLoader, path, null);
    }

    public static List<QueryPluginModel> loadPluginFromPath(Context context, ClassLoader classLoader, File path, OnCareteNotify onCreateNotify) {
        PluginFileFilter suffixFileFilter = new PluginFileFilter();
        File[] files = path.listFiles((FileFilter) suffixFileFilter);

        if (files == null || files.length == 0) {
            try {
                new File(path, "test.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<QueryPluginModel> list = new ArrayList<>();
        if (files != null) {


            for (int i = 0; i < files.length; i++) {

                File currentApk = files[i];


                QueryPluginModel model = new QueryPluginModel();
                model.setPath(currentApk.getAbsolutePath());

                //dx  --dex --Output=xx.jar    hello.jar
                ClassLoader dexClassLoader = new DexClassLoader(currentApk.getAbsolutePath(), getDefaultPluginDexPath(context).getAbsolutePath(), null, classLoader);
                try {
                    Class<?> aClass = dexClassLoader.loadClass(Cns.PLUGIN_MAIN_ENTRY_FILE);
//                        Class<?> aPluginSDKClass = dexClassLoader.loadClass(Cns.PLUGIN_INFO_ENTRY_FILE);
                    Object o = aClass.newInstance();
                    if (!(o instanceof PluginInterface)) {
                        Log.e(TAG, "插件借口类型错误，" + o + "的hashCode:" + o.getClass().hashCode() + ",需要的hashCode:" + PluginInterface.class.hashCode());
                        throw new RuntimeException("类型不匹配，期望的类型" + PluginInterface.class.getName() + "当前" + aClass.getName());
                    }
                    PluginInterface pluginInterface = (PluginInterface) o;


                    model.setPluginInterface(pluginInterface);
                    model.setOfficial(pluginInterface.getPackageName().startsWith(BuildConfig.APPLICATION_ID));

                    model.setDisableFlag(RobotContentProvider.getInstance().hasDisablePlugin(model));

//                        if(aPluginSDKClass == RobotGlobaInfo.class){
//                            RobotGlobaInfo aPluginSDKClass1 = (RobotGlobaInfo) aPluginSDKClass;


//                        }
//                        model.setBuildSDKVersion(pluginInterface.get());

                    if (onCreateNotify != null) {
                        onCreateNotify.onEach(pluginInterface);
                    }

                    list.add(model);
                    if (BuildConfig.DEBUG) {

                        Log.w(TAG, "加载插件" + currentApk.getAbsolutePath() + "成功");
                    }


                    RobotContentProvider.getInstance().writePluginErrorLog(currentApk, null);
                } catch (Throwable e) {

                    if (e instanceof ClassNotFoundException) {
                        e.printStackTrace();
                        RobotContentProvider.setLastError(e);
                        String msg = "加载" + currentApk.getName() + "文件插件失败,非机器人插件或插件apk被混淆，导致找不到" + Cns.PLUGIN_MAIN_ENTRY_FILE + "类";
                        if(BuildConfig.DEBUG){
                            Log.e(TAG,"加载插件失败"+Log.getStackTraceString(e));
                        }else{
                        Log.e(TAG, msg+" "+e.getSuppressed());

                        }


                    } else if (e instanceof IllegalAccessException) {
                        RobotContentProvider.setLastError(e);
                        e.printStackTrace();
                        String msg = "加载插件失败,无法创建对象 IllegalAccessException 可能没有访问权限";


                        Log.e(TAG, msg);


                    } else if (e instanceof InstantiationException) {
                        RobotContentProvider.setLastError(e);
                        String msg = "加载插件失败,无法创建对象InstantiationException ";
                        Log.e(TAG, msg);

                    } else if (e instanceof Exception) {

                        e.printStackTrace();


                        Log.e(TAG, "加载JAVA插件失败,未知异常 " + e.getMessage());

                    } else if (e instanceof Error) {
                        RobotContentProvider.setLastError(e);
                        e.printStackTrace();
                        Log.e(TAG, "加载插件失败,未知错误 " + e.getMessage());

                    }

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("path", model.getPath());
                        jsonObject.put("error", Log.getStackTraceString(e));

                        if (model.getPluginInterface() != null) {

                            PluginInterface pluginInterfaceError = model.getPluginInterface();
                            jsonObject.put("author", pluginInterfaceError.getAuthorName());
                            jsonObject.put("buildTime", pluginInterfaceError.getBuildTime());
                            jsonObject.put("descript", pluginInterfaceError.getDescript());
                            jsonObject.put("name", pluginInterfaceError.getPluginName());
                            jsonObject.put("packagename", pluginInterfaceError.getPackageName());
                            jsonObject.put("versionname", pluginInterfaceError.getVersionName());
                            jsonObject.put("versioncode", pluginInterfaceError.getVersionCode());


                        }

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                    RobotContentProvider.getInstance().writePluginErrorLog(currentApk, jsonObject.toString());
                }
            }


        } else {
            Log.e(TAG, "没有任何插件");
        }
        return list;
    }

    public static List<QueryPluginModel> loadPlugin(Context context, ClassLoader classLoader) {

        return loadPluginFromPath(context, getDefaultPluginApkPath(context), classLoader);
    }

    public static List<QueryPluginModel> loadPlugin(Context context, ClassLoader classLoader, OnCareteNotify onCareteNotify) {

        return loadPluginFromPath(context, classLoader, getDefaultPluginApkPath(context), onCareteNotify);
    }

    public static File getDefaultPluginApkPath(Context context) {
        File pluginDir = new File(RobotUtil.getBaseDir(), "robot_plugin");
//        File dataDir = context.getDir("plugin", Context.MODE_PRIVATE);
        if (!pluginDir.exists()) {
            boolean mkdirs = pluginDir.mkdirs();
            Log.w(TAG, "create dir is succ :" + mkdirs);
        }
        return pluginDir;
    }

    public static File getDefaultPluginDexPath(Context context) {
        File dataDir = context.getDir("plugin-dex", Context.MODE_PRIVATE);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        return dataDir;
    }


    public static int deleteAllPlugin(Context context) {
        File path = getDefaultPluginApkPath(context);
        PluginFileFilter suffixFileFilter = getPluginFileFilter();
        File[] files = path.listFiles((FileFilter) suffixFileFilter);

        if (files == null) {
            return -1;
        } else {
            int count = 0;
            for (File file : files) {
            /*    if (file.delete()) {
                    count++;
                }*/
                if (deletePlugin(null, file.getAbsolutePath()) == 1) {
                    count++;

                }
            }
            return count;
        }

    }

    public static int deletePlugin(PluginInterface pluginInterface, String path) {
        return LuaPluginUtil.deletePlugin(pluginInterface, path);
    }
}
