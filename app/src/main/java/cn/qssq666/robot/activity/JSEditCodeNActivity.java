package cn.qssq666.robot.activity;
import cn.qssq666.CoreLibrary0;import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import cn.qssq666.robot.R;
import cn.qssq666.robot.adapter.CodeSymobolAdapter;
import cn.qssq666.robot.constants.DataSource;
import cn.qssq666.robot.databinding.ActivityEditJsScriptBinding;

/**
 * Created by qssq on 2018/12/24 qssq666@foxmail.com
 */

@Deprecated
public class JSEditCodeNActivity extends SuperActivity {

    private ActivityEditJsScriptBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_js_script);
        binding.toolbar.setTitle("代码编辑");

        CodeSymobolAdapter adapter = new CodeSymobolAdapter();
        adapter.setList(DataSource.getJSCodeSymobolList());

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.recyclerview.setAdapter(adapter);
    }
}
