package cn.qssq666.robot.plugin.lua.util;
import cn.qssq666.CoreLibrary0;import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.interfaces.OnCareteNotify;
import cn.qssq666.robot.plugin.lua.api.LuaPluginInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.util.QueryPluginModel;
import cn.qssq666.robot.utils.RobotUtil;

/**
 * Created by qssq on 2018/11/11 qssq666@foxmail.com
 */
public class LuaPluginUtil {
    private static final String TAG = "PluginUtils";

    private static FileFilter getPluginFileFilter() {
        return new LuaPluginFilter();
    }

    static class LuaPluginFilter implements FileFilter {


        @Override
        public boolean accept(File pathname) {

            String absolutePath = pathname.getAbsolutePath();
            boolean result = absolutePath.endsWith(".lua") || (absolutePath.endsWith(".txt") && pathname.getName().contains("lua"));
            return result;
        }
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
        File[] files = path.listFiles(new LuaPluginFilter());
        if (files == null || files.length == 0) {
          /*  try {
                new File(path, "test.txt").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }

        ArrayList<QueryPluginModel> list = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File luaFile = files[i];
                QueryPluginModel model = new QueryPluginModel();
                model.setPath(luaFile.getAbsolutePath());
                try {
                    PluginInterface pluginInterface = new LuaPluginInterface(luaFile);
                    model.setPluginInterface(pluginInterface);
                    String packageName = pluginInterface.getPackageName();
                    if (!TextUtils.isEmpty(packageName)) {
                        boolean official = packageName.startsWith(BuildConfig.APPLICATION_ID);
                        model.setOfficial(official);
                    }
                    model.setDisableFlag(RobotContentProvider.getInstance().hasDisablePlugin(model));

                    if (onCreateNotify != null) {
                        onCreateNotify.onEach(pluginInterface);
                    }
                    list.add(model);
                    if (BuildConfig.DEBUG) {

                        Log.w(TAG, "加载插件" + luaFile.getAbsolutePath() + "成功");
                    }

                    RobotContentProvider.getInstance().writePluginErrorLog(luaFile, null);
                } catch (Throwable e) {

                    if (e instanceof ClassNotFoundException) {
                        e.printStackTrace();
                        RobotContentProvider.setLastError(e);
                        String msg = "加载" + luaFile.getName() + "文件插件失败,ClassNotFoundException，导致找不到" + Cns.PLUGIN_MAIN_ENTRY_FILE + "类";
                        Log.e(TAG, msg);


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


                        Log.e(TAG, "加载LUA插件失败,未知异常 " + e.getMessage());

                    } else if (e instanceof Error) {
                        RobotContentProvider.setLastError(e);
                        e.printStackTrace();
                        Log.e(TAG, "加载LUA插件失败,未知错误 " + e.getMessage());

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
                    RobotContentProvider.getInstance().writePluginErrorLog(luaFile, jsonObject.toString());
                }
            }


        } else {
            Log.e(TAG, "没有任何插件");
        }
        return list;
    }

 /*   public static List<QueryPluginModel> loadPlugin(Context context, ClassLoader classLoader, PluginUtils.OnCareteNotify onCareteNotify) {

        return loadPluginFromPath(context, getDefaultPath(context), classLoader,onCareteNotify);
    }*/

    public static List<QueryPluginModel> loadPlugin(Context context, ClassLoader classLoader, OnCareteNotify onCareteNotify) {

        return loadPluginFromPath(context, classLoader, getDefaultPath(context), onCareteNotify);
    }

    public static File getDefaultPath(Context context) {
        File pluginDir = new File(RobotUtil.getBaseDir(), "robot_plugin_lua");
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
        File path = getDefaultPath(context);
        FileFilter suffixFileFilter = getPluginFileFilter();
        File[] files = path.listFiles(suffixFileFilter);
        if (files == null) {
            return -1;
        } else {
            int count = 0;
            for (File file : files) {
                /*if (file.delete()) {

                    File fileTemp = new File(path, ".log");
                    if (fileTemp.exists()) {
                        fileTemp.delete();
                    }
                    fileTemp = new File(path, ".disable");
                    if (fileTemp.exists()) {
                        fileTemp.delete();
                    }
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
        boolean delete = new File(path).delete();
        File file = new File(path + ".log");
        if (file.exists()) {
            file.delete();
        }
        file = new File(path + ".disable");
        if (file.exists()) {

            file.delete();
        }
        return delete ? 1 : 0;
    }
}
