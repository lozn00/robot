package cn.qssq666.robot.selfplugin;

/**
 * Created by qssq on 2018/5/17 qssq666@foxmail.com
 */
public interface IPluginHolder {

    /**
     * 是否是官方开发的
     *
     * @return
     */
    public boolean isOfficial();

    /**
     * 是否是授权的
     *
     * @return
     */
    public boolean isAuthorized();


    public String getPath();

    public cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface getPluginInterface();

    /**
     * 内存中保存的，这个东西，判断起来发给你吧
     * @param result
     */

    void setDisableFlag(boolean result);
    boolean isDisable();

    boolean hasNewControlApiMethod();
}
