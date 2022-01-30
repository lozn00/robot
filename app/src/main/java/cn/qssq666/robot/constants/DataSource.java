package cn.qssq666.robot.constants;

import java.util.ArrayList;
import java.util.List;

import cn.qssq666.robot.R;
import cn.qssq666.robot.app.AppContext;
import cn.qssq666.robot.bean.CodeSymobolBean;

/**
 * Created by qssq on 2018/11/17 qssq666@foxmail.com
 */
public class DataSource {
    //ignore_start
    public static List<CodeSymobolBean> getLuaCodeSymobolList() {
        List<CodeSymobolBean> list = new ArrayList<>();
        list.add(CodeSymobolBean.getInstance("函数", AppContext.getInstance().getString(R.string.templete_code_insert_function_lua)));
        list.add(CodeSymobolBean.getInstance("导包", AppContext.getInstance().getString(R.string.templete_code_insert_import_package_lua)));
        list.add(CodeSymobolBean.getInstance("("));
        list.add(CodeSymobolBean.getInstance(")"));
        list.add(CodeSymobolBean.getInstance(".."));
        list.add(CodeSymobolBean.getInstance("\""));
        list.add(CodeSymobolBean.getInstance("="));
        list.add(CodeSymobolBean.getInstance(":"));
        list.add(CodeSymobolBean.getInstance("."));
        list.add(CodeSymobolBean.getInstance(","));
        list.add(CodeSymobolBean.getInstance("注释", "--", CodeSymobolBean.CodeLocation.LINE_START));
        list.add(CodeSymobolBean.getInstance("if", AppContext.getInstance().getString(R.string.templete_code_insert_if_lua)));
        list.add(CodeSymobolBean.getInstance("遍历", AppContext.getInstance().getString(R.string.templete_code_insert_where_lua)));
        list.add(CodeSymobolBean.getInstance("Tab", "\t"));
        list.add(CodeSymobolBean.getInstance("{"));
        list.add(CodeSymobolBean.getInstance("}"));
        list.add(CodeSymobolBean.getInstance("-"));
        list.add(CodeSymobolBean.getInstance("+"));
        list.add(CodeSymobolBean.getInstance("/"));
        list.add(CodeSymobolBean.getInstance("\\"));
        list.add(CodeSymobolBean.getInstance("插件名", AppContext.getInstance().getString(R.string.templete_code_define_plugin_name_lua)));
        list.add(CodeSymobolBean.getInstance("插件作者", AppContext.getInstance().getString(R.string.templete_code_define_plugin_author_lua)));
        list.add(CodeSymobolBean.getInstance("版本名", AppContext.getInstance().getString(R.string.templete_code_define_plugin_version_name_lua)));
        list.add(CodeSymobolBean.getInstance("版本号", AppContext.getInstance().getString(R.string.templete_code_define_plugin_version_code_lua)));
        list.add(CodeSymobolBean.getInstance("全局变量", "a=1"));
        list.add(CodeSymobolBean.getInstance("局部变量", "local a=1"));
        list.add(CodeSymobolBean.getInstance("转换为字符串", "a=1;\nb=tostring(a)"));
        list.add(CodeSymobolBean.getInstance("收到消息方法", AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_msg_lua)));
        list.add(CodeSymobolBean.getInstance("拦截最终消息方法", AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_final_msg_lua)));
        list.add(CodeSymobolBean.getInstance("插入提示Ui", AppContext.getInstance().getString(R.string.templete_code_insert_toast_lua)));
                list.add(CodeSymobolBean.getInstance("实现类/接口", AppContext.getInstance().getString(R.string.templete_code_impl_class_js)));
        return list;
    }

      public static List<CodeSymobolBean> getJSCodeSymobolList() {
        List<CodeSymobolBean> list = new ArrayList<>();
        list.add(CodeSymobolBean.getInstance("函数", AppContext.getInstance().getString(R.string.templete_code_insert_function_js)));
        list.add(CodeSymobolBean.getInstance("("));
        list.add(CodeSymobolBean.getInstance(")"));
        list.add(CodeSymobolBean.getInstance(".."));
        list.add(CodeSymobolBean.getInstance("\""));
        list.add(CodeSymobolBean.getInstance("="));
        list.add(CodeSymobolBean.getInstance(":"));
        list.add(CodeSymobolBean.getInstance("."));
        list.add(CodeSymobolBean.getInstance(","));
        list.add(CodeSymobolBean.getInstance("if", AppContext.getInstance().getString(R.string.templete_code_insert_if_js)));
        list.add(CodeSymobolBean.getInstance("实现类/接口", AppContext.getInstance().getString(R.string.templete_code_impl_class_js)));
        list.add(CodeSymobolBean.getInstance("Tab", "\t"));
        list.add(CodeSymobolBean.getInstance("注释", "//", CodeSymobolBean.CodeLocation.LINE_START));
        list.add(CodeSymobolBean.getInstance("多行注释", "/* code */", CodeSymobolBean.CodeLocation.LINE_START));
        list.add(CodeSymobolBean.getInstance("{"));
        list.add(CodeSymobolBean.getInstance("}"));
        list.add(CodeSymobolBean.getInstance("-"));
        list.add(CodeSymobolBean.getInstance("+"));
        list.add(CodeSymobolBean.getInstance("/"));
        list.add(CodeSymobolBean.getInstance("\\"));
        list.add(CodeSymobolBean.getInstance("插件名", AppContext.getInstance().getString(R.string.templete_code_define_plugin_name_js)));
        list.add(CodeSymobolBean.getInstance("插件作者", AppContext.getInstance().getString(R.string.templete_code_define_plugin_author_js)));
        list.add(CodeSymobolBean.getInstance("版本名", AppContext.getInstance().getString(R.string.templete_code_define_plugin_version_name_js)));
        list.add(CodeSymobolBean.getInstance("版本号", AppContext.getInstance().getString(R.string.templete_code_define_plugin_version_code_js)));
        list.add(CodeSymobolBean.getInstance("全局变量", "a=1"));
        list.add(CodeSymobolBean.getInstance("局部变量", "local a=1"));
        list.add(CodeSymobolBean.getInstance("转换为字符串", "a=1;\nb=tostring(a)"));
        list.add(CodeSymobolBean.getInstance("收到消息方法", AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_msg_js)));
        list.add(CodeSymobolBean.getInstance("拦截最终消息方法", AppContext.getInstance().getString(R.string.templete_code_insert_on_receive_final_msg_js)));
        list.add(CodeSymobolBean.getInstance("插入提示Ui", AppContext.getInstance().getString(R.string.templete_code_insert_toast_js)));
        return list;
    }

    //ignore_end
}
