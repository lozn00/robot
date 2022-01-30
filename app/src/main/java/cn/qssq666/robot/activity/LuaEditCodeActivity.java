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
import cn.qssq666.robot.ide.LanguageCode;
import cn.qssq666.robot.plugin.lua.util.LuaUtil;
import cn.qssq666.robot.ui.CodeView;

/**
 * Created by qssq on 2018/11/14 qssq666@foxmail.com
 */
public class LuaEditCodeActivity extends BaseEditCodeActivity {
    @Override
    protected String getExtName() {
        if (_path != null) {
            return FilenameUtils.getExtension(_path);
        } else if (uri != null) {
            String path = uri.getPath();
            return FilenameUtils.getExtension(path);
        }
        return ".lua";
    }

    @Override
    View onCreateCodeView(FrameLayout codeviewContainer) {
        return new CodeView(codeviewContainer.getContext());
    }

    protected List<CodeSymobolBean> getDefaultSource() {
        return DataSource.getLuaCodeSymobolList();
    }

    @Override
    protected String getCurrentRobotPluginDir() {
        return "/sdcard/qssq666/robot_plugin_lua";
    }

    @Override
    protected String getCurrentScriptExNme() {
        return ".lua";
    }

    @Override
    protected String getCurrentScriptTypeName() {
        return "LUA";
    }

    @Override
    protected void setDefaultLanguage() {
        getCodeView().setLang(LanguageCode.LUA);
    }


    @Override
    public String doInsertCodeBy(int which) {
        String string = "";
        switch (which) {
            case 0:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_msg_lua);
                break;
            case 1:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_final_msg_lua);
                break;
            case 2:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_reply_msg_code_lua);
                break;
            case 3:
                string = AppContext.getInstance().getString(R.string.templete_code_insert_toast_lua);
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
        LuaUtil.runReceiveMsgByGUI(codeStr, false, activity);
    }

    @Override
    public boolean execRun() {
        if (!mEnableRun) {
            Toast.makeText(this, "非lua脚本不能运行!", Toast.LENGTH_SHORT).show();
            return true;
        }
        final String luaStr = getCodeView().getTextCode();
        String file = _path;
        final Activity activity = this;
        if (TextUtils.isEmpty(luaStr)) {
            Toast.makeText(activity, "内容为空，运行个锤子", Toast.LENGTH_SHORT).show();
        }
        LuaUtil.runByGUI(luaStr, false, activity);
        return false;
    }

}
