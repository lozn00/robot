package cn.qssq666.robot.plugin.util;

import cn.qssq666.robot.plugin.PluginUtils;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.selfplugin.IPluginHolder;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class QueryPluginModel implements IPluginHolder {
    private boolean hasNewControlMethod;

    public boolean isHasFinalControlMethod() {
        return hasFinalControlMethod;
    }

    private boolean hasFinalControlMethod;

    public boolean isOfficial() {
        return official;
    }

    @Override
    public boolean isAuthorized() {
        return true;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    boolean official;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PluginInterface getPluginInterface() {
        return pluginInterface;
    }

    @Override
    public void setDisableFlag(boolean result) {

        this.disable=result;
    }

    @Override
    public boolean isDisable() {
        return disable;
    }

    @Override
    public boolean hasNewControlApiMethod() {
        return this.hasNewControlMethod;
    }

    boolean disable;

    public void setPluginInterface(PluginInterface pluginInterface) {
        this.pluginInterface = pluginInterface;

        this.hasNewControlMethod=PluginUtils.hasNewControlApiMethod(pluginInterface);
    }

    String path;
    PluginInterface pluginInterface;
}
