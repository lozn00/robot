package cn.qssq666.robot.plugin.sdk.myimpl;

import androidx.core.util.Pair;

import java.util.List;

import cn.qssq666.robot.BuildConfig;
import cn.qssq666.robot.bean.GagAccountBean;
import cn.qssq666.robot.bean.GroupAtBean;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.business.RobotContentProvider;
import cn.qssq666.robot.constants.ExpandQueryPluginTypeI;
import cn.qssq666.robot.plugin.sdk.interfaces.IGroupConfig;
import cn.qssq666.robot.plugin.sdk.interfaces.IMsgModel;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.RobotConfigInterface;
import cn.qssq666.robot.utils.AccountUtil;
import cn.qssq666.robot.utils.ConfigUtils;
import cn.qssq666.robot.utils.PairFix;

/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 */

public class ConfigQueryImpl implements RobotConfigInterface {
    public static void setRobotContentProvider(RobotContentProvider robotContentProvider) {
        ConfigQueryImpl.robotContentProvider = robotContentProvider;
    }

    public static RobotContentProvider robotContentProvider;

    @Override
    public boolean isEnableGroupMsg() {
        return robotContentProvider.mCfeanbleGroupReply;
    }

    @Override
    public boolean isReplyAiteUser(IGroupConfig config) {
        return false;
    }

    @Override
    public boolean isReplyAiteUser(String group) {
        return robotContentProvider.mCfBaseReplyShowNickName;
    }

    @Override
    public boolean isNeedAiteReply(IGroupConfig config) {
        //isNeedaite

        return false;
    }


    @Override
    public boolean isNeedAiteReply(IMsgModel item) {

        IGroupConfig iGroupConfig = queryGroupConfig(item.getFrienduin());
        return isNeedAiteReply(iGroupConfig);
    }

    @Override
    public boolean checkSensitiveWord(String word) {


        if(isManager(word)){

            return false;

        }


        PairFix<GagAccountBean, String> gagAccountBeanStringPair = RobotContentProvider.getInstance().keyMapContainGag(word, false);

        if(gagAccountBeanStringPair!=null){
            return true;
        }

        return false;
    }

    public boolean isManager(String qq){

        return RobotContentProvider.getInstance().isManager(qq);

    }

    @Override
    public boolean checkSensitiveWordAndUseSystemGag(IMsgModel item) {


        if(isManager(item.getSenderuin())){

            return false;

        }


    PairFix<GagAccountBean, String> gagAccountBeanStringPair = RobotContentProvider.getInstance().keyMapContainGag(item.getMessage(), false);


        if (gagAccountBeanStringPair != null) {
            RobotContentProvider.getInstance().doGagImpLogic(item, gagAccountBeanStringPair, item.getMessage());//
            return true;
        }
//        }


        return false;
    }

    @Override
    public boolean isNeedAiteReply(String group) {

        return ConfigUtils.replyNeedAt(ConfigQueryImpl.robotContentProvider);
    }

    @Override
    public boolean isEnablePrivateReply() {
        return robotContentProvider.mCfprivateReply;
    }

    @Override
    public IGroupConfig queryGroupConfig(String group) {


        return robotContentProvider.getGroupConfig(group);
    }

    @Override
    public boolean isEnableGroupWhilteName() {
        return robotContentProvider.mCfOnlyReplyWhiteNameGroup;
    }

    @Override
    public boolean currentGroupRobotIsShouldReply() {
        return false;
    }

    public boolean hasAite(IMsgModel msgItem){
//        robotContentProvider.hasDisablePlugin()
        Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> atPair;
        if (msgItem.getIstroop()==1) {
            atPair = ConfigUtils.clearAndFetchAtArray((MsgItem) msgItem);
            return atPair.first;
        } else {
            return false;
        }
    }


    public boolean hasAiteMe(IMsgModel msgItem){
//        robotContentProvider.hasDisablePlugin()
        Pair<Boolean, Pair<Boolean, List<GroupAtBean>>> atPair;
        if (msgItem.getIstroop()==1) {
            atPair = ConfigUtils.clearAndFetchAtArray((MsgItem) msgItem);
            return atPair.first&&atPair.second!=null&&atPair.second.first;
        } else {
            return false;
        }
    }



    /**
     * @param typeConfig
     * @return
     */
    @Override
    public boolean generalQuery(int typeConfig) {
        return false;
    }

    @Override
    public boolean generalQueryItem(int type, IMsgModel item) {
            switch (type){
                case ExpandQueryPluginTypeI.MANAGER:
                    return robotContentProvider.isManager(item);
            }
        return false;
    }

    @Override
    public boolean isAtGroupWhiteNames(IMsgModel item) {
        return AccountUtil.isContainAccount(robotContentProvider.mQQGroupWhiteNames,
                item.getFrienduin(), true);
    }

    @Override
    public int getRobotVersion() {
        return cn.qssq666.robot.BuildConfig.VERSION_CODE;
    }

    @Override
    public String getRobotVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public boolean isCompatibility(PluginInterface pluginInterface) {
        if(BuildConfig.VERSION_CODE>=pluginInterface.getMinRobotSdk()){
            return true;
        }
        return false;
    }
}
