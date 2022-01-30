package cn.qssq666.robot.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.Cns;
import cn.qssq666.robot.utils.LogUtil;

/**
 * Created by qssq on 2019/1/11 qssq666@foxmail.com
 */
public class CodeUpdateReceiver extends BroadcastReceiver {
    /**
     *
     *
     *
     *
     *
     *
adb shell am broadcast -a cn.qssq666.robot.update_plugin //更新所有 js，lua,java插件
adb shell am broadcast -a cn.qssq666.robot.update_plugin -e plugin_type js
adb shell am broadcast -a cn.qssq666.robot.update_plugin -e plugin_type lua
adb shell am broadcast -a cn.qssq666.robot.update_plugin -e plugin_type java
     //在编辑界面 的执行命令
adb shell am broadcast -a cn.qssq666.robot.run
adb shell am broadcast -a cn.qssq666.robot.run_simulator
    (*/
    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals(Cns.UPDATE_CODE_BROADCAST)) {
            String plugin_type = intent.getStringExtra("plugin_type");
            LogUtil.writeLog("收到插件更新事件，插件类型:" + plugin_type);
            if (TextUtils.isEmpty(plugin_type)) {
                RobotContentProvider.getInstance().reloadPlugin(null);
            } else if (plugin_type.equals("lua")) {
                RobotContentProvider.getInstance().initLuaPlugin();
            } else if (plugin_type.equals("js") || plugin_type.equals("javascript")) {
                RobotContentProvider.getInstance().initJavascriptSPlugin();
            } else if (plugin_type.equals("java")) {
                RobotContentProvider.getInstance().initJAVAPlugin();
            } else {
                AppContext.showToast("无法更新插件,未知类型:" + plugin_type);
            }
        }
    }
}
