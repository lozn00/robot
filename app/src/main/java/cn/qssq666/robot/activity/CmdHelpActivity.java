package cn.qssq666.robot.activity;
import cn.qssq666.CoreLibrary0;import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.TextView;

import cn.qssq666.robot.R;
import cn.qssq666.robot.config.CmdConfig;

/**
 * Created by luozheng on 2017/4/23.  qssq.space
 */

public class CmdHelpActivity extends SuperActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("机器人命令大全");
//         DataBindingUtil.setContentView(this,R.layout.activity_log);
        setContentView(R.layout.activity_self_cmd);
        ((TextView) findViewById(R.id.tv_update_commend)).setText(CmdConfig.printSupportCmd());
    }

}
