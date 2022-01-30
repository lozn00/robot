package cn.qssq666.robot.activity.intent;
import cn.qssq666.CoreLibrary0;import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.plugin.lua.util.LuaUtil;

/**
 * Created by qssq on 2018/11/16 qssq666@foxmail.com
 */
public class RunLuaActivity extends BaseIntentActivity {
    private static final String TAG = "RunLuaTAG";


    protected void onReceiveStr(String fileOrContent) {
        LuaUtil.runByGUI(fileOrContent, false, this, new INotify<Boolean>() {
            @Override
            public void onNotify(Boolean param) {
                finish();
            }
        });
    }

    protected void onReceivePath(String path) {
        LuaUtil.runByGUI(path, true, this, new INotify<Boolean>() {
            @Override
            public void onNotify(Boolean param) {
                finish();
            }
        });
    }

}
