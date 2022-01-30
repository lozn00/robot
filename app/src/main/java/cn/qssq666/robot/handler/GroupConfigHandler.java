package cn.qssq666.robot.handler;

import cn.qssq666.CoreLibrary0;

import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import cn.qssq666.robot.bean.GroupWhiteNameBean;

/**
 * Created by qssq on 2017/12/22 qssq666@foxmail.com
 */

public class GroupConfigHandler {

    private static final String TAG = "GroupConfigHandler";
    String before = null;

    public GroupWhiteNameBean getWhiteNameBean() {
        return whiteNameBean;
    }

    public void setWhiteNameBean(GroupWhiteNameBean whiteNameBean) {
        this.whiteNameBean = whiteNameBean;
        before = this.whiteNameBean.toString();
    }

    GroupWhiteNameBean whiteNameBean;


    /**
     * 原生 也是使用checkbox但是要完全匹配 setOnCheckListener 否则就提示找不到了.
     * <p>
     * android:onCheckedChanged="@{(view, isChecked) -> handler.onCompletedChanged(view, isChecked)}"
     * 当第一个参数在xml里面没有声明，那么会寻找原生的类型.如果没有寻找到就会出现错。
     * <p>
     * /*
     * public void onCompletedChanged(android.widget.CompoundButton compoundButton, boolean check) {
     * int id = compoundButton.getId();
     * switch (id) {
     * case R.id.cb_enable_local_word:
     * whiteNameBean.setLocalword(check);
     * break;
     * case R.id.cb_enable_net_word:
     * whiteNameBean.setNetword(check);
     * break;
     * case R.id.cb_has_permission:
     * whiteNameBean.setAdmin(check);
     * break;
     * default:
     * return;
     * <p>
     * }
     * <p>
     * <p>
     * }
     */


    public void onEditTextChange() {

    }


    /**
     * 自定义     android:onCheckedChanged="@{(cb, isChecked) -> handler.onCompletedChanged(model, isChecked)}"
     *
     * @param callbackArg_0
     * @param callbackArg_1
     */
    public void onCompletedChanged(GroupWhiteNameBean callbackArg_0, boolean callbackArg_1) {

    }

    public void onCompletedChanged(CheckBox callbackArg_0, boolean callbackArg_1) {

    }

    public boolean isHasChange() {
        Log.w(TAG, whiteNameBean.toString() + "," + before);
        return !whiteNameBean.toString().equals(before);
    }

    public void onTextChanged(EditText editText, CharSequence s, Object model, Object count) {

    }
}
