package cn.qssq666.robot.adapter;
import cn.qssq666.CoreLibrary0;import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import cn.qssq666.robot.plugin.sdk.interfaces.AtBeanModelI;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginControlInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.RobotConfigInterface;

/**
 * Created by qssq on 2018/8/28 qssq666@foxmail.com
 */
public class ErrorPluginInterface implements PluginInterface {

    private final File errorFile;
    private  JSONObject jsonObject;

    public ErrorPluginInterface(File file,String string) {
        this.errorFile=file;

        try {
            jsonObject = new JSONObject(string);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPluginSDKVersion() {
        return 0;
    }

    @Override
    public String getAuthorName() {
        if(jsonObject!=null){
            return jsonObject.optString("author");
        }
        return null;
    }

    @Override
    public void onReceiveControlApi(PluginControlInterface instance) {







    }

    public String getPath(){
        if(jsonObject!=null){
            return jsonObject.optString("path");
        }
        return "";
    }


    public String getErrorMsg(){
        if(jsonObject!=null){
            return jsonObject.optString("error");
        }
        return "没有错误";

    }

    @Override
    public int getVersionCode() {
        if(jsonObject!=null){
            return jsonObject.optInt("versioncode");
        }
        return 0;
    }

    @Override
    public String getBuildTime() {
        if(jsonObject!=null){
            return jsonObject.optString("buildTime");
        }
        return null;
    }

    @Override
    public String getVersionName() {
        if(jsonObject!=null){
            return jsonObject.optString("versionname");
        }
        return null;
    }

    @Override
    public String getPackageName() {
        if(jsonObject!=null){
            return jsonObject.optString("packagename");
        }
        return null;
    }

    @Override
    public String getDescript() {
        if(jsonObject!=null){
            return jsonObject.optString("descript");
        }
        return null;
    }

    @Override
    public String getPluginName() {
        if(jsonObject!=null){
            return jsonObject.optString("name");
        }
        return null;
    }

    @Override
    public boolean isDisable() {
        return true;
    }

    @Override
    public void setDisable(boolean disable) {

    }

    @Override
    public void onCreate(Context context) {

    }

    @Override
    public boolean onReceiveMsgIsNeedIntercept(IMsgModel item) {
        return false;
    }

    @Override
    public boolean onReceiveMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean hasAite, boolean hasAiteMe) {
        return false;
    }

    @Override
    public boolean onReceiveOtherIntercept(IMsgModel item, int type) {
        return false;
    }

    @Override
    public void onDestory() {

    }

    @Override
    public void onReceiveRobotConfig(RobotConfigInterface robotConfigInterface) {

    }

    @Override
    public View onConfigUi(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getMinRobotSdk() {

        return 0;
    }

    @Override
    public boolean onReceiveRobotFinalCallMsgIsNeedIntercept(IMsgModel item, List<AtBeanModelI> list, boolean aite, boolean haisaiteme) {
        return false;
    }

    public File getErrorFile() {
        return errorFile;
    }
}
