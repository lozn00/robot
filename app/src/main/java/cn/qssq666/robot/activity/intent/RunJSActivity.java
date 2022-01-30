package cn.qssq666.robot.activity.intent;
import cn.qssq666.CoreLibrary0;
import cn.qssq666.robot.interfaces.INotify;
import cn.qssq666.robot.plugin.js.ui.JSGUIUtil;

/**
 * Created by qssq on 2018/11/16 qssq666@foxmail.com
 */
public class RunJSActivity extends BaseIntentActivity {
    private static final String TAG = "RunLuaTAG";


    protected void onReceiveStr(String fileOrContent) {
        JSGUIUtil.runByGUI(fileOrContent, false, this, new INotify<Boolean>() {
            @Override
            public void onNotify(Boolean param) {
                finish();
            }
        });
    }

    protected void onReceivePath(String path) {
        JSGUIUtil.runByGUI(path, true, this, new INotify<Boolean>() {
            @Override
            public void onNotify(Boolean param) {
                finish();
            }
        });
    }

}
