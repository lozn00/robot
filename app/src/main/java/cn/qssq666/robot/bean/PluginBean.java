package cn.qssq666.robot.bean;

import androidx.databinding.Bindable;

import cn.qssq666.robot.BR;
import cn.qssq666.robot.adapter.ErrorPluginInterface;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.selfplugin.IPluginHolder;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class PluginBean extends AccountBean {


    private IPluginHolder pluginHolder;
    private PluginInterface pluginInterface;

    @Override
    public String getAccount() {
        return pluginInterface.getPackageName();

    }


    public PluginInterface getPluginInterface() {
        return pluginInterface;
    }

    public String getAuthorName() {
        return pluginInterface.getAuthorName();
    }

    public String getPluginName() {
        return pluginInterface.getPluginName();
    }

    public String getDescript() {
        return pluginInterface.getDescript();
    }


    public int getVersionCode() {
        return pluginInterface.getVersionCode();
    }


    public String getVersionName() {
        return pluginInterface.getVersionName();
    }


    public String getPackageName() {
        return pluginInterface.getPackageName();
    }


  /*  String authorName;
    int versionCode;
    String versionName;
    String packageName;*/

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isError() {
        if (pluginHolder!=null&&pluginHolder.getPluginInterface() instanceof ErrorPluginInterface) {
            return true;
        }
        return false;
    }



    String path;

    //    boolean disable;
    @Bindable
    @Override
    public boolean isDisable() {
        if (isError()) {
            return true;
        }

        return RobotContentProvider.getInstance().hasDisablePlugin(this.pluginHolder);
    }

    @Override

    public void setDisable(boolean disable) {

        if (isError()) {
        }


        super.setDisable(disable);


        notifyPropertyChanged(BR.disable);
        RobotContentProvider.getInstance().disablePlugin(this.pluginHolder, disable);
    }

    public void setPluginHolder(IPluginHolder pluginHolder) {
        this.pluginHolder = pluginHolder;
        this.pluginInterface = pluginHolder.getPluginInterface();
    }

    public IPluginHolder getPluginHolder() {
        return pluginHolder;
    }

    /*  String authorName;
    int versionCode;
    String versionName;
    String packageName;
    boolean disable;
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }




    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }




    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }


    @Override
    public String getAuthorName() {
        return authorName;
    }

    @Override
    public int getVersionCode() {
        return versionCode;
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public boolean isDisable() {
        return disable;
    }

    @Override
    public void setDisable(boolean disable) {
        this.disable = disable;

    }

    @Override
    public void onCreate(Context context) {

    }

    @Override
    public void onDestory() {

    }*/
}
