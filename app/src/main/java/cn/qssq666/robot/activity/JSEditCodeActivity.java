package cn.qssq666.robot.activity;
import cn.qssq666.CoreLibrary0;
import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import org.apache.commons.io.FilenameUtils;

import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.CodeSymobolBean;
import cn.qssq666.robot.constants.DataSource;
import cn.qssq666.robot.constants.StaticTemp;
import cn.qssq666.robot.ide.JSEditor;
import cn.qssq666.robot.ide.LanguageCode;
import cn.qssq666.robot.plugin.js.ui.JSGUIUtil;
import cn.qssq666.robot.ui.CodeView;

/**
 * Created by qssq on 2018/11/14 qssq666@foxmail.com
 */
public class JSEditCodeActivity extends BaseEditCodeActivity {
    @Override
    protected String getExtName() {
        if (_path != null) {
            return FilenameUtils.getExtension(_path);
        } else if (uri != null) {
            String path = uri.getPath();
            return FilenameUtils.getExtension(path);
        }
        return ".js";
    }

    @Override
    View onCreateCodeView(FrameLayout codeviewContainer) {
   /*     if (true) {
            JSEditorN jsEditorN = new JSEditorN(codeviewContainer.getContext());
            return jsEditorN;
        }*/
        if (StaticTemp.useIde) {
            return new CodeView(codeviewContainer.getContext());
        }
        return new JSEditor(codeviewContainer.getContext());
    }


    @Override
    protected List<CodeSymobolBean> getDefaultSource() {
        return DataSource.getJSCodeSymobolList();
    }

    @Override
    protected String getCurrentRobotPluginDir() {
        return "/sdcard/qssq666/robot_plugin_js";
    }

    @Override
    protected String getCurrentScriptExNme() {
        return ".js";
    }

    @Override
    protected String getCurrentScriptTypeName() {
        return "JS";
    }

    @Override
    protected void setDefaultLanguage() {
        getCodeView().setLang(LanguageCode.JAVASCRIPT);
    }


    @Override
    public String doInsertCodeBy(int which) {
        String string = "";
        switch (which) {
            case 0:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_msg_js);
                break;
            case 1:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_final_msg_js);
                break;
            case 2:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_reply_msg_code_js);
                break;
            case 3:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_toast_js);
                break;
        }
        return string;
    }

    @Override
    public void execSimulator() {
        final String codeStr = getCodeView().getTextCode();
        if (TextUtils.isEmpty(codeStr)) {
            Toast.makeText(this, "内容为空，运行个锤子", Toast.LENGTH_SHORT).show();
        }
        String file = _path;
        final Activity activity = this;
        JSGUIUtil.runReceiveMsgByGUI(codeStr, false, activity);
    }

    @Override
    public boolean execRun() {
        if (!mEnableRun) {
            Toast.makeText(this, "非JS脚本不能运行!", Toast.LENGTH_SHORT).show();
            return true;
        }
        final String mycode = getCodeView().getTextCode();
        String file = _path;
        final Activity activity = this;
        if (TextUtils.isEmpty(mycode)) {
            Toast.makeText(activity, "内容为空，运行个锤子", Toast.LENGTH_SHORT).show();
        }
        JSGUIUtil.runByGUI(mycode, false, activity);
        return false;
    }

}
